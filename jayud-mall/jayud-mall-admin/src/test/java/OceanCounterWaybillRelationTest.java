import com.jayud.mall.model.bo.OceanCounterWaybillRelationForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OceanCounterWaybillRelationTest {

    @Test
    public void test1(){
        List<OceanCounterWaybillRelationForm> list = new ArrayList<>();
        OceanCounterWaybillRelationForm form = new OceanCounterWaybillRelationForm();
        form.setId(null);
        form.setOceanCounterId(5L);
        form.setOrderInfoId(1L);
        list.add(form);
        TestUtils.JSONObjectPrint(list);

    }

}
