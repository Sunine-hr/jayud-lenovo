import com.jayud.mall.model.bo.QueryCurrencyRateForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;

public class CurrencyRateTest {

    @Test
    public void test1(){
        QueryCurrencyRateForm form = new QueryCurrencyRateForm();
        form.setDcid(1);
        form.setOcid(2);
        form.setExchangeRate(new BigDecimal(0.151500));
        TestUtils.JSONObjectPrint(form);
    }

}
