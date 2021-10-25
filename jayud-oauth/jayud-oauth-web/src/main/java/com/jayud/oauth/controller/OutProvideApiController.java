package com.jayud.oauth.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.TokenGenerator;
import com.jayud.oauth.mapper.SystemUserMapper;
import com.jayud.oauth.model.bo.BizData;
import com.jayud.oauth.model.bo.ExtAuthenticationForm;
import com.jayud.oauth.model.bo.OutAuthenticationForm;
import com.jayud.oauth.model.po.DevelopmentSetting;
import com.jayud.oauth.model.po.SystemUser;
import com.jayud.oauth.model.vo.SystemUserVO;
import com.jayud.oauth.model.vo.UserLoginToken;
import com.jayud.oauth.service.IDevelopmentSettingService;
import com.jayud.oauth.service.ISystemUserService;
import com.jayud.oauth.service.impl.SystemUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@Api(tags = "第三方API鉴权接口")
@Slf4j
public class OutProvideApiController {

    @Autowired
    ISystemUserService systemUserService;

    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "对外授权", notes = "sign签名方法: md5(账号+时间轴转json+密码)")
    @RequestMapping("/api/outOauth")
    public CommonResult outOauth(@RequestBody OutAuthenticationForm form) {

        UserLoginToken token = new UserLoginToken();
        token.setPassword(form.getPassword().toCharArray());
        token.setUsername(form.getLoginname());

        String sign = form.getSign();//密文
        String loginname = form.getLoginname();
        String oprTime = form.getBizData().getOprTime();
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(loginname) || StringUtils.isBlank(form.getPassword()) ||
                form.getBizData() == null || "".equals(form.getBizData()) ||
                StringUtils.isBlank(oprTime) || oprTime.length() != 14) {
            return CommonResult.error(10001, "授权请求参数不合法");
        }

        BizData bizData = new BizData();
        bizData.setOprTime(oprTime);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex((loginname + jsonStr + form.getPassword()).getBytes());
        if (!encodeValue.equals(sign)) {
            return CommonResult.error(10002, "授权不通过");
        }

        // 获取Subject实例对象，用户实例
        Subject subject = SecurityUtils.getSubject();

        SystemUserVO cacheUser = null;
        // 认证
        // 传到 MyShiroRealm 类中的方法进行认证
        try {
            subject.login(token);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            cacheUser = new SystemUserVO();
            cacheUser.setIsError(true);
            return CommonResult.error(1, "授权不通过", cacheUser);
        }
        // 构建缓存用户信息返回给前端
        SystemUser user = (SystemUser) subject.getPrincipals().getPrimaryPrincipal();
        if (user.getStatus() == 0) {
            return CommonResult.error(400, "该用户已被禁用");
        }
        //响应前端数据
        cacheUser = ConvertUtil.convert(user, SystemUserVO.class);
        //缓存用户ID larry 2020年8月13日11:21:11
        String uid = redisUtils.get(user.getId().toString());
        if (uid == null) {
            redisUtils.set(user.getId().toString(), subject.getSession().getId().toString());
        }
        cacheUser.setToken(subject.getSession().getId().toString());
        cacheUser.setId(null);
        cacheUser.setPassword(null);
        log.warn("CacheUser is " + JSONUtil.toJsonStr(cacheUser));
        //保存登录记录
        systemUserService.insertLoginLog(user);
        redisUtils.set(cacheUser.getToken(), user.getName());

        return CommonResult.success(cacheUser);
    }

    public static void main(String[] args) {

        BizData bizData = new BizData();
        String date = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        bizData.setOprTime(date);
        System.out.println(date);
        String jsonStr = JSONObject.toJSONString(bizData);
        String encodeValue = DigestUtils.md5DigestAsHex(("蔡永春" + jsonStr + "123456").getBytes());
        System.out.println(encodeValue);
        String token = TokenGenerator.generateValue(encodeValue);
        System.out.println(token);
    }


}









    



