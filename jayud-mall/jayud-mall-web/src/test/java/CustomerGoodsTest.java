import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class CustomerGoodsTest {

    /**
     * 保存客户商品
     */
    @Test
    public void test1(){
        CustomerGoodsForm form = new CustomerGoodsForm();
        form.setId(1);
        form.setCustomerId(1);
        form.setNameCn("小蜜蜂帽子2020");
        form.setImageUrl("htttp:url1");
        TestUtils.JSONObjectPrint(form);

    }


}
