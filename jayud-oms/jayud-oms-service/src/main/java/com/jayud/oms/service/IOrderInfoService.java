package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AuditCostForm;
import com.jayud.oms.model.bo.InputCostForm;
import com.jayud.oms.model.bo.InputMainOrderForm;
import com.jayud.oms.model.bo.QueryOrderInfoForm;
import com.jayud.oms.model.po.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.InputCostVO;
import com.jayud.oms.model.vo.InputMainOrderVO;
import com.jayud.oms.model.vo.OrderInfoVO;

/**
 * <p>
 * 主订单基础数据表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 操作主订单
     * @param form
     * @return
     */
    public String oprMainOrder(InputMainOrderForm form);

    /**
     * 订单是否存在
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 分页查询未提交订单
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(QueryOrderInfoForm form);


    /**
     * 根据主键获取主订单信息
     * @param idValue
     * @return
     */
    InputMainOrderVO getMainOrderById(Long idValue);

    /**
     * 获取主订单主键
     * @param orderNo
     * @return
     */
    Long getIdByOrderNo(String orderNo);

    /**
     * 录入费用
     * @param form
     * @return
     */
    boolean saveOrUpdateCost(InputCostForm form);

    /**
     * 获取费用详情
     * @param id
     * @return
     */
    InputCostVO getCostDetail(Long id);

    /**
     * 审核费用
     * @param form
     * @return
     */
    boolean auditCost(AuditCostForm form);

}
