package com.jayud.aspects;

import cn.hutool.json.JSONUtil;
import com.jayud.model.po.GeneralApiLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 统一接口日志切面
 * <li>监控接口方法入参，将入参整合为json存储
 * <li>监控接口方法返回值，将结果整合为json存储
 * <li>监控接口请求的路径
 * <li>监控接口操作时间
 * <br>通过kafka将组装好的数据写入数据库
 *
 * @author william
 * @description
 * @Date: 2020-09-10 15:35
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class GeneralAPILogAspect {
//    @Autowired
//    GeneralLogMsgClient msgClient;

    @Pointcut("@annotation(com.jayud.annotations.APILog)")
    public void catchLog() {
    }

    @Around("catchLog()")
    public void doAround(ProceedingJoinPoint process) throws Throwable {
        Long startTime = System.currentTimeMillis();
        //获取当前请求的路径
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        //获取请求方法名
        String methodName = process.getSignature().getName();
        //获取请求入参
        Object[] params = process.getArgs();
        //获取签名
        Signature signature = process.getSignature();
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取被@RequestBody标记的入参，转为json
        Object parameter = getParameter(methodSignature.getMethod(), params);
        String requestParameterString = JSONUtil.toJsonStr(parameter);

        Object Result = process.proceed();

        String resultParameterString = JSONUtil.toJsonStr(Result);

        Long endTime = System.currentTimeMillis();
        Integer timeSpan = (int) (endTime - startTime);

        GeneralApiLog apiLog = new GeneralApiLog();
        apiLog.setMethod(methodName);
        apiLog.setRequestJson(requestParameterString);
        apiLog.setResultJson(resultParameterString);
        //todo 获取远程访问者ID
        apiLog.setIpAddress(request.getRemoteAddr());
        //todo 加入通过会话ID从redis获取用户ID的功能
        apiLog.setUserId(1);
        apiLog.setTimeSpan(timeSpan);
        apiLog.setRequestTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));

        //todo 配置kafka异步处理
//        msgClient.saveLog(JSONUtil.toJsonStr(apiLog));
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
                continue;
            }

            //将RequestParam注解修饰的参数作为请求参数(不适用)
//            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
//            if (requestParam != null) {
//                Map<String, Object> map = new HashMap<>();
//                String key = parameters[i].getName();
//                if (!StringUtils.isEmpty(requestParam.value())) {
//                    key = requestParam.value();
//                }
//                map.put(key, args[i]);
//                argList.add(map);
//            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
