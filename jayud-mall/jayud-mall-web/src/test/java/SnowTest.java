import com.jayud.common.utils.IdGenerator;
import com.jayud.mall.utils.SnowflakeUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class SnowTest {

    @Test
    public void test1() throws FileNotFoundException {
        File f=new File("D://out2.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
        System.out.println("默认输出到了文件 out2.txt");
        long start = System.currentTimeMillis();
        for (int i=0; i<1000000; i++){
            String longNextId = IdGenerator.getLongNextId();
            System.out.println(longNextId);//19位
        }
        long end = System.currentTimeMillis();
        System.out.println("currentTimeMillis:   " + (end - start));

    }

    @Test
    public void test2(){
        for (int i = 0; i<100; i++){
            String orderNo = String.valueOf(SnowflakeUtils.getOrderNo());//雪花算法生成订单id
            System.out.println(orderNo);//18位
        }
    }
}
