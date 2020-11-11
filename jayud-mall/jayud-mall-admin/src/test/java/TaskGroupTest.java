import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayud.mall.model.bo.TaskGroupForm;
import org.junit.Test;

public class TaskGroupTest {
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
        TaskGroupForm form = new TaskGroupForm();
        form.setId(1L);
        form.setIdCode("A");
        form.setCodeName("拣货A");
        form.setStatus("1");
        form.setTypes(1);
        ObjectMapperPrint(form);
    }

}
