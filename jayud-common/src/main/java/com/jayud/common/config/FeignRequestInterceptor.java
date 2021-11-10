package com.jayud.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @desc: 微服务之间使用Feign调用接口时, 透传header参数信息
 * @author: cao_wencao
 * @date: 2020-09-25 16:36
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                // 跳过 content-length
                if (name.equals("content-length")) {
                    continue;
                }
                requestTemplate.header(name, values);

            }
        } else {
            log.info("feign interceptor error header:{}", requestTemplate);
        }
        Object kid = request.getAttribute("TX_XID");
        if (kid != null) {
            requestTemplate.header("TX_XID", kid.toString());
        }

//        requestTemplate.header(RootContext.KEY_XID, RootContext.getXID());
        Enumeration<String> bodyNames = request.getParameterNames();
        StringBuffer body = new StringBuffer();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String values = request.getParameter(name);
                body.append(name).append("=").append(values).append("&");
            }
        }
        if (body.length() != 0) {
            body.deleteCharAt(body.length() - 1);
            requestTemplate.body(body.toString());
            log.info("feign interceptor body:{}", body.toString());
        }
    }
}