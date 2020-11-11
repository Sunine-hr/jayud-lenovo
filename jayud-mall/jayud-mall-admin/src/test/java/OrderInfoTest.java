import com.jayud.mall.model.bo.QueryOrderInfoForm;
import org.junit.Test;

public class OrderInfoTest {

    @Test
    public void test1(){
        QueryOrderInfoForm form = new QueryOrderInfoForm();
        form.setOrderNo("DD0001");
        TestUtils.JSONObjectPrint(form);

    }
}
