import com.jayud.mall.model.bo.SaveCounterCaseForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;

public class CounterCaseTest {

    @Test
    public void test1(){
        SaveCounterCaseForm form = new SaveCounterCaseForm();
        form.setOceanCounterId(6L);
        form.setOrderCaseIds(Arrays.asList(27L,28L));
        TestUtils.JSONObjectPrint(form);
    }

}
