package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.OfferInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.OfferInfoVO;

/**
 * <p>
 * 报价管理 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-05
 */
public interface IOfferInfoService extends IService<OfferInfo> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<OfferInfoVO> findOfferInfoByPage(QueryOfferInfoForm form);

    /**
     * 禁用报价
     * @param id
     */
    void disabledOfferInfo(Long id);

    /**
     * 启用报价
     * @param id
     */
    void enableOfferInfo(Long id);

    /**
     * 保存报价
     * @param form
     */
    void saveOfferInfo(OfferInfoForm form);
}