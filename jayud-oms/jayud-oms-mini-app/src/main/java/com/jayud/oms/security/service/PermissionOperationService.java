package com.jayud.oms.security.service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 权限操作
 */
public interface PermissionOperationService {

    /**
     * 权限过滤
     * @param request
     * @param response
     * @param chain
     */
    void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                          FilterChain chain, Set<String> allowAccessPath) throws IOException, ServletException;
}
