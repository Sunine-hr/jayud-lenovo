import com.jayud.mall.model.bo.CountryForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class CountryTest {

    /**
     * 查询国家基础信息
     */
    @Test
    public void test1(){
        CountryForm form = new CountryForm();
        form.setCode("CN");
        form.setName("中国");
        form.setStatus("1");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test2(){
        //从nacos中获取，新增用户，初始化密码
        String pwd = "123456";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bcryptPasswordEncoder.encode(pwd.trim());
        System.out.println(password);
    }

    @Test
    public void test3(){
        for (int i = 0; i<10; i++){
            System.out.println(i);
        }
    }

    @Test
    public void test4(){
        LocalDateTime startTime =  LocalDateTime.now();

        LocalDateTime localDateTime = startTime.plusDays(0);
        System.out.println(startTime);
        System.out.println(localDateTime);

        LocalDateTime localDateTime2 = startTime.plusDays(-2);
        System.out.println(localDateTime2);
    }

}
