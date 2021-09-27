package com.megait.nocoronazone.configuration;

import com.megait.nocoronazone.service.CustomOAuth2UserService;
import com.megait.nocoronazone.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final MemberService memberService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .mvcMatchers("/css/**","/img/**", "/js/**", "/svg/**", "/ws/**", "/map/**").permitAll()

                .mvcMatchers("/","/slide", "/login", "/signup", "/check_nickname","/logout",
                        "/infection", "/density", "/distancing", "/clinic",
                        "/video","/news","/article","/local_article","/svg","/vaccine",
                        "/cosns", "/timeline_location","/mention/write","/mention_detail",
                        "/timeline_follow", "/search","/remention","/follow","/unfollow","/delete_follower",
                        "/chat", "/mention","/find_pw","/create_code","/check_code","/change_pw").permitAll()


                .mvcMatchers("https://nip.kdca.go.kr/irgd/cov19stats.do?list=all").permitAll()


                .anyRequest().authenticated()

                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)

                .and()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)


                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");

//                http.rememberMe()
//                .userDetailsService(memberService)
//                .tokenRepository(tokenRepository());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
