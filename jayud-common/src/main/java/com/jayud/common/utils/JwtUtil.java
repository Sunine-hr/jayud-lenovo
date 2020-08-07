package com.jayud.common.utils;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @date 2019/10/24
 */
public class JwtUtil {

    //密钥 -- 根据实际项目，这里可以做成配置
    public static final String KEY = "022bdc63c3c5a45879ee6581508b9d03adfec4a4658c0ab3d722e50c91a351c42c231cf43bb8f86998202b01ec52239a74fc0c9a9aeccce604743367c9646b";

    public static final String ADMIN_KEY ="022bdc63c3c5a45879ee6581508b9d03adfec4a4658c0ab3d722e50csfg91a351c42c231cf43bb8f86998202b01ec52239a74fc0c9a9aeccce604743367c9646b";

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey(String value) {
        byte[] encodedKey = Base64.decodeBase64(value);
        SecretKeySpec key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        return key;
    }

    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法  私匙使用用户密码
     *
     * @param ttlMillis jwt过期时间
     * @param userId    登录成功的user对象
     * @param userNum   用户编号
     * @return
     */
    public static String createJWT(long ttlMillis, Long userId, String userNum) {
        return createTokenJWt(ttlMillis,userId,userNum,KEY);
    }

    public static String createAdminJWT(long ttlMillis,Long userId,String userNum) {
        return createTokenJWt(ttlMillis,userId,userNum,ADMIN_KEY);
    }

    public static String createTokenJWt(long ttlMillis, Long userId, String userNum,String keyValue){
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>(16);
        claims.put("userId", userId);
        claims.put("userNum", userNum);

        //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        //生成签发人
        String subject = "userService";
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, generalKey(keyValue));
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * Token的解密  为了兼容金融平台用户，未修改方法参数
     *
     * @param token 加密后的token
     * @return
     */
    public static Claims parseJWT(String token) {
        return parse(token, KEY);
    }

    /**
     * Admin Token 解密
     * @param token
     * @return
     */
    public static Claims parseAdminJWT(String token){
        return parse(token,ADMIN_KEY);
    }

    /**
     * 真正解析
     * @param token
     * @param keyValue
     * @return
     */
    private static Claims parse(String token ,String keyValue){
        SecretKey key = generalKey(keyValue);
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }


    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     *
     * @param token
     * @return
     */
    public static Boolean isVerify(String token) {
        try {
            Claims claims = parseJWT(token);
            claims.getExpiration();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String token = createAdminJWT(3600000L, 1L, "");
        try {
            System.out.println(token);
            System.out.println(parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTnVtIjoiYWRtaW4iLCJzdWIiOiJ1c2VyU2VydmljZSIsImV4cCI6MTU3MjkzODE3MSwidXNlcklkIjoyLCJpYXQiOjE1NzI5MzgxMzUsImp0aSI6ImZhMDBkZTkwLTE0ZTMtNDA1Yy04NWY1LWNjZGFhMGY5ZDE3ZiJ9.rPF3haH1pfeHRTmQBTw9udSzIjIkHobto3V0Y9M5Ums"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}