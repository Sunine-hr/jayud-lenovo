import com.jayud.mall.model.bo.CustomerGoodsAuditForm;
import com.jayud.mall.model.bo.CustomerGoodsForm;
import com.jayud.mall.model.bo.QueryCustomerGoodsForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void test2(){
        CustomerGoodsForm form = new CustomerGoodsForm();
        form.setId(1);
        form.setStatus(1);
        form.setDataCode("BG00026");
        form.setClearanceCode("QG0078");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test3(){
        CustomerGoodsAuditForm form = new CustomerGoodsAuditForm();
        List<Integer> ids = Arrays.asList(1,2);
        form.setIds(ids);
        form.setStatus(1);
        form.setDataCode("B001");
        form.setClearanceCode("QG001");
        TestUtils.JSONObjectPrint(form);
    }

}
