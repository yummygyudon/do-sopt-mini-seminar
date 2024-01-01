package org.sopt.global.Interceptor.annotation;

import org.sopt.entity.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionScope {
    Role[] targetRoles() default { Role.MASTER, Role.ADMIN, Role.MEMBER };
}
