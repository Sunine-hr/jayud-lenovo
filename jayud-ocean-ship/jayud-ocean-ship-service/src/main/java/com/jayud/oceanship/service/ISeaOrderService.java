package com.jayud.oceanship.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.po.SeaOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaOrderVO;

/**
 * <p>
 * 海运订单表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface ISeaOrderService extends IService<SeaOrder> {

    /**
     * 生成海运单
     * @param addSeaOrderForm
     */
    void createOrder(AddSeaOrderForm addSeaOrderForm);

    /**
     * 生成订单号
     * @return
     */
    String generationOrderNo();

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    boolean isExistOrder(String orderNo);
    /**
     * 根据主订单号获取订单信息
     * @param orderNo
     * @return
     */
    SeaOrder getByMainOrderNO(String orderNo);

    /**
     * 根据订单id获取订单信息
     * @param id
     * @return
     */
    SeaOrderVO getSeaOrderByOrderNO(Long id);

    /**
     * 分页获取海运订单信息
     * @param form
     * @return
     */
    IPage<SeaOrderFormVO> findByPage(QuerySeaOrderForm form);
}
