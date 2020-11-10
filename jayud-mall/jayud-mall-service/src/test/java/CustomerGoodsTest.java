import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import org.junit.Test;

public class CustomerGoodsTest {

    @Test
    public void test1(){
        QueryCustomerGoodsForm form = new QueryCustomerGoodsForm();
        form.setSku("81131008836812");
        form.setNameCn("小蜜蜂帽子");
        form.setCustomerId(1);
        form.setDataCode("BG00026");
        form.setClearanceCode("QG0078");
        TestUtils.JSONObjectPrint(form);


    }

}
