import com.jayud.mall.model.bo.QueryCustomsDataForm;
import org.junit.Test;

public class CustomsDataTest {

    @Test
    public void test1(){
        QueryCustomsDataForm form = new QueryCustomsDataForm();
        form.setIdCode("B001");
        form.setEnName("blanket");
        form.setHsCode("6000.40.3001");
        TestUtils.JSONObjectPrint(form);

    }

}
