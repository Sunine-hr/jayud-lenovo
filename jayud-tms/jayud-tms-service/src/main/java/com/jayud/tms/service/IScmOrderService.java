package com.jayud.tms.service;

import com.jayud.tms.model.bo.ScmTransportationInformationForm;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * <p>
 * 供应链订单接口 服务类
 * </p>
 *
 * @author cyc
 * @since 2021-10-08
 */
public interface IScmOrderService {

    /**
     * 设置车次状态
     *
     * @param trainStatus
     * @param truckNo
     * @return
     */
    String setManifest(String trainStatus, String truckNo, String unitCode);

    /**
     * 设置运输公司信息
     *
     * @param scmTransportationInformationForm
     * @return
     */
    String acceptTransportationInformation(
            ScmTransportationInformationForm scmTransportationInformationForm, String unitCode);

    /**
     * 刷新Token
     */
    void refreshToken();

}
