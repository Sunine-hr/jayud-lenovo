package com.jayud.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.tools.model.bo.AddFbaOrderForm;
import com.jayud.tools.model.bo.QueryFbaOrderForm;
import com.jayud.tools.model.po.FbaOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tools.model.vo.FbaOrderVO;

import java.util.List;

/**
 * <p>
 * FBA订单 服务类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
public interface IFbaOrderService extends IService<FbaOrder> {

    IPage selectPage(QueryFbaOrderForm queryFbaOrderForm);

    List<FbaOrderVO> selectList(QueryFbaOrderForm queryFbaOrderForm);

    void saveOrUpdateFbaOrder(AddFbaOrderForm addFbaOrderForm);

    void deleteById(List<Long> ids);

    FbaOrderVO getFbaOrderById(int id);

    FbaOrder getFbaOrderByOrderNo(String orderNo);

    FbaOrderVO getFbaOrderVOByOrderNo(String orderNo);

    FbaOrder getFbaOrderByCustomerNo(String customerNo);

    FbaOrder getFbaOrderByTransshipmentNo(String transshipmentNo);
}
