import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import org.junit.Test;

public class CustomsClearanceTest {

    @Test
    public void test1(){
        QueryCustomsClearanceForm form = new QueryCustomsClearanceForm();
        form.setIdCode("Q001");
        form.setCustomsCode("USA");
        form.setHsCode("6000.40.3001");
        TestUtils.JSONObjectPrint(form);
    }

}
