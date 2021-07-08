package com.jayud.customs.service;

import com.jayud.customs.model.po.CustomsFiling;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报关归档表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-07-08
 */
public interface ICustomsFilingService extends IService<CustomsFiling> {

    /**
     * 生成箱号
     * @param archiveDate
     * @param goodsType
     * @param bizModel
     * @return
     */
    String generateBoxNum(String archiveDate, Integer goodsType, Integer bizModel);
}
