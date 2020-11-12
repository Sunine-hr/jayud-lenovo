import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OceanCounterTest {

    @Test
    public void test1(){
        OceanCounterForm form = new OceanCounterForm();
        form.setId(1L);
        form.setCntrNo("A0001");
        form.setCabinetCode("20GP");
        form.setVolume(100.00);
        form.setCost(new BigDecimal(1000.00));
        form.setCid(1);
        form.setStatus("1");
        form.setObId(1L);
        form.setCreateTime(LocalDateTime.now());

        TestUtils.JSONObjectPrint(form);

    }

    /**
     * 1提单对应1货柜(PS:之前是1提单对应N货柜)
     */
    @Test
    public void test2(){
        OceanCounterForm form = new OceanCounterForm();
        form.setObId(1L);

        List<OceanCounterForm> oceanCounterFormList = new ArrayList<>();
        OceanCounterForm form1 = new OceanCounterForm();
        form1.setId(1L);
        form1.setCntrNo("A0001");
        form1.setCabinetCode("20GP");
        form1.setVolume(100.00);
        form1.setCost(new BigDecimal(1000.00));
        form1.setCid(1);
        form1.setStatus("1");
        form1.setObId(1L);
        form1.setCreateTime(LocalDateTime.now());
        oceanCounterFormList.add(form1);

        form.setOceanCounterFormList(oceanCounterFormList);
        TestUtils.JSONObjectPrint(form);


    }


}
