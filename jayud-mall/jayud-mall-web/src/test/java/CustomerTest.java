import com.jayud.mall.model.bo.CustomerRegisterForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void test1(){
        CustomerRegisterForm form = new CustomerRegisterForm();
        form.setUserName("dd");
        form.setPasswd("123456");
        form.setAffirmPasswd("123456");
        form.setPhone("13900008888");
        form.setVerificationCode("1232");
        form.setCompany("A信息科技公司");
        TestUtils.JSONObjectPrint(form);


    }

}
