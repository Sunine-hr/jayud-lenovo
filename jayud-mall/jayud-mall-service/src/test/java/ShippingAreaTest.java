import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import org.junit.Test;

public class ShippingAreaTest {

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

    @Test
    public void test1(){
        QueryShippingAreaForm form = new QueryShippingAreaForm();
        form.setWarehouseCode("SZ");
        form.setWarehouseName("苏州仓");
        form.setStateCode("CN");
        ObjectMapperPrint(form);

    }


}
