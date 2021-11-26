package com.jayud.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.entity.AuditInfoForm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RSAEncrypt {

    private static final Logger logger = Logger.getLogger(String.valueOf(RSAEncrypt.class));

    public static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJPCYLmW+xH/c2bdL0F9IAy877QJB5o/ATFsXBDn7GY0A/XVHxFGoQeJ3c9XLr3y7ocxf0yCfDbwvZYsZsXA95sCAwEAAQ==";
    public static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAk8JguZb7Ef9zZt0vQX0gDLzvtAkHmj8BMWxcEOfsZjQD9dUfEUahB4ndz1cuvfLuhzF/TIJ8NvC9lixmxcD3mwIDAQABAkBefEnF49ohJCzzdjUlt5GrE5ZklfGanPdyV0x4MxZkM5bYHBtlpfjhLvk99F+2I6KDk01Pt9l29193UvQRZD1hAiEA97Pk2yExWeoY8TWIQu/jtra9HvolJAt7Ah/D3cs6y6UCIQCYtXJbh+LaoY8oxvpOevx5COMbnjO/7QAQvy8Ro3ZSPwIhAL3XTer0AZLb68nSMWC74lZCr6dyJ8z8T4ZNOhxeie1RAiBzaIWRiSO36Wtg/OfEkgvmiQHiKHChtsJa5bHz2Z6dTwIgP9O/wki9hKaxTSaXoe1IpXSgDQ5XcmSr2Z/+suMudIk=";



    public static void main(String[] args) throws Exception {
        //得到了密钥对
//        Map<Integer, String> map = RSAEncrypt.genKeyPair();
//
//        System.out.println(map.get(0));
//        System.out.println(map.get(1));

        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setAuditComment("1111");
        auditInfoForm.setAuditUser("2222");



        System.out.println("加密前:"+auditInfoForm);
        String s = JSONObject.toJSONString(auditInfoForm);
        System.out.println("对象转换成字符串:"+s);
        String s3 = RSAEncrypt.encrypt(s, RSAEncrypt.PUBLIC_KEY);
        System.out.println("公钥加密后："+s3);


        String decrypt = RSAEncrypt.decrypt(s3, RSAEncrypt.PRIVATE_KEY);
        AuditInfoForm auditInfoForm1 = JSONObject.parseObject(decrypt, AuditInfoForm.class);
        System.out.println("解密后的数据："+auditInfoForm1);

    }

    /**
     * 随机生成密钥对
     */
    public static Map<Integer, String> genKeyPair() {

        Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(512,new SecureRandom());

            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥

            // 得到公钥字符串
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            logger.info("得到公钥："+publicKeyString);
            logger.info("得到私钥："+privateKeyString);

            // 将公钥和私钥保存到Map
            keyMap.put(0,publicKeyString);  //0表示公钥
            keyMap.put(1,privateKeyString);  //1表示私钥

        } catch (Exception e) {
            logger.info("生成公钥私钥异常："+e.getMessage());
            return null;
        }

        return keyMap;
    }
    /**
     * RSA公钥加密
     * @param str  需要加密的字符串
     * @param publicKey  公钥
     * @return 公钥加密后的内容
     */
    public static String encrypt( String str, String publicKey ){
        String outStr=null;
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            logger.info("使用公钥加密【"+str+"】异常："+e.getMessage());
        }
        return outStr;
    }

    /**
     * RSA私钥解密
     * @param str  加密字符串
     * @param privateKey  私钥
     * @return 私钥解密后的内容
     */
    public static String decrypt(String str, String privateKey){
        String outStr=null;
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            logger.info("使用私钥解密异常："+e.getMessage());
        }
        return outStr;
    }

}
