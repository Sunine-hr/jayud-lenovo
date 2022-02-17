package com.jayud.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.jayud.auth.model.entity.SysUser;
import com.jayud.auth.service.SysUserService;
import com.jayud.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author ciro
 * @since 2022-02-15
 */
@Slf4j
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    private final TokenEndpoint tokenEndpoint;

    public SysUserController(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    @GetMapping("/getMsg")
    public String isSuccess(){
        SysUser sysUser = userService.getUser();
        log.info("sysUser:{}",sysUser);
        return "is_success";
    }

    @GetMapping(value = "/current")
    public Principal getSysUser(Principal principal) {
        Object userDetail = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail == null){
            return null;
        }
        return principal;
    }

    /**
     * @description 退出登录
     * @author  ciro
     * @date   2022/2/16 15:32
     * @param: token
     * @return: java.lang.String
     **/
    @GetMapping("logout")
    public String logout(@RequestParam("token")String token){
        if (consumerTokenServices.revokeToken(token)) {
            return "logout success";
        }
        return "logout error";
    }

    @GetMapping("/token")
    public BaseResult<?> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return returnMsg(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }
    @PostMapping("/token")
    public BaseResult<?> postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return returnMsg(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    /**
     * 自定义返回格式
     *
     * @param accessToken 　Token
     * @return Result
     */
    private BaseResult returnMsg(OAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new LinkedHashMap<>(token.getAdditionalInformation());
        data.put("access_token", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refresh_token", token.getRefreshToken().getValue());
        }
        data.put("token_type",token.getTokenType());
        data.put("expires_in",token.getExpiration());
        data.put("scope",token.getScope());
        String userData = "{\"id\":1,\"name\":\"Admin\",\"password\":\"E10ADC3949BA59ABBE56E057F20F883E\",\"auditStatusDesc\":null,\"userName\":\"Admin\",\"enUserName\":\"\",\"phone\":\"15131231212\",\"departmentId\":1,\"workId\":null,\"workName\":\"运维岗位\",\"roleId\":null,\"roleName\":null,\"companyId\":1,\"companyName\":null,\"superiorId\":98,\"superiorName\":null,\"note\":null,\"loginTime\":null,\"status\":1,\"createdTime\":\"2020-09-10 15:27:25.0\",\"createdUser\":\"admin\",\"token\":\"b65a00ec-e9f4-4d6b-b9ea-02c7828f3e00\",\"isError\":null,\"legalEntityIds\":[],\"legalEntityIdStr\":\"\",\"legalEntities\":null,\"isForcedPasswordChange\":true,\"updatePassWordDate\":\"2021-05-07 09:29:28\"}";
        JSONObject jsonObject = JSONObject.parseObject(userData);
        data.put("userData",jsonObject);
        return BaseResult.ok(data);
    }


}

