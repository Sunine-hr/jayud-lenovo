package com.jayud.oauth.config;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 用户拦截器，用户已经身份验证/记住我登录的都可
 * @author bocong.zheng
 * @version 20160329
 */
public class UserLevelFilter extends UserFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse;
        HttpServletRequest httpRequest;
        Map<String, Object> map = new HashMap<String, Object>();
        httpResponse = WebUtils.toHttp(response);
        httpRequest = WebUtils.toHttp(request);
        WebUtils.saveRequest(request);
        map.put("requestUri", StringUtils.isEmpty(httpRequest.getRequestURI())?"":httpRequest.getRequestURI());
        map.put("requestUrl", Objects.isNull(httpRequest.getRequestURL())?"":httpRequest.getRequestURL());
        httpResponse.setStatus(HttpStatus.HTTP_OK, "redirect");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpResponse.getWriter();
            out.append(JSONUtil.toJsonStr(CommonResult.error(ResultEnum.UNAUTHORIZED)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return false;
    }
}