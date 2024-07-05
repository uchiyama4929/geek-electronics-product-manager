package com.example.demo.form;

import com.example.demo.validation.FieldMatch;
import com.example.demo.validation.ValidPermissionId;
import com.example.demo.validation.ValidPositionId;
import com.example.demo.validation.ValidStoreId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;


@Data
@FieldMatch(first = "password", second = "passwordConfirmation", message = "パスワードが確認用と一致しません。")
public class ManagerForm implements Serializable {

    private String id;

    @NotBlank
    @ValidStoreId
    private String storeId;

    @NotBlank
    @ValidPositionId
    private String positionId;

    @NotBlank
    @ValidPermissionId
    private String permissionId;

    @NotBlank
    @Size(max = 255, message = "姓は255文字以下で入力してください。")
    private String lastName;

    @NotBlank
    @Size(max = 255, message = "名は255文字以下で入力してください。")
    private String firstName;

    @NotBlank
    @Email
    @Size(max = 255, message = "メールアドレスは255文字以下で入力してください。")
    private String email;

    @NotBlank
    @Size(min = 10, max = 11)
    private String phoneNumber;

    @Size(max = 255, message = "パスワードは255文字以下で入力してください。")
    private String password;

    private String passwordConfirmation;
}
