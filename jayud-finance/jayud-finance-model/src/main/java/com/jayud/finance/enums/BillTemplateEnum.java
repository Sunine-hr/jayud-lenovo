package com.jayud.finance.enums;

import com.jayud.finance.vo.template.order.AirOrderTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillTemplateEnum {

    ONE("ky", AirOrderTemplate.class);

    private String cmd;
    private Class clazz;

    public static Class getTemplate(String cmd) {
        for (BillTemplateEnum values : BillTemplateEnum.values()) {
            if (values.cmd.equals(cmd)) {
                return values.clazz;
            }
        }
        return null;
    }

}
