package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.InlandFeeCostForm;
import com.jayud.mall.model.bo.QueryInlandFeeCostForm;
import com.jayud.mall.model.po.InlandFeeCost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.InlandFeeCostVO;

/**
 * <p>
 * 内陆费费用表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-08
 */
public interface IInlandFeeCostService extends IService<InlandFeeCost> {

    /**
     * 1.分页查询内陆费费用page
     * @param form
     * @return
     */
    IPage<InlandFeeCostVO> findInlandFeeCostByPage(QueryInlandFeeCostForm form);

    /**
     * 保存内陆费
     * @param form
     * @return
     */
    CommonResult saveInlandFeeCost(InlandFeeCostForm form);

    /**
     * 跟据id，查询内陆费费用
     * @param id
     * @return
     */
    InlandFeeCostVO findInlandFeeCostById(Long id);
}
