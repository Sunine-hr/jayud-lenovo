package com.jayud.oms.security.entity;

import lombok.Data;

import java.util.Map;

/**
 * 权限凭证
 */
@Data
public class Certificate {

    //凭证id
    private String id;
    //用户凭证
    private String certificate;
    //参数（用作参数传递）
    private Map<String,Object> param;


}
