package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomerGoodsAuditForm;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.model.po.CustomerGoods;
import com.jayud.mall.model.vo.CustomerGoodsVO;

import java.util.List;

/**
 * <p>
 * 客户商品表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface ICustomerGoodsService extends IService<CustomerGoods> {

    /**
     * 分页
     * @param form
     * @return
     */
    IPage<CustomerGoodsVO> findCustomerGoodsByPage(QueryCustomerGoodsForm form);

    /**
     * 审核客户商品
     * @param form
     * @return
     */
    CommonResult auditCustomerGoods(CustomerGoodsAuditForm form);

    /**
     * 保存客户商品
     * @param form
     * @return
     */
    CustomerGoodsVO saveCustomerGoods(CustomerGoodsForm form);

    /**
     * 批量保存-客户商品
     * @param list
     */
    void batchSaveCustomerGoods(List<CustomerGoodsVO> list);

    /**
     * 订单详情-查看商品详情(根据商品id查询)
     * @param id
     * @return
     */
    CommonResult<CustomerGoodsVO> findCustomerGoodsById(Integer id);

    /**
     * 根据商品id，查看商品，及其商品服务费用
     * @param id
     * @return
     */
    CustomerGoodsVO findCustomerGoodsCostById(Integer id);
}
