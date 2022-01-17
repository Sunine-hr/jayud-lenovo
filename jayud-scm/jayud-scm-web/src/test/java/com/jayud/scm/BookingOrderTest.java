package com.jayud.scm;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.jayud.scm.model.enums.StateFlagEnum;
import org.junit.Test;

public class BookingOrderTest {


    /**
     * 委托单号
     */
    @Test
    public void bookingNoTest(){
        /*
        使用毫秒数，生成订单号
        */
        long l = System.currentTimeMillis();
        System.out.println(l);//1629166130847

        /*
        使用hutool的IdUtil，生成订单号
        */
        //生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
        String uuid = IdUtil.randomUUID();
        System.out.println(uuid);
        //生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        String simpleUUID = IdUtil.simpleUUID();
        System.out.println(simpleUUID);

        /*
        IdUtil.getSnowflake使用全局单例对象
        */
        Snowflake snowflake = IdUtil.getSnowflake(1,1);
        long id = snowflake.nextId();
        System.out.println(id);//1427455157129580544

    }

    /**
     * 打印枚举，给前端
     */
    @Test
    public void stateFlagTest(){
        System.out.println(StateFlagEnum.getStateFlagEnumList());
    }

}
