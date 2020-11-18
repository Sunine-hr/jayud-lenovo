import com.jayud.mall.model.bo.OfferInfoForm;
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

    @Test
    public void test2(){
        OfferInfoForm form = new OfferInfoForm();
        form.setId(1L);
        form.setQie(1);
        form.setNames("美西");
        form.setSailTime(LocalDateTime.now());
        form.setCutOffTime(LocalDateTime.now());
        form.setJcTime(LocalDateTime.now());
        form.setJkcTime(LocalDateTime.now());
        form.setTypes(1);
        form.setStatus("1");
        form.setUserId(1);
        form.setUserName("admin");
        form.setCreateTime(LocalDateTime.now());

        TestUtils.JSONObjectPrint(form);
    }


}
