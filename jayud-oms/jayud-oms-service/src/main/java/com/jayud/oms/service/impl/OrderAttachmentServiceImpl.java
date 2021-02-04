package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.Query;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.OrderAttachment;
import com.jayud.oms.mapper.OrderAttachmentMapper;
import com.jayud.oms.service.IOrderAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单附件 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-02-02
 */
@Service
public class OrderAttachmentServiceImpl extends ServiceImpl<OrderAttachmentMapper, OrderAttachment> implements IOrderAttachmentService {

    /**
     * 根据主订单和描述修改附件信息
     */
    @Override
    public boolean update(String mainOrderNo, List<String> remarks, OrderAttachment orderAttachment) {
        QueryWrapper<OrderAttachment> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderAttachment::getMainOrderNo, mainOrderNo)
                .in(OrderAttachment::getRemarks, remarks);
        return this.update(orderAttachment, condition);
    }

    /**
     * 根据订单号和描述信息集合获取附件
     */
    @Override
    public List<OrderAttachment> getByMainOrderNoAndRemarks(String mainOrderNo, List<String> remarksList) {
        QueryWrapper<OrderAttachment> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderAttachment::getMainOrderNo, mainOrderNo)
                .in(OrderAttachment::getRemarks, remarksList)
                .eq(OrderAttachment::getStatus, StatusEnum.ENABLE.getCode());
        return this.baseMapper.selectList(condition);
    }
}
