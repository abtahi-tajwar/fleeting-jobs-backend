package com.fleetingtrails.fleetingjobsbackend.auth.filter;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.PermissionRepository;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final PermissionRepository permissionRepository;


    private boolean equalsIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Authorize authorize = handlerMethod.getMethodAnnotation(Authorize.class);

        // No authorization annotation = allow
        if (authorize == null) {
            return true;
        }

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;

        }

        if (!(authentication.getPrincipal() instanceof UserEntity user)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        RoleEntity role = user.getRole();
        List<PermissionEntity> permissions = permissionRepository.findByRole(role);

        // Your authorization logic here
        AppModule module = authorize.module();
        AppModule.Submodule submodule = authorize.submodule();
        String action = authorize.action();

        boolean allowed = permissions.stream()
                .anyMatch(permission ->
                        permission.getModule() == module
                                && permission.getSubmodule() == submodule
                                && equalsIgnoreCase(permission.getAction(), action)
                );

        if (!allowed) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;

        }

        return true;

    }
}
