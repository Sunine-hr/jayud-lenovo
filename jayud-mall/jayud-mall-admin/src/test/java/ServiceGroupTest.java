import com.alibaba.fastjson.JSONObject;
import com.jayud.mall.model.bo.ServiceGroupForm;
import org.junit.Test;

public class ServiceGroupTest {

    @Test
    public void test1(){
        ServiceGroupForm form = new ServiceGroupForm();
        form.setIdCode("A");
        form.setCodeName("美国美森快船服务");
        form.setStatus("1");
        String json = JSONObject.toJSONString(form);
        System.out.println(json);
    }
}
