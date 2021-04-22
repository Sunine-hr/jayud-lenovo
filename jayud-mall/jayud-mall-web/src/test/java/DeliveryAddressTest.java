import com.jayud.mall.model.bo.DeliveryAddressForm;
import com.jayud.mall.model.bo.QueryDeliveryAddressForm;
import com.jayud.mall.model.vo.DeliveryAddressVO;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 选择提货地址
     */
    @Test
    public void test3(){
        List<DeliveryAddressVO> list = new ArrayList<>();

        DeliveryAddressVO form1 = new DeliveryAddressVO();
        form1.setId(1);
        form1.setContacts("啦啦啦");
        form1.setPhone("15252524545");
        form1.setStateName("广东省");
        form1.setCityName("深圳市");
        form1.setAddress("龙岗区信利康产业园2栋4楼");
        list.add(form1);

        DeliveryAddressVO form2 = new DeliveryAddressVO();
        form2.setId(2);
        form2.setContacts("小贝");
        form2.setPhone("15567678989");
        form2.setStateName("广东省");
        form2.setCityName("深圳市");
        form2.setAddress("龙岗区信利康产业园1栋1楼");
        list.add(form2);

        TestUtils.JSONObjectPrint(list);

    }


}
