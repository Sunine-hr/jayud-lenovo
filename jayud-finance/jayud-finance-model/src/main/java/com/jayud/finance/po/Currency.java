package com.jayud.finance.po;

import com.jayud.finance.enums.FormIDEnum;
import lombok.Data;

/**
 * 查询币别（将币别通用代码转为金蝶代码）使用
 *
 * @author william
 * @description
 * @Date: 2020-04-28 14:11
 */
@Data
@K3Entity(value = { "FNumber", "FName","FCODE"}, formId = FormIDEnum.CURRENCY,fName = "FCODE")
public class Currency {
    private String FNumber;
    private String FName;
    private String FCODE;
}
