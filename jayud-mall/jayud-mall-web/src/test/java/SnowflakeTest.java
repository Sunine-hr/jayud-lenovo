import com.jayud.mall.utils.SnowflakeUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class SnowflakeTest {

    /**
     * SnowflakeUtils,生产单号
     */
    @Test
    public void test1() throws FileNotFoundException {
        File f=new File("D://out.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
        System.out.println("默认输出到了文件 out.txt");
        long start = System.currentTimeMillis();
        for (int i=0; i<1000000; i++){
            long orderNo = SnowflakeUtils.getOrderNo();
            System.out.println(orderNo);
        }
        long end = System.currentTimeMillis();
        System.out.println("currentTimeMillis:   " + (end - start));
    }
}
