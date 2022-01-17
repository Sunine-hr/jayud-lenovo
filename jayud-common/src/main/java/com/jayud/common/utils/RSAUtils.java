package com.jayud.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.entity.AuditInfoForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;
import cn.hutool.json.JSONUtil;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSAUtils {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";

    public static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL7q_PqXeAdSxbk2DcujrQdwQbRI0HqpowVDo-ZiNEI2eIh_odjTkQN4skvvXDfbzcKUEwclxl8XM0dQxDMTT5UCAwEAAQ";
    public static final String PRIVATE_KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAvur8-pd4B1LFuTYNy6OtB3BBtEjQeqmjBUOj5mI0QjZ4iH-h2NORA3iyS-9cN9vNwpQTByXGXxczR1DEMxNPlQIDAQABAkBNRhOK4g_k9u2sn2JKED-SiS8u52HqpAqlsNEACLOaMHrq_XwSzxCQ82DGnLzbhF8PRt5lgvcDWPscW1D7md8BAiEA4f_r9zS1oa4yq6yTeOZ9WtNtM1V9CwrGYU20C_LmynUCIQDYQuS0YrsOxo5y7NJjDghHs5ZObMH-JjQnEFh5kZ6MoQIgEwLdTbFQaiZmOszMpwn0l5Rbhkr1tt0ULRFFSRAI3BUCID48-i8D3wvAjwx6JY5GYpGoGJDXcfkfq5C2fo_2HaghAiBZNarD68AqGWCqVU05Z4VEummlchrRdVECeRgVi43R5g";


    public static Map<String, String> createKeys(){
    public static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMlB9-D3eJsl7zz7V3v2t07Zhv60YZrbsG5c1fiI7ioqPI_Xa80CNwLyG-QhqocgUOjH1JOyw7EXsPx16XyGsIkCAwEAAQ";
    public static final String PRIVATE_KEY = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAyUH34Pd4myXvPPtXe_a3TtmG_rRhmtuwblzV-IjuKio8j9drzQI3AvIb5CGqhyBQ6MfUk7LDsRew_HXpfIawiQIDAQABAkEAkYuemtf9JZ56dEyESQb0LBbONLb4e0hzQGzs5O8J5f0aY-YOJiWeSlU-_25mwYpXajR_70OsgBxQQEQzDnuzvQIhAPP34eBEJp9PKY7O7iVuEtIyjyYAC0XE9Zibs3zyVye3AiEA0y7ch-X6urhLzFtR-52x2BHg4vjFVgHSJDyAtAd6ab8CIDDRd1djC79xHcW_zpOa1RVOnKpj892ALgzdiysDa0E9AiEArz6D4oIFvkyRGdPuBE6n9hVf-PlXSDfamhda9gn-myECIDlTgdFKb7YBR6dP9xU9df3QDhGAWWo1qBNmE0F0N9FA";
    public static final String APP_ID = "1637925530991";

    public static Map<String, String> createKeys(int keySize){
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(512);                //keySize 可以为1024
//        kpg.initialize(keySize);                //keySize 可以为1024
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
//        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    private Object[] initSecretkey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);// 可以理解为：加密后的密文长度，实际原文要小些 越大 加密解密越慢
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        log.info("初始化密钥，生成公钥私钥对完毕");

        String publicKey = java.util.Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
        String privateKey = java.util.Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        log.debug("---------------------publicKey----------------------");
        log.debug(publicKey);
        log.debug("---------------------privateKey----------------------");
        log.debug(privateKey);


        Object[] keyPairArr = new Object[2];
        keyPairArr[0] = publicKey;
        keyPairArr[1] = privateKey;

        return keyPairArr;
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }



    public static void main(String[] args) throws Exception {

//
//        Map<String, String> keys = RSAUtils.createKeys();
//        String publicKey = keys.get("publicKey");
//        String privateKey = keys.get("privateKey");
//        System.out.println("publicKey"+publicKey);
//        System.out.println("privateKey"+privateKey);

//        Map<String, Object> form = new HashMap<>();
//        form.put("trainStatus", "");
//        form.put("truckNo", "truckNo");
//        form.put("userName", "defaultUsername");



        String  vo="{\"id\":\"\",\"type\":1,\"orderNo\":\"EX2111000008\",\"vehicleType\":\"1\",\"vehicleSize\":\"3T\",\"takeAdrForms1\":[{\"province\":1,\"city\":2,\"area\":null,\"contacts\":\"1\",\"phone\":\"1\",\"address\":\"广东省 深圳市 罗湖区\",\"goodsDesc\":\"一个货物\",\"pieceAmount\":\"1\",\"weight\":0.2,\"volume\":0.1,\"remarks\":\"备注\",\"date\":\"2021-11-22 11:21:43\",\"provinceName\":\"广东省\",\"cityName\":\"深圳市\",\"areaName\":\"罗湖区\"}],\"takeAdrForms2\":[{\"province\":1,\"city\":2,\"area\":null,\"contacts\":\"1\",\"phone\":\"1\",\"address\":\"河北省 石家庄市 长安区\",\"goodsDesc\":\"一个货物\",\"pieceAmount\":\"1\",\"weight\":0.2,\"volume\":0.1,\"remarks\":\"备注\",\"date\":\"2021-11-22 11:21:43\",\"provinceName\":\"河北省\",\"cityName\":\"石家庄市\",\"areaName\":\"长安区\"}]}";

        AuditInfoForm form = new AuditInfoForm();
        form.setAuditComment("ggggg");
        form.setAuditUser("1111");
        form.setAuditStatus("2222");

        System.out.println("加密前:"+form);
        //转化成json字符串
        String s1 = JSONObject.toJSONString(form);
        System.out.println("对象转换成json的字符串去加密:"+s1);

        //  私钥加密
        String sjm = RSAUtils.privateEncrypt(s1, RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
        System.out.println("加密后 :"+sjm);
        System.out.println("----------");

        String  bb="ApSoVcj4RoHCjMZw2ftTpuoha4z4K7a4tw8dl9oBCWhlRAoAV6J4JCXZdxKieHaLa4yqquNrQ9FCJziKKmQRYw1";

        System.out.println("解密部分");
        //公钥解密
        String jmm = RSAUtils.publicDecrypt(sjm, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        System.out.println("解密数据"+jmm);
        // RSA签名
        String sign = sign(s1, getPrivateKey(RSAUtils.PRIVATE_KEY));
        // RSA验签
        boolean result = verify(jmm, getPublicKey(RSAUtils.PUBLIC_KEY), sign);
        System.out.print("验签结果:" + result);



        AuditInfoForm auditInfoForm1 = JSONObject.parseObject(jmm, AuditInfoForm.class);
        System.out.println("解密后的数据："+auditInfoForm1);

    //加密数据
    public static JSONObject getEncryptedData(Object o){
        String s1 = null;
        try {
            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(o), RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
        jsonObject.put("appId",RSAUtils.APP_ID);
        jsonObject.put("data",s1);

        return jsonObject;
    }

    public static void main(String[] args) throws Exception {
        RSAUtils generatorRSAKey = new RSAUtils();
        generatorRSAKey.initSecretkey();
        JSONObject s = new JSONObject();
//        s.put("warehouseName", "佳裕达龙岗仓库");
//        s.put("customerName", "Fingo");
//        s.put("transportTypeName", "云途小包");
//        s.put("transportTrackNo", "11");
//        s.put("status", 80);
//        s.put("customerRefNo", "11");
//        List<StockInOrderDetailForm> list = new ArrayList<>();
//        StockInOrderDetailForm stockInOrderDetailForm = new StockInOrderDetailForm();
//        stockInOrderDetailForm.setSku("SKU-30-2");
//        stockInOrderDetailForm.setProductQty(3);
//        list.add(stockInOrderDetailForm);
//        s.put("items", list);
//        s.put("warehouseName", "佳裕达龙岗仓库");
//        s.put("customerName", "Fingo");
//        s.put("transportTypeName", "云途小包");
//
        s.put("contacts", "李贤斌");
        s.put("countryName", "中国");
        s.put("cityName", "深圳");
        s.put("stateProvinceName", "广州");
        s.put("streetAddress", "罗湖区北京大厦");
        s.put("postCode", "234345");
        s.put("phone", "1234243233");
        s.put("telephone", "");
        s.put("email", "");
        s.put("company", "佳裕达");
        s.put("senderName", "小吴");
        s.put("senderPhone", "133435432343");
        s.put("senderCountryName", "中国");
        s.put("senderPostCode", "234345");
        s.put("senderStateProvinceName", "湖南");
        s.put("senderCityName", "长沙");
        s.put("senderAddress", "雨花区");
        s.put("senderTelephone", "");
        s.put("senderCompany", "");
        s.put("trackingNo", "2344534");
        s.put("remark", "");
        s.put("countyName", "");
        s.put("townName", "");
        s.put("senderEmail", "");
        s.put("origin", 1);
        s.put("weight", new BigDecimal(10));
        s.put("length", new BigDecimal(10));
        s.put("height", new BigDecimal(10));
        s.put("width", new BigDecimal(10));
        s.put("identityCard", "2453534564343543453453");
        s.put("status", 0);
//
//        s.put("customerRefNo", "11");
//        List<StockInOrderDetailForm> list = new ArrayList<>();
//        StockInOrderDetailForm stockInOrderDetailForm = new StockInOrderDetailForm();
//        stockInOrderDetailForm.setSku("SKU-30-2");
//        stockInOrderDetailForm.setProductQty(3);
//        list.add(stockInOrderDetailForm);
//        s.put("items", list);

//        s.put("customerName", "Fingo");
//        s.put("sku", "yy-123");
//        s.put("barcode", "ert3345");
//        s.put("nameCn", "电脑");
//        s.put("nameEn", "department");
//        s.put("productDescribe", "用来办公的好东西");
//        s.put("declaredValue", new BigDecimal(100));
//        s.put("weight", new BigDecimal(10));
//        s.put("length", new BigDecimal(10));
//        s.put("width", new BigDecimal(10));
//        s.put("height", new BigDecimal(10));
//        s.put("hsCode", "");
//        s.put("orgin", "");
//        s.put("brandEn", "");
//        s.put("imageUrl", "");
//        s.put("ccDeclarePrice", new BigDecimal(10));
//        s.put("bcDeclarePrice", new BigDecimal(10));
//        s.put("etkDeclarePrice", new BigDecimal(10));
//        s.put("validityTime", new Date());
//        s.put("ifBattery", "");
//        s.put("specification", "");


        System.out.println(s);
        String s2 = s.toJSONString();
        System.out.println("s2:"+s2);
        String s1 = RSAUtils.publicEncrypt(s2, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        System.out.println("s1:"+s1);
    }



}