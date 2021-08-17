package com.megait.nocoronazone.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank(message = "필수 항목입니다.")
    @Length(min = 5, max = 40, message = "이메일은 5자 이상 40자 이하여야합니다.")
    @Email(message = "이메일 형식을 지켜주세요. (예. test@gemail.com)")
    private String email;

    @Pattern(
            regexp = "^[a-zA-Z가-힣][0-9a-zA-Z가-힣]{3,7}$",
            message = "닉네임은 영어 또는 한글로 시작하는 4자 이상 8자 이하여야합니다."
    )
    private String nickname;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,}$",
            message = "패스워드는 영문자, 숫자, 특수기호를 조합하여 최소 8자 이상을 입력하셔야 합니다."
    )
    private String password;

    @NotBlank(message = "반드시 약관에 동의하셔야합니다.")
    private String agreeTermsOfService;


}
