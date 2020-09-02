package com.jayud.feign;


/**
 * @author larry
 * @version 1.0.0
 * @ClassName WorkClient.java
 * @Description TODO
 * @createTime 2020年01月16日 11:09:00
@FeignClient(value = "rtscf-platform-work", path = "${business.web.work.content.path:}", fallback = com.scmrt.client.WorkClient.WorkClientHystrix.class)
public interface WorkClient {
    *//**
     * 保存用户信息
     *//*
    @PostMapping("/msgInfo/save")
    ApiResult saveMsg(@RequestBody MsgInfoForm form);



}*/
