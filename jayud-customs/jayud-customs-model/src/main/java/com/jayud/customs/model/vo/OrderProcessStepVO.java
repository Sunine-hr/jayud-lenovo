package com.jayud.customs.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 委托进程VO
 *
 * @author william
 * @description
 * @Date: 2020-09-09 17:56
 */
@Data
public class OrderProcessStepVO {
    private List<TrustTraceVO> trustTrace;
    private List<DeclarationTraceVO> bgTrace;
}
