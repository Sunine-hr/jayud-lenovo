package com.jayud.oms.security.util;

import com.alibaba.fastjson.JSON;
import com.jayud.common.CommonResult;
import com.jayud.oms.security.enums.MiniResultEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtil {

    /**
     * 返回前端提示信息
     *
     * @param request
     * @param response
     */
    public static void writeError(HttpServletRequest request, HttpServletResponse response, MiniResultEnums resultEnums) {
        PrintWriter write = null;
        try {
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            write = response.getWriter();
            CommonResult responseData = CommonResult.error(resultEnums);
            write.write(JSON.toJSONString(responseData));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (write != null) {
                write.close();
            }
        }
    }

    /**
     * 输出数据
     *
     * @param request
     * @param response
     * @param resultData
     */
    public static void writeData(HttpServletRequest request, HttpServletResponse response,
                                 Map<String, Object> resultData) {

        if (CollectionUtils.isEmpty(resultData)) resultData = new HashMap<>();

        PrintWriter write = null;
        try {
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            write = response.getWriter();
            CommonResult responseData = CommonResult.success(resultData);
            write.write(JSON.toJSONString(responseData));
        } catch (RuntimeException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (write != null) {
                write.close();
            }
        }

    }

    /**
     * 获取头部信息
     *
     * @param key 键
     * @return
     */
    public static String getHead(String key) {
        return getHttpRequestServletContext().getHeader(key);
    }

    /**
     * 获取请求上下文
     */
    public static HttpServletRequest getHttpRequestServletContext() {
        ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servlet.getRequest();
    }

    /**
     * 获取响应上下文
     */
    public static HttpServletResponse getHttpResponseServletContext() {
        ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servlet.getResponse();
    }
}
