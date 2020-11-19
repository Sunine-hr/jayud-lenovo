import com.jayud.mall.model.bo.CountryForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

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

}
