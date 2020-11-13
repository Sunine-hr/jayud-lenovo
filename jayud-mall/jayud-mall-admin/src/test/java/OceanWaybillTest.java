import com.jayud.mall.model.bo.OceanWaybillForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OceanWaybillTest {

    @Test
    public void test1(){
        OceanWaybillForm form = new OceanWaybillForm();
        form.setOceanCounterId(1);

        List<OceanWaybillForm> oceanWaybillFormList = new ArrayList<>();

        OceanWaybillForm form1 = new OceanWaybillForm();
        form1.setId(1L);
        form1.setWaybillNo("YS000001");
        form1.setDescribe("运单");
        form1.setShippingInformation("上海市 杨浦区 沪青平公路333号");
        oceanWaybillFormList.add(form1);

        OceanWaybillForm form2 = new OceanWaybillForm();
        form2.setId(2L);
        form2.setWaybillNo("YS000002");
        form2.setDescribe("运单");
        form2.setShippingInformation("上海市 杨浦区 沪青平公路333号");
        oceanWaybillFormList.add(form2);

        form.setOceanWaybillFormList(oceanWaybillFormList);

        TestUtils.JSONObjectPrint(form);


    }

}
