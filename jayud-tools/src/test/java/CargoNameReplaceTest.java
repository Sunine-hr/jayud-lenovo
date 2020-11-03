import com.alibaba.fastjson.JSONObject;
import com.jayud.tools.model.bo.CargoNameReplaceForm;
import org.junit.Test;

public class CargoNameReplaceTest {

    @Test
    public void test1(){
        CargoNameReplaceForm form = new CargoNameReplaceForm();
        form.setHpmc("服饰");
        form.setReplaceName("衣服");
        String json = JSONObject.toJSONString(form);
        System.err.println(json);
    }

    @Test
    public void test2(){
        CargoNameReplaceForm form = new CargoNameReplaceForm();
        form.setId(1L);
        form.setHpmc("服饰");
        form.setReplaceName("衣服");
        String json = JSONObject.toJSONString(form);
        System.err.println(json);
    }


}
