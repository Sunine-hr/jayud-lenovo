import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.mall.model.bo.GoodsTypeForm;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GoodsTypeTest {

    /**
     * <p>object -> json</p>
     * <p>json有顺序</p>
     * @param t
     */
    private void ObjectMapperPrint(Object t){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(t);
            System.err.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>object -> json</p>
     * <p>json没有顺序</p>
     * @param t
     */
    private void JSONObjectPrint(Object t){
        String s = JSONObject.toJSONString(t);
        System.err.println(s);
    }

    @Test
    public void test1(){
        GoodsTypeForm form = new GoodsTypeForm();
        form.setId(1L);
        form.setName("特货");
        form.setFid(0);
        form.setStatus("1");
        form.setTypes("2");
        ObjectMapperPrint(form);

        JSONObjectPrint(form);

    }




}
