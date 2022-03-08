package com.jayud.crm.utils;

import com.jayud.common.BaseResult;
import com.jayud.crm.feign.AuthClient;
import io.seata.core.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

/**
 * @author ciro
 * @date 2022/3/8 14:37
 * @description:生成编码工具
 */
@Component
public class CodeUtils {

    @Autowired
    private AuthClient authClient;

    /**
     * @description 根据规则获取编码
     * @author  ciro
     * @date   2022/3/8 14:39
     * @param: ruleCode
     * @return: java.lang.String
     **/
    public String getCodeByRule(String ruleCode){
        BaseResult baseResult = authClient.getOrderFeign(ruleCode, new Date());
        if (baseResult.isSuccess()){
            HashMap data = (HashMap)baseResult.getResult();
            return data.get("order").toString();
        }
        return null;
    }
}
