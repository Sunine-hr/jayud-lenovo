package com.jayud.oms.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.oms.order.model.bo.CheckForm;
import com.jayud.oms.order.model.bo.InputOrderForm;
import com.jayud.oms.order.model.bo.OmsOrderForm;
import com.jayud.oms.order.model.po.OmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.order.model.vo.OrderVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单管理——订单主表 服务类
 *
 * @author jayud
 * @since 2022-03-23
 */
public interface IOmsOrderService extends IService<OmsOrder> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrder
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrder>
     **/
    IPage<OmsOrder> selectPage(OmsOrder omsOrder,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrder
     * @param: req
     * @return: java.util.List<com.jayud.oms.order.model.po.OmsOrder>
     **/
    List<OmsOrder> selectList(OmsOrder omsOrder);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-23
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(List<Long> id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-23
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryOmsOrderForExcel(Map<String, Object> paramMap);

    /**
     * 保存订单
     * @param form
     */
    BaseResult saveOmsOrder(InputOrderForm form);

    /**
     * 修改订单操作时间和备注
     * @param omsOrderForm
     * @return
     */
    BaseResult updateOmsOrderById(OmsOrderForm omsOrderForm);

    /**
     * 审核
     * @param checkForm
     * @return
     */
    BaseResult check(CheckForm checkForm);

    /**
     * 反审
     * @param checkForm
     * @return
     */
    BaseResult unCheck(CheckForm checkForm);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    OrderVO getOrderById(Long id);

    /**
     * 复制新建订单
     * @param id
     * @return
     */
    BaseResult copyNew(Long id);
}
