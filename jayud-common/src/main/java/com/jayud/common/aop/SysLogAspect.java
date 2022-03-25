package com.jayud.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.jayud.common.HttpContextUtils;
import com.jayud.common.aop.annotations.SysLog;
import com.jayud.common.dto.LogDTO;
import com.jayud.common.enums.SysLogOperateTypeEnum;

import com.jayud.common.service.BaseCommonService;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @author ciro
 * @date 2021/12/13 16:50
 * @description: 系统日志，切面处理
 */
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private BaseCommonService baseCommonService;

    @Pointcut("@annotation(com.jayud.common.aop.annotations.SysLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveSysLog(point, time, result);
        System.out.println("前置通知！");
        return result;
    }

    // 后置通知   没用到
    @After("@annotation(com.jayud.common.aop.annotations.Action)")
    public void after(JoinPoint  point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
//        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
//        saveSysLog(point, time, result);

        System.out.println("后置通知！" + point);


        Signature signature = point.getSignature();
        String name = signature.getName();
        System.out.println("后置通知！" + name);

        //得到 传参参数
        Object[] args = point.getArgs();
        System.out.println("拿到传参："+args);
//        Long id = (Long)args[0];
        System.out.println("aop获得订单id");

    }


    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LogDTO dto = new LogDTO();
        SysLog sysLog = method.getAnnotation(SysLog.class);
        if (Objects.nonNull(sysLog)) {
            String content = sysLog.value();
            // 注解上的描述,操作日志内容
            dto.setLogType(sysLog.logType().getType());
            dto.setLogContent(content);
        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        dto.setMethod(className + "." + methodName + "()");

        // 设置操作类型
        dto.setOperateType(SysLogOperateTypeEnum.getOperateType(sysLog.logType(), methodName));
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 请求方法
        String requestMethod = request.getMethod();
        dto.setRequestType(requestMethod);
        // 请求的参数
        dto.setRequestParam(getRequestParams(requestMethod, joinPoint));
        // 设置IP地址
        dto.setIp(HttpUtils.getIpAddr(request));
        // 请求地址
        dto.setRequestUrl(request.getRequestURI());
        String uername = CurrentUserUtil.getUsername();
        if (Objects.nonNull(uername)) {
            dto.setCreateBy(uername);
            dto.setUsername(uername);
        }
        // 耗时
        dto.setCostTime(time);
        dto.setCreateTime(new Date());
        // 保存系统日志
        try {
            baseCommonService.addLog(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取请求参数
     *
     * @param requestMethod 请求方法
     * @param joinPoint     切点
     * @return 请求参数
     */
    private String getRequestParams(String requestMethod, JoinPoint joinPoint) {
        StringBuilder params = new StringBuilder();
        if (RequestMethod.POST.name().equals(requestMethod) || RequestMethod.PUT.name().equals(requestMethod) || RequestMethod.PATCH.name().equals(requestMethod)) {
            Object[] paramsArray = joinPoint.getArgs();

            Object[] arguments = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof BindingResult || paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            // update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            PropertyFilter profilter = new PropertyFilter() {
                @Override
                public boolean apply(Object o, String name, Object value) {
                    return value == null || value.toString().length() <= 500;
                }
            };
            params = new StringBuilder(JSONObject.toJSONString(arguments, profilter));
            // update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
                }
            }
        }
        return params.toString();
    }

}
