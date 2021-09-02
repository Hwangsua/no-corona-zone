package com.megait.nocoronazone.domain;

import com.megait.nocoronazone.form.SettingForm;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(nullable = false)
    private String nickname;

    private String introduce;

    private boolean certification;

    private String emailCheckToken;

    @Column(nullable = false)
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @OneToOne(mappedBy = "member")
    private ProfileImage profileImage;

    public void generateEmailCheckToken(){
        emailCheckToken = UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token) {
        return emailCheckToken.equals(token);
    }

    public void completeSignup(){
        emailVerified = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public void update(SettingForm settingForm) {
        this.nickname = settingForm.getNickname();
        this.password = settingForm.getPassword();
        this.introduce = settingForm.getIntroduce();
        this.certification = settingForm.isCertification();
    }

    @Override
    public String toString() {
        return "Member{" +
                "no=" + no +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", authType=" + authType +
                ", nickname='" + nickname + '\'' +
                ", introduce='" + introduce + '\'' +
                ", certification=" + certification +
                ", emailCheckToken='" + emailCheckToken + '\'' +
                ", emailVerified=" + emailVerified +
                ", memberType=" + memberType;
    }
}
