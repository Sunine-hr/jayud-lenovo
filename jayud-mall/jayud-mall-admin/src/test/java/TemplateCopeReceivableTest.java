import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import org.junit.Test;

import java.math.BigDecimal;

public class TemplateCopeReceivableTest {

    @Test
    public void test1(){
        TemplateCopeReceivableForm form = new TemplateCopeReceivableForm();
        form.setId(1L);
        form.setQie(1);
        form.setCostName("海运费");
        form.setSpecificationCode("20GP");
        form.setCalculateWay(1);
        form.setCount(15);
        form.setUnit(1);
        form.setSource(1);
        form.setUnitPrice(new BigDecimal(2000.00));
        form.setCid(1);
        form.setAmount(new BigDecimal(2000.00));
        form.setRemarks("海运费");
        TestUtils.ObjectMapperPrint(form);


    }
}
