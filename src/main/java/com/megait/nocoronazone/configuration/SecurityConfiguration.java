package com.megait.nocoronazone.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/signup", "/nicknameCk","/logout","/settings",
                        "/infection", "/density", "/distancing", "/clinic",
                        "/vaccine", "/news",  "/news/article", "/news/video",
                        "/cosns", "/timeline_location","/mention/write","/mention_detail",
                        "/remention", "/search", "/following","/follower","/{nickname}").permitAll()

                .mvcMatchers("/css/**","/img/**", "/js/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")  // 안해도 기본값이 이미 '/login'임
                .defaultSuccessUrl("/", true)

                .and()
                .logout()
                .logoutUrl("/logout") // 안해도 기본값이 이미 '/logout'임임
                .invalidateHttpSession(true) // 로그아웃했을때 세션을 갱신
                .logoutSuccessUrl("/"); // 로그아웃하면 메인으로 가게
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
