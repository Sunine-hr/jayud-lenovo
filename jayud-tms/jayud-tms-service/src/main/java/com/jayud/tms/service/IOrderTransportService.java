package com.jayud.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.OprStatusForm;
import com.jayud.tms.model.bo.QueryDriverOrderTransportForm;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.tms.model.vo.DriverOrderTransportVO;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.model.vo.SendCarPdfVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * 中港运输订单 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
public interface IOrderTransportService extends IService<OrderTransport> {


    /**
     * 创建订单
     *
     * @param form
     * @return
     */
    boolean createOrderTransport(InputOrderTransportForm form);

    /**
     * 订单是否存在
     *
     * @param orderNo
     * @return
     */
    public boolean isExistOrder(String orderNo);

    /**
     * 获取订单详情
     *
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(String mainOrderNo);

    /**
     * 中港分页查询
     *
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(QueryOrderTmsForm form);

    /**
     * 初始化确认派车时的PDF数据
     *
     * @param orderNo
     * @param classCode
     * @return
     */
    SendCarPdfVO initPdfData(String orderNo, String classCode);

    /**
     * 分页查询司机的中港订单信息
     *
     * @param form
     * @return
     */
    List<DriverOrderTransportVO> getDriverOrderTransport(QueryDriverOrderTransportForm form);

    /**
     * 获取中港订单状态
     */
    String getOrderTransportStatus(String orderNo);

    /**
     * 司机反馈状态
     * @param form
     */
    void doDriverFeedbackStatus(OprStatusForm form);

    /**
     * 小程序司机车辆通关（补出仓和入仓数据）,送货地址只有一个时候才做这个操作
     */
    void driverCustomsClearanceVehicles(OprStatusForm form);
}
