package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.OrderProcess;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单流程 服务类
 *
 * @author jyd
 * @since 2021-12-14
 */
public interface IOrderProcessService extends IService<OrderProcess> {

    /**
     * 分页查询
     *
     * @param orderProcess
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<OrderProcess> selectPage(OrderProcess orderProcess,
                                   Integer pageNo,
                                   Integer pageSize,
                                   HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param orderProcess
     * @return
     */
    List<OrderProcess> selectList(OrderProcess orderProcess);

    /**
     * 生成流程配置
     * @param orderNo
     * @param warehouseId
     * @param type 类型(1:入库,2:出库)
     */
    void generationProcess(String orderNo, Long warehouseId, Integer type);

    String getNextNode(String receiptNoticeNum, String fStatus);
}
