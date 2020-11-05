import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试工具类
 */
public class TestUtils {

    /**
     * <p>object -> json</p>
     * <p>json有顺序</p>
     *
     * @param t
     */
    public static void ObjectMapperPrint(Object t) {
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
     *
     * @param t
     */
    public static void JSONObjectPrint(Object t) {
        //输出null
        String s = JSONObject.toJSONString(t, SerializerFeature.WriteMapNullValue);
        System.err.println(s);
    }

}
