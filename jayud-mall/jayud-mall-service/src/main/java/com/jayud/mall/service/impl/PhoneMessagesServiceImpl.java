package com.jayud.mall.service.impl;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PhoneForm;
import com.jayud.mall.service.IPhoneMessagesService;
import com.jayud.mall.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单对应商品 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-16
 */
@Service
@Slf4j
public class PhoneMessagesServiceImpl implements IPhoneMessagesService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public CommonResult sendMessage(PhoneForm form) {
        String phone = form.getPhone();
        String code = createRandomNum(6);//生成6位随机数，作为验证码
        String msg = "尊敬的用户，您好，您的验证码为："+code+"，请于"+5+"分钟内正确输入，如非本人操作，请忽略此短信。";
        log.info("LE***","d41***",phone,msg);
        redisUtils.set(phone, code, 60*5);//验证码保存在redis，有效期5分钟
        return CommonResult.success(msg);
    }

    /**
     * 生成随机数
     * @param num 位数
     * @return
     */
    public static String createRandomNum(int num){
        String randomNumStr = "";
        for(int i = 0; i < num;i ++){
            int randomNum = (int)(Math.random() * 10);
            randomNumStr += randomNum;
        }
        return randomNumStr;
    }

}
