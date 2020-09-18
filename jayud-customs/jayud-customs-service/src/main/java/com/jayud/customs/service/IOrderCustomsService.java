package com.jayud.customs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
import com.jayud.customs.model.vo.OrderCustomsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关业务订单表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderCustomsService extends IService<OrderCustoms> {

    /**
     * 报关子订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     *暂存/提交纯报关订单
     * @param form
     * @return
     */
    public boolean oprOrderCustoms(InputOrderCustomsForm form);

    /**
     * 编辑时获取订单详情
     * @param id
     * @return
     */
    public InputOrderCustomsVO editOrderCustomsView(Long id);

    /**
     * 获取子订单
     * @param param
     * @return
     */
    public List<OrderCustomsVO> findOrderCustomsByCondition(Map<String,Object> param);

}
