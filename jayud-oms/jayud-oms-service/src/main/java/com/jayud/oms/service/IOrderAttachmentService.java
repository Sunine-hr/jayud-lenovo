package com.jayud.oms.service;

import com.jayud.oms.model.po.OrderAttachment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单附件 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-02-02
 */
public interface IOrderAttachmentService extends IService<OrderAttachment> {

    /**
     * 根据主订单和描述修改附件信息
     */
    public boolean update(String mainOrderNo, List<String> remarks, OrderAttachment orderAttachment);

    /**
     * 根据订单号和描述信息集合获取附件
     */
    public List<OrderAttachment> getByMainOrderNoAndRemarks(String mainOrderNo,List<String> remarksList);
}
