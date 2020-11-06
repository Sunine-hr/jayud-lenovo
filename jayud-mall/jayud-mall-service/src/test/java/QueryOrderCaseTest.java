import com.jayud.mall.model.bo.QueryOrderCaseForm;
import org.junit.Test;

public class QueryOrderCaseTest {

    @Test
    public void test1(){
        QueryOrderCaseForm form = new QueryOrderCaseForm();
        form.setCartonNo("XH202006170001");
        TestUtils.JSONObjectPrint(form);
    }
}
