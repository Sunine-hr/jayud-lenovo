package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OceanBillVO;

/**
 * <p>
 * 提单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
public interface IOceanBillService extends IService<OceanBill> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form);
}
