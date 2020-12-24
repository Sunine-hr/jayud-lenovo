import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.utils.TestUtils;
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

    @Test
    public void test2(){
        CustomsClearanceForm form = new CustomsClearanceForm();
        form.setId(null);
        form.setIdCode("QG0001");
        form.setChName("毯子-12");
        TestUtils.JSONObjectPrint(form);
    }

}
