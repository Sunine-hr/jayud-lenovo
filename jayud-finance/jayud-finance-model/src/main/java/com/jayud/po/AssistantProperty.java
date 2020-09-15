package com.jayud.po;

import com.jayud.enums.FormIDEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 费用类型，费用类别查询（金蝶基础资料-辅助属性）
 *
 * @author william
 * @description
 * @Date: 2020-04-26 16:34
 */
@Data
@K3Entity(value = {"FId", "FNumber", "FDataValue"}, formId = FormIDEnum.ASSISTANT,fName = "FDataValue")
public class AssistantProperty implements Serializable {
    private static final long serialVersionUID = -6921449813226381950L;
    private String FId;
    private String FNumber;
    private String FDataValue;

}
