package com.megait.nocoronazone.form;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.ProfileImage;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SettingForm {

    private String email;
    private Long fileNo;


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


    private String introduce;

    private boolean certification;

    private ProfileImage profileImage;




}
