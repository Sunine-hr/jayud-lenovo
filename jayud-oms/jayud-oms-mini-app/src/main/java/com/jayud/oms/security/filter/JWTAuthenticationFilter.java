package com.jayud.oms.security.filter;

import com.jayud.oms.security.properties.JayudSecurityProperties;
import com.jayud.oms.security.service.PermissionOperationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private final Set<String> allowAccessPath;
    private PermissionOperationService permissionOperationService;
    private JayudSecurityProperties jayudSecurityProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, PermissionOperationService permissionOperationService,
                                   JayudSecurityProperties jayudSecurityProperties) {
        super(authenticationManager);
        this.permissionOperationService = permissionOperationService;
        this.jayudSecurityProperties = jayudSecurityProperties;
        //允许哪些接口通过
        allowAccessPath = new HashSet<>(Arrays.asList(jayudSecurityProperties.getPermitAll()));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        permissionOperationService.doFilterInternal(request, response, chain, allowAccessPath);
    }

}
