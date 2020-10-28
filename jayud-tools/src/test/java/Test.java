import com.jayud.tools.model.vo.CargoNameVO;
import com.jayud.tools.utils.MathUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class Test {

    @org.junit.Test
    public void test1(){

        Double d = 22.924;
        BigDecimal b1 = MathUtils.getBigDecimal(d);
        String outString=b1.toString();
        System.out.println(outString);

        BigDecimal b2 = MathUtils.getBigDecimal(d, 3);
        outString=b2.toString();
        System.out.println(outString);

    }

    @org.junit.Test
    public void test2(){

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int length = s.length;
        System.out.println();
    }



}
