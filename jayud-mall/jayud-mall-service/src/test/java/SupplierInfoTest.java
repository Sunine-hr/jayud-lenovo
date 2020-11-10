import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import org.junit.Test;

public class SupplierInfoTest {

    @Test
    public void test1(){
        QuerySupplierInfoForm form = new QuerySupplierInfoForm();
        form.setKeyword("HEUNG-A");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test2(){
        QuerySupplierInfoForm form = new QuerySupplierInfoForm();
        form.setSupplierChName("EMC");
        form.setSupplierEnName("EMC");
        form.setStatus("1");
        TestUtils.JSONObjectPrint(form);
    }


}
