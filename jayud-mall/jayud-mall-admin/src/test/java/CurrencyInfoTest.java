import com.jayud.mall.model.bo.QueryCurrencyInfoForm;
import org.junit.Test;

public class CurrencyInfoTest {

    @Test
    public void test1(){
        QueryCurrencyInfoForm form = new QueryCurrencyInfoForm();
        form.setCurrencyCode("CNY");
        form.setCurrencyName("人民币");
        form.setCountryCode("CN");
        form.setStatus("1");
        TestUtils.JSONObjectPrint(form);
    }

}
