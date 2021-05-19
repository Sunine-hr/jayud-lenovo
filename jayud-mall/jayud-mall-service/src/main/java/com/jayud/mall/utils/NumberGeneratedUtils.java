package com.jayud.mall.utils;

import com.jayud.common.utils.SpringContextUtil;
import com.jayud.mall.service.INumberGeneratedService;
import org.springframework.beans.factory.annotation.Autowired;

public class NumberGeneratedUtils {

    /**
     * 工具类不能注入numberGeneratedService，这个被废弃了
     */
    @Autowired
    INumberGeneratedService numberGeneratedService;

    @Override
    public String toString() {
        return "NumberGeneratedUtils{" +
                "numberGeneratedService=" + numberGeneratedService +
                '}';
    }

    /**
     * 根据`单号编号code`，获取生成的`单号`<br>
     * numberGeneratedService,拿不到，为空，被废弃了<br>
     * 替代为 getOrderNoByCode2 or getOrderNoByCode3 <br>
     * @deprecated replaced by <code>getOrderNoByCode2(String code)</code>
     * @see  #getOrderNoByCode2(String)
     * @see  #getOrderNoByCode3(String)
     * @param code
     * @return
     */
    @Deprecated //废弃方法费注解
    public String getOrderNoByCode1(String code){
        String orderNoByCode = numberGeneratedService.getOrderNoByCode(code);
        return orderNoByCode;
    }

    /**
     * 根据`单号编号code`，获取生成的`单号`
     * @param code
     * @return
     */
    public static String getOrderNoByCode2(String code){
        INumberGeneratedService numberGeneratedService2 = SpringContextUtil.getBean(INumberGeneratedService.class);
        String orderNoByCode = numberGeneratedService2.getOrderNoByCode(code);
        return orderNoByCode;
    }

    /**
     * 根据`单号编号code`，获取生成的`单号`
     * @param code
     * @return
     */
    public static String getOrderNoByCode3(String code){
        INumberGeneratedService numberGeneratedService2 = SpringContextUtil2.getBean(INumberGeneratedService.class);
        String orderNoByCode = numberGeneratedService2.getOrderNoByCode(code);
        return orderNoByCode;
    }



}
