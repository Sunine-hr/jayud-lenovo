package com.jayud.customs.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author william
 * @description
 * @Date: 2020-09-08 11:40
 */

@Data
public class FindOrderInfoVO {
    private List<CustomsHeadVO> rows;
    private Integer totalRecord;
    private Integer curPage;
    private Integer totalPages;
}
