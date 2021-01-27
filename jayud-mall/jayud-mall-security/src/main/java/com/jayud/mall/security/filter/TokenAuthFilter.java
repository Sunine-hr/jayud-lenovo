package com.jayud.mall.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.jayud.mall.security.entity.SecurityUser;
import com.jayud.mall.security.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//2 授权的过滤器
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public TokenAuthFilter(AuthenticationManager authenticationManager,TokenManager tokenManager,RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
        //判断如果有权限信息，放到权限上下文中
        if(authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }else{
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        chain.doFilter(request,response);
    }

    /**
     * 获取到Authentication
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        if(token != null) {
            //从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            //从redis获取对应权限列表
//            List<String> permissionValueList = (List<String>)redisTemplate.opsForValue().get(username);
            Object o = redisTemplate.opsForValue().get(token);
            if(o!=null){
                String s = o.toString();
                //List<String> permissionValueList = JSON.parseObject(s, new TypeReference<List<String>>() {});
                //SecurityUser securityUser = JSONObject.parseObject(s, SecurityUser.class);

                //String jsonStr = toJsonString(exp);
                JSONObject obj = JSON.parseObject(s);
                SecurityUser securityUser = TypeUtils.castToJavaBean(obj, SecurityUser.class);


                List<String> permissionValueList = securityUser.getPermissionValueList();
                Collection<GrantedAuthority> authority = new ArrayList<>();
                for(String permissionValue : permissionValueList) {
                    SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permissionValue);
                    authority.add(auth);
                }
                return new UsernamePasswordAuthenticationToken(username,token,authority);
            }
        }
        return null;
    }

}
