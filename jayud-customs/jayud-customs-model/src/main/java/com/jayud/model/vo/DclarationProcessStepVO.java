package com.jayud.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 报关进程查询VO
 *
 * @author william
 * @description
 * @Date: 2020-09-09 16:58
 */
@Data
public class DclarationProcessStepVO {
    private DeclarationHeadVO head;
    private List<DeclarationOPDetailVO> dtl;
}
