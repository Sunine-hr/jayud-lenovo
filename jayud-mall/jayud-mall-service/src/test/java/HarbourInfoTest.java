import com.alibaba.fastjson.JSONObject;
import com.jayud.mall.model.bo.HarbourInfoForm;
import org.junit.Test;

public class HarbourInfoTest {
    @Test
    public void test1(){
        HarbourInfoForm form = new HarbourInfoForm();
        form.setIdCode("SZ");
        form.setCodeName("深圳");
        form.setCodeNameEn("shenzhen");
        form.setStateCode("CN");
        form.setGenre(2);
        form.setStatus("1");
        String json = JSONObject.toJSONString(form);
        System.err.println(json);
    }

}
