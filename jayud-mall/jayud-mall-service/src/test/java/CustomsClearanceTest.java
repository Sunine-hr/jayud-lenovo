import com.jayud.mall.model.bo.CustomsClearanceForm;
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

    @Test
    public void test2(){
        CustomsClearanceForm form = new CustomsClearanceForm();
        form.setId(1L);
        form.setChName("毯子");
        TestUtils.JSONObjectPrint(form);
    }

}
