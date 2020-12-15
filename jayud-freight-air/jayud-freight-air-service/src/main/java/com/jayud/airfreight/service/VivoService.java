package com.jayud.airfreight.service;

import cn.hutool.json.JSONObject;
import com.jayud.airfreight.model.bo.InputOrderTransportForm;
import com.jayud.airfreight.model.bo.vivo.*;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.common.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * vivo数据接口服务
 *
 * @author william
 * @description
 * @Date: 2020-09-14 13:47
 */
public interface VivoService {


    /**
     * 货代确认订舱
     *
     * @param form
     * @return
     */
    Map<String, Object> forwarderBookingConfirmedFeedback(ForwarderBookingConfirmedFeedbackForm form);


    /**
     * 货代车辆信息
     *
     * @param form
     * @return
     */
    Map<String, Object> forwarderVehicleInfo(ForwarderVehicleInfoForm form);

    /**
     * 向vivo发送空运提单文件
     *
     * @param form
     * @param file
     * @return
     */
    Map<String, Object> forwarderLadingFile(ForwarderLadingFileForm form, MultipartFile file);

    /**
     * 提单跟踪信息回执给vivo
     *
     * @param form
     * @return
     */
    Map<String, Object> forwarderLadingInfo(ForwarderLadingInfoForm form);

    /**
     * 根据登录用户查询客户信息
     */
    public JSONObject getCustomerInfoByLoginUserName();

    /**
     * 创建订单
     */
    ApiResult createAirOrder(BookingSpaceForm form);

    /**
     * 创建中港订单
     */
    ApiResult createTmsOrder(CardInfoToForwarderForm form, InputOrderTransportForm orderTransportForm);

    boolean bookingFile(AirOrder airOrder, BookingFileTransferDataForm bookingFileTransferDataForm);


    /**
     * 订舱推送
     */
    public void bookingMessagePush(AirOrder airOrder, AirBooking airBooking);

    /**
     * vivo跟踪推送
     */
    public void trackingPush(AirOrder airOrder);

    /**
     * 货代抛空运费用数据到vivo
     *
     * @param form
     * @return
     */
    Map<String, Object> forwarderAirFarePush(ForwarderAirFreightForm form);
}
