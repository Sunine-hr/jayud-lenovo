import com.alibaba.fastjson.JSONObject;
import com.jayud.mall.model.bo.TransportWayForm;
import org.junit.Test;

public class TransportWayTest {

    @Test
    public void test1(){
        TransportWayForm form = new TransportWayForm();
        form.setIdCode("A");
        form.setCodeName("海运");
        form.setStatus("1");
        String json = JSONObject.toJSONString(form);
        System.err.println(json);

    }

}
