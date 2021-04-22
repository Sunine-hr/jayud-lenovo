import com.jayud.mall.model.bo.OrderCopeReceivableForm;
import com.jayud.mall.model.bo.OrderInfoCostForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoCostTest {

    /**
     * 修改订单费用明细
     */
    @Test
    public void test1(){
        OrderInfoCostForm form = new OrderInfoCostForm();
        List<OrderCopeReceivableForm> orderCopeReceivableVOList = new ArrayList<>();
        OrderCopeReceivableForm form1 = new OrderCopeReceivableForm();
        form1.setId(1L);
        form1.setAmount(new BigDecimal("3999.99"));
        orderCopeReceivableVOList.add(form1);
        form.setOrderCopeReceivableVOList(orderCopeReceivableVOList);
        TestUtils.JSONObjectPrint(form);
    }


}
