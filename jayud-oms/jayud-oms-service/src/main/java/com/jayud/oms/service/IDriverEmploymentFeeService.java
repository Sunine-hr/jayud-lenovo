package com.jayud.oms.service;

import com.jayud.oms.model.po.DriverEmploymentFee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.DriverEmploymentFeeVO;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 司机录入费用表(小程序使用) 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
public interface IDriverEmploymentFeeService extends IService<DriverEmploymentFee> {

    /**
     * 根据订单编码查询录用费用明细
     *
     * @param orderNo
     * @return
     */
    List<DriverEmploymentFee> getEmploymentFee(String orderNo, Long driverId, String status);

    /**
     * 根据订单编码查询录用费用明细
     * @param orderId
     * @param driverId
     * @param status
     * @return
     */
    List<DriverEmploymentFee> getEmploymentFee(Long orderId, Long driverId, String status);

    /**
     * 司机费用项是否存在
     */
    boolean isExist(Long driverId, Long orderId, String status);

    /**
     * 司机费用提交
     */
    void feeSubmission(List<DriverEmploymentFee> driverEmploymentFees);

    /**
     * 查询费用详情
     *
     * @param orderNo
     * @return
     */
    List<DriverEmploymentFeeVO> getEmploymentFeeInfo(String orderNo);

    List<DriverEmploymentFee> getByCondition(DriverEmploymentFee driverEmploymentFee);

    List<DriverEmploymentFee> getByOrderNos(List<String> orderNos, String status);


    List<DriverEmploymentFeeVO> getCost(Long orderId, Long driverId, String status);
}
