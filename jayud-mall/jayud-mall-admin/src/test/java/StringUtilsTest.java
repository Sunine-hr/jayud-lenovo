import com.jayud.common.utils.StringUtils;
import org.junit.Test;

/**
 * 测试 com.jayud.common.utils.StringUtils 工具类的方法
 */
public class StringUtilsTest {

    /**
     * 测试
     */
    @Test
    public void test1(){
        //生成不重复的数
        for (int i = 0; i < 1000; i++) {
            String aaaa = StringUtils.loadNum("AAAA", 5);
            System.out.println(aaaa);
        }
    }
}
