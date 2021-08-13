package com.jayud.oms.service;

import com.jayud.oms.model.po.AppletOrderRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.DriverOrderTransportVO;

import java.util.List;

/**
 * <p>
 * 小程序订单记录 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-12
 */
public interface IAppletOrderRecordService extends IService<AppletOrderRecord> {


    public List<AppletOrderRecord> getByCondition(AppletOrderRecord appletOrderRecord);


    List<DriverOrderTransportVO> getConvertPendingRecord();
}
