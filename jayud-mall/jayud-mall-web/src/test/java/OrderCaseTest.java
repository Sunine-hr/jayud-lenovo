import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;

public class OrderCaseTest {

    /**
     * 生成箱号
     */
    @Test
    public void test1(){
        CreateOrderCaseForm form = new CreateOrderCaseForm();
        form.setCartons(4);
        form.setWeight(new BigDecimal("40"));
        form.setLength(new BigDecimal("150"));
        form.setWidth(new BigDecimal("180"));
        form.setHeight(new BigDecimal("150"));
        TestUtils.JSONObjectPrint(form);
    }


}
