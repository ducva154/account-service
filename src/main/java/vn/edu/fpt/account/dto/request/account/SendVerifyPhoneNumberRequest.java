package vn.edu.fpt.account.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/12/2022 - 14:57
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SendVerifyPhoneNumberRequest implements Serializable {

    private static final long serialVersionUID = 4381673152530979447L;
    private String phoneNumber;
}
