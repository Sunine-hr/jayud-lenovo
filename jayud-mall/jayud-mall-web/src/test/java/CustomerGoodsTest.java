import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class CustomerGoodsTest {

    /**
     * 保存客户商品
     */
    @Test
    public void test1(){
        CustomerGoodsForm form = new CustomerGoodsForm();
        form.setId(1);
        form.setCustomerId(1);
        form.setNameCn("小蜜蜂帽子2020");
        form.setImageUrl("htttp:url1");
        TestUtils.JSONObjectPrint(form);

    }

    @Test
    public void test2(){
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime firstday = date.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime lastDay = date.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        System.out.println("firstday:" + firstday);
        System.out.println("lastDay:" + lastDay);
    }

    @Test
    public void test3(){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minTime = localDateTime.with(LocalTime.MIN);
        LocalDateTime maxTime = localDateTime.with(LocalTime.MAX);
        System.out.println("minTime:" + minTime);
        System.out.println("maxTime:" + maxTime);
    }


}
