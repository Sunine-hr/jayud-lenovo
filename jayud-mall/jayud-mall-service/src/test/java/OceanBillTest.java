import com.jayud.mall.model.bo.QueryOceanBillForm;
import org.junit.Test;

import java.time.LocalDateTime;

public class OceanBillTest {

    @Test
    public void test1(){
        QueryOceanBillForm form = new QueryOceanBillForm();
        form.setOrderId("A00000000001");
        form.setSailTime(LocalDateTime.now());
        form.setSupplierCode("HEUNG-A");
        form.setEndCode("SH");
        TestUtils.JSONObjectPrint(form);

    }

}
