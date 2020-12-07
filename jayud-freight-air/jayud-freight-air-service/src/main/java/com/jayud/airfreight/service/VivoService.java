package com.jayud.airfreight.service;

import cn.hutool.json.JSONObject;
import com.jayud.airfreight.model.bo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.ForwarderLadingFileForm;
import com.jayud.airfreight.model.bo.ForwarderLadingInfoForm;
import com.jayud.airfreight.model.bo.ForwarderVehicleInfoForm;
import org.springframework.web.multipart.MultipartFile;

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
    Boolean forwarderBookingConfirmedFeedback(ForwarderBookingConfirmedFeedbackForm form);


    /**
     * 货代车辆信息
     *
     * @param form
     * @return
     */
    Boolean forwarderVehicleInfo(ForwarderVehicleInfoForm form);

    /**
     * 向vivo发送空运提单文件
     *
     * @param form
     * @param file
     * @return
     */
    Boolean forwarderLadingFile(ForwarderLadingFileForm form, MultipartFile file);

    /**
     * 向vivo发送空运状态
     *
     * @param form
     * @return
     */
    boolean forwarderLadingInfo(ForwarderLadingInfoForm form);

    /**
     * 根据登录用户查询客户信息
     */
    public JSONObject getCustomerInfoByLoginUserName();
}
