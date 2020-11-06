import com.jayud.mall.model.bo.OceanCounterForm;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OceanCounterTest {

    @Test
    public void test1(){
        OceanCounterForm form = new OceanCounterForm();
        form.setId(1L);
        form.setCntrNo("A0001");
        form.setCabinetCode("20GP");
        form.setVolume(100.00);
        form.setCost(new BigDecimal(1000.00));
        form.setCid(1);
        form.setStatus("1");
        form.setObId(1L);
        form.setCreateTime(LocalDateTime.now());
        form.setCustomerId(1L);

        TestUtils.JSONObjectPrint(form);

    }

}
