package com.jayud.test;

import cn.hutool.json.JSONUtil;
import com.jayud.model.bo.CustomsHeadForm;
import com.jayud.model.bo.FindOrderInfoWrapperForm;
import com.jayud.model.bo.LoginForm;
import com.jayud.model.bo.PushOrderForm;
import com.jayud.service.ICustomsApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * @author william
 * @description
 * @Date: 2020-09-07 16:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestThis {
    @Autowired
    ICustomsApiService service;

    @Test
    public void loginAndSaveToken() {
        service.login(new LoginForm("szjyd002", "8520", ""));
    }

    @Test
    public void getToken() {
        System.out.println(service.checkoutUserToken(new LoginForm("szjyd002", "8520", "")));
    }

    @Test
    public void pushtest() {
        PushOrderForm form = new PushOrderForm();
        form.setHead(new CustomsHeadForm());
        form.setAdtl(new ArrayList<>());
        form.setDtl(new ArrayList<>());
        form.setGdtl(new ArrayList<>());


        System.out.println(JSONUtil.toJsonStr(service.pushOrder(form)));
    }


    @Test
    public void getOrderTest() {
        System.out.println(JSONUtil.toJsonStr(service.findOrderInfo(new FindOrderInfoWrapperForm())));
    }
}
