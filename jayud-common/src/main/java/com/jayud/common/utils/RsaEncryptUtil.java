package com.jayud.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;

/**
 * @author william
 * @description
 * @Date: 2020-09-14 15:27
 */
public  class RsaEncryptUtil {
    /**
     * C#中通过FromXmlString属性加载的是XML形式,而JAVA中用到的是解析后的PEM格式的字符串,总之读取证书中信息无非是转换方式问题
     */
//    public static void main(String[] args) {
//        CryptPair info = new CryptPair();
//        info.setPassword("123456");
//        info.setTimeStamp(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
//        String infoStr = JSONUtil.toJsonStr(info);
//
//        try {
//            System.out.println(encrypt(infoStr));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取加密后的字符串（password字符串）
     * @param password 被加密的字符串
     * @param vivoPublicKey 用于加密的公钥
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static String getEncryptedPassword( String password, String vivoPublicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        CryptPair info = new CryptPair();
        info.setPassword(password);
        info.setTimeStamp(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        String infoStr = JSONUtil.toJsonStr(info);
        return encrypt(infoStr,vivoPublicKey);
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        String encryptedPassword = getEncryptedPassword("123456", "<RSAKeyValue><Modulus>4xGrWUFEftqNS4DvSOW9wfeYK1i9gJBYB+tmhz6E9QCo6cvRdmT+IaG6lE6U7sh1dP3atLc8lzvljDmxoDu/SBMLkDXiXJgj7AJWbhDUn1xCbQb/1jEb08jEqr/3K4+03bCX06W0V5HrjL53GiQ0dZHIO1eFdAvRDPYfvIwIWq0=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>");
        System.out.println(encryptedPassword);
    }

    /**
     * 实际加密算法
     * @param info 封装的password+时间密钥对
     * @param vivoPublicKey 加密用的公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static String encrypt(String info,String vivoPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        //由于C#编码问题，读取配置文件中的C#公钥要对xml进行读取，此处直接正则抓取了
        String xmlModulus = ReUtil.get("(?<=\\<Modulus\\>)\\S+(?=\\<\\/Modulus\\>)", vivoPublicKey, 0);
        String xmlExponentString = ReUtil.get("(?<=\\<Exponent\\>)\\S+(?=\\<\\/Exponent\\>)", vivoPublicKey, 0);

        //将C#的编码方式换成JAVA的编码方式
        byte[] moduleBytes = Base64.decode(xmlModulus);
        byte[] exponentBytes = Base64.decode(xmlExponentString);
        BigInteger modulus = new BigInteger(1, moduleBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        //获取java使用的RSA公钥
        RSAPublicKeySpec rsaPublicKey = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(rsaPublicKey);

        //使用RSA方式加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //获取密文字符串的输入流
        int bufferSize = (1024 / 8) - 11;
        byte[] buffer = new byte[bufferSize];
        InputStream inputStream = new ByteArrayInputStream(info.getBytes(Charset.forName("UTF-8")));
        //开始加密
        while (true) {
            int readSize = inputStream.read(buffer, 0, bufferSize);
            if (readSize <= 0) {
                break;
            }
            byte[] temp = Arrays.copyOf(buffer, readSize);
            byte[] bytes = cipher.doFinal(temp);
            outputStream.write(bytes, 0, bytes.length);
        }
        String result = new String(Base64.encode(outputStream.toByteArray()));
        return result;
    }

    /**
     * 利用私钥对传过来的信息进行解密并返回解密后的字节数组
     * @param encrypted
     * @return
     */
    private static byte[] Dencrypt(byte[] encrypted) {
        try {
            //解密用的私钥
            String delement = "";
            String modulus = "";
            byte[] expBytes = Base64.decode(delement);
            byte[] modBytes = Base64.decode(modulus);

            BigInteger modules = new BigInteger(1, modBytes);
            BigInteger exponent = new BigInteger(1, expBytes);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA");

            RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, exponent);
            PrivateKey privKey = factory.generatePrivate(privSpec);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}


@Data
class CryptPair {
    private String TimeStamp;
    private String Password;
}
