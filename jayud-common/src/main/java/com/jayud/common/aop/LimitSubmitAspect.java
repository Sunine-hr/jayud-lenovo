package com.jayud.common.aop;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.aop.annotations.RepeatSubmitLimit;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ReqDedupHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LimitSubmitAspect {
//    LFUCache<Object, Object> LFUCACHE = CacheUtil.newLFUCache(100, 60 * 1000);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.jayud.common.aop.annotations.RepeatSubmitLimit)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object handleSubmit(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        JSONArray jsonArray = new JSONArray(joinPoint.getArgs());
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("obj", jsonArray);
        Long loginUserId = null;
        if (jsonArray.get(0) instanceof JSONObject) {
            loginUserId = jsonArray.getJSONObject(0).getLong("loginUserId");
        } else {
            loginUserId = jsonArray.getJSONArray(0).getJSONObject(0).getLong("loginUserId");
        }
        String dedupMD5 = new ReqDedupHelper().dedupParamMD5(jsonObject.toString());//计算请求参数摘要，其中剔除里面请求时间的干扰
        String KEY = "dedup:U=" + loginUserId.toString() + "M=" + request.getRequestURI() + "P=" + dedupMD5;

        //获取注解信息
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RepeatSubmitLimit repeatSubmitLimit = method.getAnnotation(RepeatSubmitLimit.class);
        // 1000毫秒过期，1000ms内的重复请求会认为重复
        long expireTime = repeatSubmitLimit.expireTime();
        long expireAt = System.currentTimeMillis() + expireTime;
        String val = "expireAt@" + expireAt;

        // NOTE:直接SETNX不支持带过期时间，所以设置+过期不是原子操作，极端情况下可能设置了就不过期了，后面相同请求可能会误以为需要去重，所以这里使用底层API，保证SETNX+过期时间是原子操作
        Boolean firstSet = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(KEY.getBytes(), val.getBytes(), Expiration.milliseconds(expireTime),
                RedisStringCommands.SetOption.SET_IF_ABSENT));

//        final boolean isConsiderDup;
        if (firstSet != null && firstSet) {
//            isConsiderDup = false;
        } else {
            throw new JayudBizException("请勿重复访问！");
        }

        //获取返回参数
        Object result = joinPoint.proceed();
        return result;
    }


    public static void main(String[] args) {
//        String str1="{'name':'张三','age':20,'obj':[{'test1':3},{'test1':4}]}";
//        String str2="{'name':'张三','age':20,'obj':[{'test1':2}]}";
        String str1 = "{'obj':[{'name':'张三','age':20,'obj':[{'test1':2}]}]}";
        String str2 = "{'obj':[{'name':'张三','age':20,'obj':[{'test1':2}]}]}";
        String dedupMD5 = new ReqDedupHelper().dedupParamMD5(str1);
        String dedupMD52 = new ReqDedupHelper().dedupParamMD5(str2);
        System.out.println("req1MD5 = " + dedupMD5 + " , req2MD5=" + dedupMD52);
    }
}