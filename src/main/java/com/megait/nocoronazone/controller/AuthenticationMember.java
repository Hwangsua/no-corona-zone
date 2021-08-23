package com.megait.nocoronazone.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthenticationMember {
<<<<<<< HEAD

=======
>>>>>>> c043c797975fb0c69a019643ff206aa3c3aa37fd
}
