import com.jayud.mall.model.bo.QueryOfferInfoForm;
import org.junit.Test;

import java.time.LocalDateTime;

public class OfferInfoTest {

    @Test
    public void test1(){
        QueryOfferInfoForm form = new QueryOfferInfoForm();
        form.setNames("美西");
        form.setSailTime(LocalDateTime.now());
        form.setCutOffTime(LocalDateTime.now());
        form.setDestinationPort("纽约州");

        TestUtils.JSONObjectPrint(form);

    }

}
