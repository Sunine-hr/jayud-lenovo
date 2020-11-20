import com.jayud.mall.model.bo.DeliveryAddressForm;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class DeliveryAddressTest {

    /**
     * 查询提货地址
     */
    @Test
    public void test1(){
        QueryDeliveryAddressForm form = new QueryDeliveryAddressForm();
        form.setTypes(1);
        form.setContacts("啦啦啦");
        form.setPhone("");
        TestUtils.JSONObjectPrint(form);

    }

    /**
     * 保存-提货、收货地址
     */
    @Test
    public void test2(){
        DeliveryAddressForm form = new DeliveryAddressForm();
        form.setTypes(1);
        form.setContacts("小贝");
        TestUtils.JSONObjectPrint(form);
    }


}
