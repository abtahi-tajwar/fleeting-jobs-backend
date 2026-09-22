package com.fleetingtrails.fleetingjobsbackend.auth.filter;

import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.repository.PermissionRepository;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final PermissionRepository permissionRepository;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        UserEntity userDetails =  (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert userDetails != null;
        List<PermissionEntity> permissionsList = permissionRepository.findByRole(userDetails.getRole());
        request.setAttribute("permissions", permissionsList);
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        filterChain.doFilter(request, response);
    }
}
