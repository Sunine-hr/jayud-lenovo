import com.jayud.mall.model.bo.QueryOrderConfForm;
import org.junit.Test;

public class OrderConfTest {

    @Test
    public void test1(){
        QueryOrderConfForm form = new QueryOrderConfForm();
        form.setOrderNo("PZ202006SEA001");
        form.setHarbourCode("NY");
        TestUtils.JSONObjectPrint(form);
    }



}
