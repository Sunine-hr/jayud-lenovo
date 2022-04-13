package com.jayud.auth.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayud.auth.model.bo.LoginUserForm;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.service.ISysRoleService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.BaseResult;
import com.jayud.common.aop.annotations.SysLog;
import com.jayud.common.constant.SysTips;
import com.jayud.common.enums.SysLogTypeEnum;
import com.jayud.common.service.BaseCommonService;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ciro
 * @date 2022/2/21 15:02
 * @description: 用户登录控制
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class UserDetailsController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private BaseCommonService baseCommonService;

    private final TokenEndpoint tokenEndpoint;

    public UserDetailsController(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }


    @GetMapping(value = "/current")
    public Principal getSysUser(Principal principal) {
        Object userDetail = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail == null) {
            return null;
        }
        return principal;
    }

    /**
     * @description 退出登录
     * @author ciro
     * @date 2022/2/16 15:32
     * @param: token
     * @return: java.lang.String
     **/
    @GetMapping("logout")
    public BaseResult<?> logout() {
        long beginTime = System.currentTimeMillis();
        if (consumerTokenServices.revokeToken(CurrentUserUtil.getUserToken())) {
            // 方法全名称
            String methodName = this.getClass().getName() + ".logout()";
            // 执行时长(毫秒)
            long costTime = System.currentTimeMillis() - beginTime;
            // 日志内容
            String username = CurrentUserUtil.getUsername();
            String logContent = "用户名: " + username + "，退出登录成功！";

            //拿到用户真实名称
            LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysUser::getName, username);
            SysUser one = sysUserService.getOne(lambdaQueryWrapper);
            baseCommonService.addLog(logContent, SysLogTypeEnum.LOGIN, methodName, "", costTime, username, one.getUserName());
            return BaseResult.ok(SysTips.OUT_LOGIN_SUCCESS);
        }
        return BaseResult.ok(SysTips.OUT_LOGIN_ERROR);
    }


    @PostMapping("/token")
    public BaseResult<?> postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        long beginTime = System.currentTimeMillis();
        BaseResult checkResult = sysUserService.checkUserStatus(parameters.get("tenantCode"), parameters.get("username"), parameters.get("password"));
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        BaseResult baseResult = returnMsg(tokenEndpoint.postAccessToken(principal, parameters).getBody(), parameters.get("username"));

        Object userData = JSONUtil.parseObj(baseResult.getResult()).get("userData");

        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(userData);

        //拿用户真是姓名
        String trueName = jsonObject.getStr("userName");
        // 方法全名称
        String methodName = this.getClass().getName() + ".postAccessToken()";
        // 执行时长(毫秒)
        long costTime = System.currentTimeMillis() - beginTime;
        // 日志内容
        String logContent = "用户名: " + parameters.get("username") + "，登录成功！";
        String result = JSONUtils.toJSONString(parameters);
        baseCommonService.addLog(logContent, SysLogTypeEnum.LOGIN, methodName, result, costTime, parameters.get("username"), trueName);
        return baseResult;
    }

    /**
     * 自定义返回格式
     *
     * @param accessToken 　Token
     * @return Result
     */
    private BaseResult returnMsg(OAuth2AccessToken accessToken, String username) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new LinkedHashMap<>(token.getAdditionalInformation());
        data.put("access_token", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refresh_token", token.getRefreshToken().getValue());
        }
        data.put("token_type", token.getTokenType());
        data.put("expires_in", token.getExpiration());
        data.put("scope", token.getScope());
        //获取用户信息
        SysUser sysUser = sysUserService.getUserByUserName(null, username);
        LoginUserForm loginUserForm = new LoginUserForm();
        BeanUtils.copyProperties(sysUser, loginUserForm);
        //获取用户权限
        List<SysRole> roleList = sysRoleService.selectRoleByUsername(username);
        List<String> roleCodeList = roleList.stream().map(x -> x.getRoleCode()).collect(Collectors.toList());
        loginUserForm.setRoleCodeList(roleCodeList);

        String userData = "{\"id\":1,\"name\":\"Admin\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"auditStatusDesc\":null,\"userName\":\"Admin\",\"enUserName\":\"\",\"phone\":\"15131231212\",\"departmentId\":1,\"workId\":null,\"workName\":\"运维岗位\",\"roleId\":null,\"roleName\":null,\"companyId\":1,\"companyName\":null,\"superiorId\":98,\"superiorName\":null,\"note\":null,\"loginTime\":null,\"status\":1,\"createdTime\":\"2020-09-10 15:27:25.0\",\"createdUser\":\"admin\",\"token\":\"b65a00ec-e9f4-4d6b-b9ea-02c7828f3e00\",\"isError\":null,\"legalEntityIds\":[],\"legalEntityIdStr\":\"\",\"legalEntities\":null,\"isForcedPasswordChange\":true,\"updatePassWordDate\":\"2021-05-07 09:29:28\"}";
        JSONObject jsonObject = JSONObject.parseObject(userData);
        data.put("userData", loginUserForm);
        return BaseResult.ok(data);
    }


    //验证码
    public BaseResult verificationCode(HttpSession session, @RequestParam String checkCode) {

        // 获得验证码对象
        Object cko = session.getAttribute("simpleCaptcha");
        if (cko == null) {
            return BaseResult.ok("请输入验证码！");
        }
        String captcha = cko.toString();
        // 判断验证码输入是否正确
        //验证码创建时间
        String codeTime1 = session.getAttribute("codeTime") + "";
        System.out.println("时间戳：" + codeTime1);
        Long aLong = Long.valueOf(codeTime1);
        //当前时间
        long l = System.currentTimeMillis();
        System.out.println("当前时间戳：" + l);
        long l1 = l - aLong;
        // 验证码有效时长为1分钟
        if (l1 / 1000 / 60 > 0.2) {
            System.out.println("进入到方法！超时时间");
            return BaseResult.error("验证码已失效，请重新输入！");

        } else {
            if (StringUtils.isEmpty(checkCode) || captcha == null || !(checkCode.equalsIgnoreCase(captcha))) {
                System.out.println("进入到方法！验证码是否错误！");
                return BaseResult.error("验证码错误，请重新输入！");

            } else {
                // 在这里可以处理自己需要的事务，比如验证登陆等

                return BaseResult.ok("验证通过！");
            }
        }
    }
}
