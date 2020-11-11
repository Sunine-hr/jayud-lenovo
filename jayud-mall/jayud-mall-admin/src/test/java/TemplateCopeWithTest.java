import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;

public class TemplateCopeWithTest {

    @Test
    public void test1(){
        TemplateCopeWithForm form = new TemplateCopeWithForm();
        form.setId(1L);
        form.setQie(1);
        form.setCostName("订船费");
        form.setSupplierCode("xx供应商");
        form.setCalculateWay(1);
        form.setCount(1);
        form.setUnit(1);
        form.setSource(1);
        form.setUnitPrice(new BigDecimal(1000.00));
        form.setCid(1);
        form.setAmount(new BigDecimal(1000.00));
        form.setRemarks("订船费");
        TestUtils.ObjectMapperPrint(form);


    }

}
