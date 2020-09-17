package com.jayud.customs.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 下载报关单VO
 *
 * @author william
 * @description
 * @Date: 2020-09-09 15:46
 */
@Data
public class DownloadCustomsDeclarationVO {
    private List<CustomsHeadVO> head;
    private List<CustomsDetailVO> dtls;
    private List<CustomsGoodsVO> gdtls;
    private List<CustomsAppendixVO> adtls;
}
