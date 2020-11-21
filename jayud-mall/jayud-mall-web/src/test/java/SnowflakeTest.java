import com.jayud.mall.utils.SnowflakeUtils;
import org.junit.Test;

public class SnowflakeTest {

    /**
     * SnowflakeUtils,生产单号
     */
    @Test
    public void test1(){
        long orderNo = SnowflakeUtils.getOrderNo();
        System.out.println(orderNo);
    }
}
