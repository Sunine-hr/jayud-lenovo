package com.jayud.mall.utils;

import com.jayud.mall.service.INumberGeneratedService;
import org.springframework.beans.factory.annotation.Autowired;

public class NumberGeneratedUtils {

    @Autowired
    INumberGeneratedService numberGeneratedService;

    /**
     * 根据`单号编号code`，获取生成的`单号`
     * @param code
     * @return
     */
    public String getOrderNoByCode(String code){
        return numberGeneratedService.getOrderNoByCode(code);
    }


}
