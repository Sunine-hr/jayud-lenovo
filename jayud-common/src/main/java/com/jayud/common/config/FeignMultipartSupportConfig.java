//package com.jayud.common.config;
//
//import feign.codec.Encoder;
//import feign.form.spring.SpringFormEncoder;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.cloud.openfeign.support.SpringEncoder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
///**
// * @author Songxudong
// * @description feign-SpringMVC 多文件上传配置
// * @date 2019/11/14 1:52 下午
// */
//public class FeignMultipartSupportConfig {
//
//    @Autowired
//    private ObjectFactory<HttpMessageConverters> messageConverters;
//
//    @Bean
//    public Encoder feignEncoder() {
//        return new SpringFormEncoder(new SpringEncoder(messageConverters));
//    }
//
////    @Bean
////    public Encoder multipartFormEncoder() {
////        return new SpringFormEncoder(new SpringEncoder(new ObjectFactory<HttpMessageConverters>() {
////            @Override
////            public HttpMessageConverters getObject() throws BeansException {
////                return new HttpMessageConverters(new RestTemplate().getMessageConverters());
////            }
////        }));
////    }
//
//}
