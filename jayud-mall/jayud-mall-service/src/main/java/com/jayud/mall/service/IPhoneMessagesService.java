package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PhoneForm;

/**
 * <p>
 * 手机短信 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IPhoneMessagesService {

    /**
     * 发送消息
     * @param form
     * @return
     */
    CommonResult sendMessage(PhoneForm form);

}
