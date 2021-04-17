package com.jayud.mall;

import com.jayud.mall.utils.NumberGeneratedUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class NumberGeneratedUtilsTest {

    @Test
    public void getOrderNoByCode() {
        NumberGeneratedUtils numberGeneratedUtils = new NumberGeneratedUtils();
        String code = numberGeneratedUtils.getOrderNoByCode1("order_conf_code");//null指针异常
        log.info(code);
    }

    @Test
    public void getOrderNoByCode2(){
        String code = NumberGeneratedUtils.getOrderNoByCode2("order_conf_code");//成功
        log.info(code);
    }


    @Test
    public void getOrderNoByCode3(){
        String code = NumberGeneratedUtils.getOrderNoByCode3("order_conf_code");
        log.info(code);
    }

}
