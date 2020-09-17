package com.jayud.customs.service;

import com.jayud.model.bo.*;
import com.jayud.model.vo.*;

/**
 * api接口服务
 *
 * @author william
 * @description
 * @Date: 2020-09-07 15:43
 */
public interface ICustomsApiService {
    /**
     * 云报关登录
     *
     * @param form
     */
    void login(LoginForm form);

    /**
     * 根据用户名和密码获取云报关token
     *
     * @param form
     * @return
     */
    String checkoutUserToken(LoginForm form);

    /**
     * 上传委托单
     *
     * @param form
     * @return
     */
    PushOrderVO pushOrder(PushOrderForm form);

    /**
     * 上传委托单附件
     *
     * @param form
     */
    void pushAppendix(PushAppendixForm form);

    FindOrderInfoVO findOrderInfo(FindOrderInfoWrapperForm form);

    FindOrderDetailVO findOrderDetail(String uid);

    DownloadCustomsDeclarationVO DownloadCustomsDeclaration(String id, String idType);

    DclarationProcessStepVO getDeclarationProcessStep(String id);

    OrderProcessStepVO getOrderProcessStep(String id);
}
