package com.jayud.customs.aspects;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.enums.PushKingdeeEnum;
import com.jayud.customs.model.po.GeneralApiLog;
import com.jayud.customs.model.po.YunbaoguanKingdeePushLog;
import com.jayud.customs.service.IGeneralApiLogService;
import com.jayud.customs.service.IYunbaoguanKingdeePushLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 云报关到金蝶推送日志
     */
    @Autowired
    IYunbaoguanKingdeePushLogService yunbaoguanKingdeePushLogService;

    @Autowired
    IGeneralApiLogService logService;

    @Pointcut("@annotation(com.jayud.customs.annotations.APILog)")
    public void catchLog() {
    }

    @Around("catchLog()")
    public Object doAround(ProceedingJoinPoint process) throws Throwable {

        //获取当前请求的路径
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取请求方法名
        String methodName = process.getSignature().getName();
        String moduleName = process.getTarget().getClass().getName();
        //获取请求入参
        Object[] params = process.getArgs();
        //获取签名
        Signature signature = process.getSignature();
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        //获取被@RequestBody标记的入参，转为json
        Object parameter = getParameter(methodSignature.getMethod(), params);
        String requestParameterString = JSONUtil.toJsonStr(parameter);

        /**IYunbaoguanKingdeePushLogService**/
        LocalDateTime now = LocalDateTime.now();//当前时间
        Map param = JSONObject.parseObject(requestParameterString, Map.class);
        String apply_no = param.get("apply_no").toString();//报关单号18位，主键
        YunbaoguanKingdeePushLog yunbaoguanKingdeePushLog = yunbaoguanKingdeePushLogService.getById(apply_no);
        if(yunbaoguanKingdeePushLog == null){
            //新增
            yunbaoguanKingdeePushLog = new YunbaoguanKingdeePushLog();
            yunbaoguanKingdeePushLog.setApplyNo(apply_no);
            yunbaoguanKingdeePushLog.setPushStatusCode(PushKingdeeEnum.STEP1.getCode());
            yunbaoguanKingdeePushLog.setPushStatusMsg(PushKingdeeEnum.STEP1.getMsg());
            yunbaoguanKingdeePushLog.setIpAddress(request.getRemoteAddr());
            yunbaoguanKingdeePushLog.setUserId(1);
            yunbaoguanKingdeePushLog.setCreateTime(now);
            yunbaoguanKingdeePushLog.setUpdateTime(now);
        }else{
            //修改
            yunbaoguanKingdeePushLog.setPushStatusCode(PushKingdeeEnum.STEP1.getCode());
            yunbaoguanKingdeePushLog.setPushStatusMsg(PushKingdeeEnum.STEP1.getMsg());
            yunbaoguanKingdeePushLog.setIpAddress(request.getRemoteAddr());
            yunbaoguanKingdeePushLog.setUserId(1);
            yunbaoguanKingdeePushLog.setUpdateTime(now);
        }
        yunbaoguanKingdeePushLogService.saveOrUpdate(yunbaoguanKingdeePushLog);

        /**IGeneralApiLogService**/
        Long startTime = System.currentTimeMillis();
        Object Result = process.proceed();//执行方法，拿到执行后的结果
        String resultParameterString = JSONUtil.toJsonStr(Result);
        Long endTime = System.currentTimeMillis();
        Integer timeSpan = (int) (endTime - startTime);//处理时间

        GeneralApiLog apiLog = new GeneralApiLog();
        apiLog.setMethod(methodName);
        apiLog.setModuleName(moduleName);
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
        logService.save(apiLog);
        return Result;
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
