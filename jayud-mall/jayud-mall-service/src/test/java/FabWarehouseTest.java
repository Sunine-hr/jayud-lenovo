import com.alibaba.fastjson.JSONObject;
import com.jayud.mall.model.bo.QueryFabWarehouseForm;
import org.junit.Test;

public class FabWarehouseTest {

    @Test
    public void test1(){
        QueryFabWarehouseForm form = new QueryFabWarehouseForm();
        form.setWarehouseCode("ONT8");
        form.setWarehouseName("ONT8");
        form.setStateCode("US");
        String json = JSONObject.toJSONString(form);
        System.err.println(json);
    }

}
