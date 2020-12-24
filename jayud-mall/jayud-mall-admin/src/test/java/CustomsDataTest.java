import com.jayud.mall.model.bo.CustomsDataForm;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class CustomsDataTest {

    @Test
    public void test1(){
        QueryCustomsDataForm form = new QueryCustomsDataForm();
        form.setIdCode("BG0001");
        form.setEnName("blanket");
        form.setHsCode("6000.40.3001");
        TestUtils.JSONObjectPrint(form);

    }

    @Test
    public void test2(){
        CustomsDataForm form = new CustomsDataForm();
        form.setId(null);
        form.setIdCode("BG0001");
        form.setChName("毯子2020-12");
        TestUtils.JSONObjectPrint(form);

    }



}
