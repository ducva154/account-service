package vn.edu.fpt.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.common.UserInfoResponse;
import vn.edu.fpt.account.dto.request.role.*;
import vn.edu.fpt.account.dto.response.role.CreateRoleResponse;
import vn.edu.fpt.account.dto.response.role.GetRoleResponse;
import vn.edu.fpt.account.entity._Permission;
import vn.edu.fpt.account.entity._Role;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.repository.BaseMongoRepository;
import vn.edu.fpt.account.repository.PermissionRepository;
import vn.edu.fpt.account.repository.RoleRepository;
import vn.edu.fpt.account.service.PermissionService;
import vn.edu.fpt.account.service.RoleService;
import vn.edu.fpt.account.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 14:23
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final UserInfoService userInfoService;
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void init() {
        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {
            _Role adminRole = _Role.builder()
                    .roleName("ADMIN")
                    .description("Role for ADMIN")
                    .permissions(List.of(permissionRepository.findByPermissionName("readAndWriteAnyProject").orElseThrow(() -> new BusinessException("Permission readAndWriteAnyProject not exist"))))
                    .createdBy(null)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedBy(null)
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            try {
                roleRepository.save(adminRole);
                log.info("Init ADMIN role success");
            } catch (Exception ex) {
                throw new BusinessException("Can't init ADMIN role in database: " + ex.getMessage());
            }
        }
        if (roleRepository.findByRoleName("MANAGER").isEmpty()) {
            _Role managerRole = _Role.builder()
                    .roleName("MANAGER")
                    .description("Role for MANAGER")
                    .permissions(List.of(permissionRepository.findByPermissionName("readAndWriteProject").orElseThrow(() -> new BusinessException("Permission readAndWriteProject not exist"))))
                    .createdBy(null)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedBy(null)
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            try {
                roleRepository.save(managerRole);
                log.info("Init MANAGER role success");
            } catch (Exception ex) {
                throw new BusinessException("Can't init MANAGER role in database: " + ex.getMessage());
            }
        }
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            _Role userRole = _Role.builder()
                    .roleName("USER")
                    .description("Role for USER")
                    .permissions(List.of(permissionRepository.findByPermissionName("readProject").orElseThrow(() -> new BusinessException("Permission readProject not exist"))))
                    .createdBy(null)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedBy(null)
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            try {
                roleRepository.save(userRole);
                log.info("Init USER role success");
            } catch (Exception ex) {
                throw new BusinessException("Can't init USER role in database: " + ex.getMessage());
            }
        }
    }

    @Override
    public CreateRoleResponse createRole(CreateRoleRequest request) {
        Optional<_Role> roleOptional = roleRepository.findByRoleName(request.getRoleName());
        if (roleOptional.isPresent()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role name already exist in database");
        }
        _Permission permission = permissionRepository.findByPermissionName("readProject")
                .orElseThrow(() -> new BusinessException("Missing default permission: readProject"));

        _Role role = _Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .permissions(List.of(permission))
                .build();

        try {
            role = roleRepository.save(role);
            log.info("Create role success: {}", role);
        } catch (Exception ex) {
            throw new BusinessException("Can't save new role to database: " + ex.getMessage());
        }
        return CreateRoleResponse.builder()
                .roleId(role.getRoleId())
                .build();
    }

    @Override
    public void updateRole(String roleId, UpdateRoleRequest request) {
        _Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role id not found"));

        if (Objects.nonNull(request.getRoleName())) {
            if (roleRepository.findByRoleName(request.getRoleName()).isPresent()) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role name already in database");
            }
            log.info("Update role name: {}", request.getRoleName());
            role.setRoleName(request.getRoleName());
        }
        if (Objects.nonNull(request.getDescription())) {
            log.info("Update description: {}", request.getDescription());
            role.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getIsEnable())) {
            log.info("Update is enable status: {}", request.getIsEnable());
            role.setIsEnable(request.getIsEnable());
        }
        try {
            roleRepository.save(role);
            log.info("Update role success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save role in database when update role: " + ex.getMessage());
        }
    }

    @Override
    public void deleteRole(String roleId) {
        roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role ID not found"));
        try {
            roleRepository.deleteById(roleId);
            log.info("Delete role: {} success", roleId);
        } catch (Exception ex) {
            throw new BusinessException("Can't delete role by ID: " + ex.getMessage());
        }
    }

    @Override
    public void addPermissionToRole(String roleId, AddPermissionToRoleRequest request) {
        _Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role id not found"));
        Optional<_Permission> permissionInRole = role.getPermissions().stream().filter(v -> v.getPermissionId().equals(request.getPermissionId()))
                .findAny();
        if (permissionInRole.isPresent()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role already contain this permission");
        }
        _Permission permission = permissionRepository.findById(request.getPermissionId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Permission id not found"));
        List<_Permission> permissions = role.getPermissions();
        permissions.add(permission);
        role.setPermissions(permissions);
        try {
            roleRepository.save(role);
            log.info("Add permission to role success");
        } catch (Exception ex) {
            throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can not add permission to role in database: " + ex.getMessage());
        }
    }

    @Override
    public PageableResponse<GetRoleResponse> getRoleByCondition(GetRoleRequest request) {
        Query query = new Query();
        if (Objects.nonNull(request.getRoleId())) {
            query.addCriteria(Criteria.where("_id").is(request.getRoleId()));
        }
        if (Objects.nonNull(request.getRoleName())) {
            query.addCriteria(Criteria.where("role_name").regex(request.getRoleName()));
        }
        if (Objects.nonNull(request.getDescription())) {
            query.addCriteria(Criteria.where("description").regex(request.getDescription()));
        }
        if (Objects.nonNull(request.getIsEnable())) {
            query.addCriteria(Criteria.where("is_enable").is(request.getIsEnable()));
        }
        BaseMongoRepository.addCriteriaWithAuditable(query, request);
        Long totalElements = mongoTemplate.count(query, _Role.class);
        BaseMongoRepository.addCriteriaWithPageable(query, request);
        BaseMongoRepository.addCriteriaWithSorted(query, request);

        List<_Role> roles = mongoTemplate.find(query, _Role.class);
        List<GetRoleResponse> roleResponses = roles.stream().map(this::convertToRoleResponse).collect(Collectors.toList());

        return new PageableResponse<>(request, totalElements, roleResponses);
    }

    @Override
    public void removePermissionFromRole(String roleId, String permissionId) {
        _Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Role id not found"));

        List<_Permission> permissions = role.getPermissions();
        Optional<_Permission> permissionInRole = permissions.stream().filter(v -> v.getPermissionId().equals(permissionId)).findAny();

        if (permissionInRole.isEmpty()) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Permission id not found");
        }

        if (permissions.remove(permissionInRole.get())) {
            role.setPermissions(permissions);
            try {
                roleRepository.save(role);
                log.info("Remove permission from role success");
            } catch (Exception ex) {
                throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can't update role in database after remove permission");
            }
        } else {
            throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can't remove permission from role");
        }
    }

    public GetRoleResponse convertToRoleResponse(_Role role) {
        return GetRoleResponse.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .isEnable(role.getIsEnable())
                .description(role.getDescription())
                .permissions(role.getPermissions().stream().map(permissionService::convertToPermissionResponse).collect(Collectors.toList()))
                .createdBy(UserInfoResponse.builder()
                        .accountId(role.getCreatedBy())
                        .userInfo(userInfoService.getUserInfo(role.getCreatedBy()))
                        .build())
                .createdDate(role.getCreatedDate())
                .lastModifiedBy(UserInfoResponse.builder()
                        .accountId(role.getCreatedBy())
                        .userInfo(userInfoService.getUserInfo(role.getCreatedBy()))
                        .build())
                .lastModifiedDate(role.getLastModifiedDate())
                .build();
    }
}
