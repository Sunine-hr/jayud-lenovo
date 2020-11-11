import com.jayud.mall.model.bo.OceanBillForm;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.bo.QueryOceanBillForm;
import com.jayud.mall.model.po.OceanCounterCustomerRelation;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OceanBillTest {

    @Test
    public void test1(){
        QueryOceanBillForm form = new QueryOceanBillForm();
        form.setOrderId("A00000000001");
        form.setSailTime(LocalDateTime.now());
        form.setSupplierCode("HEUNG-A");
        form.setEndCode("SH");
        TestUtils.JSONObjectPrint(form);

    }

    @Test
    public void test2(){
        Long id = 1L;
        OceanBillForm form = new OceanBillForm();
        form.setId(id);
        form.setTid(1);
        form.setSupplierCode("HEUNG-A");
        form.setOrderId("AB123");
        form.setStartCode("SH");
        form.setEndCode("NY");
        form.setSailTime(LocalDateTime.now());
        form.setVoyageDay(10);
        form.setUnit(2);
        form.setCreateTime(LocalDateTime.now());
        List<OceanCounterForm> oceanCounterForms = getOceanCounterForms(id);
        form.setOceanCounterForms(oceanCounterForms);
        TestUtils.JSONObjectPrint(form);
    }

    public List<OceanCounterForm> getOceanCounterForms(Long oceanCounterId){
        List<OceanCounterForm> oceanCounterForms = new ArrayList<>();
        OceanCounterForm form1 = new OceanCounterForm();
        form1.setId(1L);
        form1.setCntrNo("A0001");
        form1.setCabinetCode("20GP");
        form1.setVolume(100.00);
        form1.setCost(new BigDecimal(1000.00));
        form1.setCid(1);
        form1.setStatus("1");
        form1.setObId(oceanCounterId);
        form1.setCreateTime(LocalDateTime.now());

        List<OceanCounterCustomerRelation> list1 = new ArrayList<>();

        OceanCounterCustomerRelation oceanCounterCustomerRelation1 = new OceanCounterCustomerRelation();
        oceanCounterCustomerRelation1.setCustomerId(1L);
        oceanCounterCustomerRelation1.setOrderCaseId(1L);
        list1.add(oceanCounterCustomerRelation1);

        OceanCounterCustomerRelation oceanCounterCustomerRelation2 = new OceanCounterCustomerRelation();
        oceanCounterCustomerRelation2.setCustomerId(1L);
        oceanCounterCustomerRelation2.setOrderCaseId(2L);
        list1.add(oceanCounterCustomerRelation2);

        form1.setOceanCounterCustomerRelationList(list1);

        oceanCounterForms.add(form1);

        OceanCounterForm form2 = new OceanCounterForm();
        form2.setId(2L);
        form2.setCntrNo("A0002");
        form2.setCabinetCode("40GP");
        form2.setVolume(100.00);
        form2.setCost(new BigDecimal(1000.00));
        form2.setCid(1);
        form2.setStatus("1");
        form2.setObId(oceanCounterId);
        form2.setCreateTime(LocalDateTime.now());

        List<OceanCounterCustomerRelation> list2 = new ArrayList<>();

        OceanCounterCustomerRelation oceanCounterCustomerRelation11 = new OceanCounterCustomerRelation();
        oceanCounterCustomerRelation11.setCustomerId(1L);
        oceanCounterCustomerRelation11.setOrderCaseId(3L);
        list2.add(oceanCounterCustomerRelation11);

        OceanCounterCustomerRelation oceanCounterCustomerRelation12 = new OceanCounterCustomerRelation();
        oceanCounterCustomerRelation12.setCustomerId(1L);
        oceanCounterCustomerRelation12.setOrderCaseId(4L);
        list2.add(oceanCounterCustomerRelation12);

        form2.setOceanCounterCustomerRelationList(list2);

        oceanCounterForms.add(form2);

        return oceanCounterForms;
    }



}
