import com.jayud.mall.model.bo.OrderCaseForm;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderCaseTest {

    @Test
    public void test1(){
        QueryOrderCaseForm form = new QueryOrderCaseForm();
        form.setCartonNo("XH202006170001");
        TestUtils.JSONObjectPrint(form);
    }

    /**
     * 修改订单箱号
     */
    @Test
    public void test2(){
        List<OrderCaseForm> list = new ArrayList<>();

        OrderCaseForm form = new OrderCaseForm();
        form.setId(1L);
        form.setWmsWeight(new BigDecimal("126.67"));
        form.setWmsLength(new BigDecimal("125"));
        form.setWmsWidth(new BigDecimal("135"));
        form.setWmsHeight(new BigDecimal("124"));
        form.setWmsVolume(new BigDecimal("1.3"));

        form.setConfirmWeight(new BigDecimal("126.67"));
        form.setConfirmVolume(new BigDecimal("1.3"));
        list.add(form);

        OrderCaseForm form1 = new OrderCaseForm();
        form1.setId(2L);
        form1.setWmsWeight(new BigDecimal("126.67"));
        form1.setWmsLength(new BigDecimal("125"));
        form1.setWmsWidth(new BigDecimal("135"));
        form1.setWmsHeight(new BigDecimal("124"));
        form1.setWmsVolume(new BigDecimal("1.3"));

        form1.setConfirmWeight(new BigDecimal("126.67"));
        form1.setConfirmVolume(new BigDecimal("1.3"));
        list.add(form1);

        TestUtils.JSONObjectPrint(list);
    }

}
