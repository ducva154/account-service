package vn.edu.fpt.account.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.account.config.security.annotation.IsAdmin;
import vn.edu.fpt.account.config.security.annotation.IsViewer;
import vn.edu.fpt.account.constant.ResponseStatusEnum;
import vn.edu.fpt.account.controller.AccountController;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.common.SortableRequest;
import vn.edu.fpt.account.dto.request.account.*;
import vn.edu.fpt.account.dto.response.account.*;
import vn.edu.fpt.account.factory.ResponseFactory;
import vn.edu.fpt.account.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 14:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final ResponseFactory responseFactory;

    @Override
    @IsViewer
    public ResponseEntity<GeneralResponse<CreateAccountResponse>> createAccount(CreateAccountRequest request) {
        return responseFactory.response(accountService.createAccount(request), ResponseStatusEnum.CREATED);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetAccountResponse>>> getAccountByCondition(
            String accountId,
            String accountIdSortBy,
            String email,
            String roleId,
            List<String> role,
            String emailSortBy,
            String username,
            String usernameSortBy,
            String fullName,
            String fullNameSortBy,
            String createdDateFrom,
            String createdDateTo,
            String createdDateSortBy,
            String lastModifiedDateFrom,
            String lastModifiedDateTo,
            String lastModifiedDateSortBy,
            Integer page,
            Integer size) {
        List<SortableRequest> sortableRequests = new ArrayList<>();
        if (Objects.nonNull(accountIdSortBy)) {
            sortableRequests.add(new SortableRequest("_id", accountIdSortBy));
        }
        if (Objects.nonNull(emailSortBy)) {
            sortableRequests.add(new SortableRequest("email", emailSortBy));
        }
        if (Objects.nonNull(usernameSortBy)) {
            sortableRequests.add(new SortableRequest("username", usernameSortBy));
        }
        if (Objects.nonNull(fullNameSortBy)) {
            sortableRequests.add(new SortableRequest("full_name", fullNameSortBy));
        }
        if (Objects.nonNull(createdDateSortBy)) {
            sortableRequests.add(new SortableRequest("created_date", createdDateSortBy));
        }
        if (Objects.nonNull(lastModifiedDateSortBy)) {
            sortableRequests.add(new SortableRequest("last_modified_date", lastModifiedDateSortBy));
        }

        GetAccountRequest request = GetAccountRequest.builder()
                .accountId(accountId)
                .email(email)
                .roleId(roleId)
                .role(role)
                .username(username)
                .fullName(fullName)
                .createdDateFrom(createdDateFrom)
                .createdDateTo(createdDateTo)
                .lastModifiedDateFrom(lastModifiedDateFrom)
                .lastModifiedDateTo(lastModifiedDateTo)
                .page(page)
                .size(size)
                .sortBy(sortableRequests)
                .build();
        return responseFactory.response(accountService.getAccountByCondition(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<LoginResponse>> login(LoginRequest request) {
        return responseFactory.response(accountService.login(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<LoginResponse>> refreshToken(RefreshTokenRequest request) {
        return responseFactory.response(accountService.refreshToken(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> changePassword(String id, ChangePasswordRequest request) {
        accountService.changePassword(id, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> changeEmail(String id, ChangeEmailRequest request) {
        accountService.changeEmail(id, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> resetPassword(ResetPasswordRequest request) {
        accountService.resetPassword(request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    @IsAdmin
    public ResponseEntity<GeneralResponse<Object>> deleteAccountById(String id) {
        accountService.deleteAccountById(id);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> addRoleToAccount(String id, AddRoleToAccountRequest request) {
        accountService.addRoleToAccount(id, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> removeRoleFromAccount(String id, String roleId) {
        accountService.removeRoleFromAccount(id, roleId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetAccountNotInLabResponse>>> getAccountNotInLab(GetAccountNotInLabRequest request) {
        return responseFactory.response(accountService.getAccountNotInLab(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetOTTResponse>> getOTT(GenerateOTTRequest request) {
        return responseFactory.response(accountService.generateOTT(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<LoginResponse>> verifyOTT(VerifyOTTRequest request) {
        return responseFactory.response(accountService.verifyOTT(request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> sendVerifyPhoneNumber(String id, SendVerifyPhoneNumberRequest request) {
        accountService.sendVerifyPhoneNumber(id, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> verifyPhoneNumber(String id, VerifyPhoneNumberRequest request) {
        accountService.verifyPhoneNumber(id, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }
}
