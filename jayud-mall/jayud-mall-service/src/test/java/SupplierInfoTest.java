import com.jayud.mall.model.bo.SupplierInfoForm;
import org.junit.Test;

public class SupplierInfoTest {

    @Test
    public void test1(){
        SupplierInfoForm form = new SupplierInfoForm();
        form.setKeyword("HEUNG-A");
        TestUtils.JSONObjectPrint(form);

    }

}
