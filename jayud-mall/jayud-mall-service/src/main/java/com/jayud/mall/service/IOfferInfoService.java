package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OfferInfoForm;
import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.model.bo.QueryOfferInfoForm;
import com.jayud.mall.model.po.OfferInfo;
import com.jayud.mall.model.vo.FabWarehouseVO;
import com.jayud.mall.model.vo.OfferInfoVO;

import java.util.List;

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

    /**
     * 查看报价信息
     *
     * @param id
     * @return
     */
    CommonResult<OfferInfoVO> lookOfferInfo(Long id);

    /**
     * 分页查询报价(运价)
     * @param form
     * @return
     */
    IPage<OfferInfoVO> findOfferInfoFareByPage(QueryOfferInfoFareForm form);

    /**
     * 查看运价服务详情
     * @param id
     * @return
     */
    OfferInfoVO lookOfferInfoFare(Long id);

    /**
     * 去下单-确认订单(查看选择运价)
     * @param id
     * @return
     */
    OfferInfoVO purchaseOrders(Long id);

    /**
     * 查询运价单的可达仓库
     * @param id
     * @return
     */
    List<FabWarehouseVO> findFabWarehouse(Long id);

}
