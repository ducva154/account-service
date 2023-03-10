package vn.edu.fpt.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.account.config.security.annotation.IsAdmin;
import vn.edu.fpt.account.dto.common.GeneralResponse;
import vn.edu.fpt.account.dto.common.PageableResponse;
import vn.edu.fpt.account.dto.request.account.*;
import vn.edu.fpt.account.dto.response.account.*;

import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 14:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequestMapping("${app.application-context}/public/api/v1/accounts")
public interface AccountController {

    @PostMapping("/account")
    ResponseEntity<GeneralResponse<CreateAccountResponse>> createAccount(@RequestBody CreateAccountRequest request);

    @GetMapping
    ResponseEntity<GeneralResponse<PageableResponse<GetAccountResponse>>> getAccountByCondition(
            @RequestParam(name = "account-id", required = false) String accountId,
            @RequestParam(name = "account-id-sort-by", required = false) String accountIdSortBy,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "role-id", required = false) String roleId,
            @RequestParam(name = "role", required = false) List<String> role,
            @RequestParam(name = "email-sort-by", required = false) String emailSortBy,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "username-sort-by", required = false) String usernameSortBy,
            @RequestParam(name = "full-name", required = false) String fullName,
            @RequestParam(name = "full-name-sort-by", required = false) String fullNameSortBy,
            @RequestParam(name = "created-date-from", required = false) String createdDateFrom,
            @RequestParam(name = "created-date-to", required = false) String createdDateTo,
            @RequestParam(name = "created-date-sort-by", required = false) String createdDateSortBy,
            @RequestParam(name = "last-modified-date-from", required = false) String lastModifiedDateFrom,
            @RequestParam(name = "last-modified-date-to", required = false) String lastModifiedDateTo,
            @RequestParam(name = "last-modified-date-sort-by", required = false) String lastModifiedDateSortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size
    );

    @PostMapping("/account/login")
    ResponseEntity<GeneralResponse<LoginResponse>> login(@RequestBody LoginRequest request);

    @PostMapping("/token/refresh")
    ResponseEntity<GeneralResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request);

    @PutMapping("/{id}/password")
    ResponseEntity<GeneralResponse<Object>> changePassword(
            @PathVariable("id") String id,
            @RequestBody ChangePasswordRequest request);

    @PutMapping("/{id}/email")
    ResponseEntity<GeneralResponse<Object>> changeEmail(
            @PathVariable String id,
            @RequestBody ChangeEmailRequest request);

    @PostMapping("/{id}/phone-number")
    ResponseEntity<GeneralResponse<Object>> sendVerifyPhoneNumber(@PathVariable(name = "id") String id, @RequestBody SendVerifyPhoneNumberRequest request);

    @PostMapping("/{id}/phone-number/verify")
    ResponseEntity<GeneralResponse<Object>> verifyPhoneNumber(@PathVariable(name = "id") String id, @RequestBody VerifyPhoneNumberRequest request);

    @PostMapping("/password/reset")
    ResponseEntity<GeneralResponse<Object>> resetPassword(@RequestBody ResetPasswordRequest request);

    @DeleteMapping("/{id}")
    @IsAdmin
    ResponseEntity<GeneralResponse<Object>> deleteAccountById(@PathVariable("id") String id);

    @PostMapping("/{id}/role")
    @IsAdmin
    ResponseEntity<GeneralResponse<Object>> addRoleToAccount(
            @PathVariable("id") String id,
            @RequestBody AddRoleToAccountRequest request);

    @DeleteMapping("/{id}/roles/{role-id}")
    @IsAdmin
    ResponseEntity<GeneralResponse<Object>> removeRoleFromAccount(
            @PathVariable("id") String id,
            @PathVariable("role-id") String roleId
    );

    @PostMapping("/lab")
    ResponseEntity<GeneralResponse<PageableResponse<GetAccountNotInLabResponse>>> getAccountNotInLab(@RequestBody GetAccountNotInLabRequest request);

    @PostMapping("/account/ott")
    ResponseEntity<GeneralResponse<GetOTTResponse>> getOTT(@RequestBody GenerateOTTRequest request);

    @PostMapping("/account/ott/verify")
    ResponseEntity<GeneralResponse<LoginResponse>> verifyOTT(@RequestBody VerifyOTTRequest request);
}
