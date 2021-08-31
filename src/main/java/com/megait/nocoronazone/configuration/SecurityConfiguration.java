package com.megait.nocoronazone.configuration;

import com.megait.nocoronazone.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers("/css/**","/img/**", "/js/**", "/svg/**", "/ws/**", "/map/**").permitAll()


                .mvcMatchers("/","/slide", "/login", "/signup", "/nicknameCk","/logout",
                        "/infection", "/density", "/distancing", "/clinic",
                        "/video","/news","/article","/local_article","/svg","/vaccine",
                        "/cosns", "/timeline_location","/mention/write","/mention_detail",
                        "/timeline_follow", "/remention", "/search", "/following","/follower","/{nickname}",
                        "/chat", "/mention").permitAll()

                .mvcMatchers("https://nip.kdca.go.kr/irgd/cov19stats.do?list=all").permitAll()

                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)

//                .and()
                .and()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)

                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true) // 로그아웃했을때 세션을 갱신
                .logoutSuccessUrl("/") // 로그아웃하면 메인으로 가게
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
