import com.jayud.mall.model.bo.QuerySupplierServeForm;
import org.junit.Test;

public class SupplierServeTest {

    @Test
    public void test1(){
        QuerySupplierServeForm form = new QuerySupplierServeForm();
        form.setServeName("海运40HQ报价");
        form.setSupplierCode("G001");
        form.setTransportPay(1);
        TestUtils.JSONObjectPrint(form);
    }

}
