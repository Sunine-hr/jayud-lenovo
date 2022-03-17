package com.jayud.common.aop;

import com.jayud.common.aop.annotations.SysWarehousePermission;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.fegin.WmsClient;
import com.jayud.common.utils.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ciro
 * @date 2021/12/22 13:43
 * @description:    仓库权限，切面处理类
 */
@Slf4j
@Aspect
@Component
public class SysWarehousePermissionAspect {


    @Resource
    private WmsClient wmsClient;


    @Pointcut("@annotation(com.jayud.common.aop.annotations.SysWarehousePermission)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 请求参数
        Object[] args = point.getArgs();
        dealDataParam(point,args);
        return point.proceed(args);
    }

    private void dealDataParam(ProceedingJoinPoint joinPoint,Object[] args) throws IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysWarehousePermission sysWarehousePermission = method.getAnnotation(SysWarehousePermission.class);
        if (Objects.nonNull(sysWarehousePermission)) {
            AuthUserDetail userDetail = CurrentUserUtil.getUserDetail();
            for (Object param : args) {
                List<String> warehouseIdList = wmsClient.getWarehouseIdByUserId(String.valueOf(userDetail.getId()));
                setField(param, sysWarehousePermission.warehouseIdListField(), warehouseIdList);
            }
        }
    }


    /**
     * @description 设置字段
     * @author  ciro
     * @date   2021/12/8 14:31
     * @param: param
     * @param: propertyName
     * @param: propertyValue
     * @return: void
     **/
    private void setField(Object param, String propertyName, Object propertyValue) throws IllegalAccessException {
        if (Objects.nonNull(param)) {
            Field field = ReflectionUtils.findField(param.getClass(), propertyName);
            if (Objects.nonNull(field)) {
                field.setAccessible(true);
                field.set(param, propertyValue);
            }
        }
    }


}
