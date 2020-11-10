import com.jayud.mall.model.bo.CustomerForm;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void test1(){
        CustomerForm form = new CustomerForm();
        form.setId(1);
        form.setCompany("深圳市坤鑫国际货运代理有限公司");
        TestUtils.JSONObjectPrint(form);


    }

}
