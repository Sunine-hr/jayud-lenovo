package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.AuditOrderInteriorStatusForm;
import com.jayud.mall.model.bo.OrderInteriorStatusQueryForm;
import com.jayud.mall.model.po.OrderInteriorStatus;
import com.jayud.mall.mapper.OrderInteriorStatusMapper;
import com.jayud.mall.model.vo.OrderInteriorStatusVO;
import com.jayud.mall.service.IOrderInteriorStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单内部状态表(非流程状态) 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-20
 */
@Service
public class OrderInteriorStatusServiceImpl extends ServiceImpl<OrderInteriorStatusMapper, OrderInteriorStatus> implements IOrderInteriorStatusService {

    @Autowired
    OrderInteriorStatusMapper orderInteriorStatusMapper;

    @Override
    public OrderInteriorStatusVO findOrderInteriorStatusByOrderIdAndCode(OrderInteriorStatusQueryForm form) {
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusByOrderIdAndCode(form);
        return orderInteriorStatusVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditOrderInteriorStatus(AuditOrderInteriorStatusForm form) {
        Long id = form.getId();
        OrderInteriorStatusVO orderInteriorStatusVO = orderInteriorStatusMapper.findOrderInteriorStatusById(id);
        if(ObjectUtil.isEmpty(orderInteriorStatusVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "内部状态没有找到");
        }
        OrderInteriorStatus orderInteriorStatus = ConvertUtil.convert(orderInteriorStatusVO, OrderInteriorStatus.class);
        String statusFlag = form.getStatusFlag();
        orderInteriorStatus.setStatusFlag(statusFlag);
        this.saveOrUpdate(orderInteriorStatus);
    }

}
