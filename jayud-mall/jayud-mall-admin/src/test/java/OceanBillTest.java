import com.jayud.mall.model.bo.*;
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
        //form.setSupplierCode("HEUNG-A");
        //form.setEndCode("SH");
        TestUtils.JSONObjectPrint(form);

    }

    @Test
    public void test2(){
        Long id = 1L;
        OceanBillForm form = new OceanBillForm();
        form.setId(id);
        form.setTid(1);
//        form.setSupplierCode("HEUNG-A");
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
//        form1.setCreateTime(LocalDateTime.now());
//
//        List<OceanCounterCustomerRelation> list1 = new ArrayList<>();
//
//        OceanCounterCustomerRelation oceanCounterCustomerRelation1 = new OceanCounterCustomerRelation();
//        oceanCounterCustomerRelation1.setCustomerId(1L);
//        oceanCounterCustomerRelation1.setOrderCaseId(1L);
//        list1.add(oceanCounterCustomerRelation1);
//
//        OceanCounterCustomerRelation oceanCounterCustomerRelation2 = new OceanCounterCustomerRelation();
//        oceanCounterCustomerRelation2.setCustomerId(1L);
//        oceanCounterCustomerRelation2.setOrderCaseId(2L);
//        list1.add(oceanCounterCustomerRelation2);

//        form1.setOceanCounterCustomerRelationList(list1);

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

//        List<OceanCounterCustomerRelation> list2 = new ArrayList<>();

//        OceanCounterCustomerRelation oceanCounterCustomerRelation11 = new OceanCounterCustomerRelation();
//        oceanCounterCustomerRelation11.setCustomerId(1L);
//        oceanCounterCustomerRelation11.setOrderCaseId(3L);
//        list2.add(oceanCounterCustomerRelation11);

//        OceanCounterCustomerRelation oceanCounterCustomerRelation12 = new OceanCounterCustomerRelation();
//        oceanCounterCustomerRelation12.setCustomerId(1L);
//        oceanCounterCustomerRelation12.setOrderCaseId(4L);
//        list2.add(oceanCounterCustomerRelation12);

//        form2.setOceanCounterCustomerRelationList(list2);

        oceanCounterForms.add(form2);

        return oceanCounterForms;
    }

    /**
     * ???????????????????????????????????????????????????N??????????????????????????????N????????????
     */
    @Test
    public void test3(){
        OceanBillForm form = new OceanBillForm();
        form.setId(1L);
        form.setTid(1);
//        form.setSupplierCode("G001");
        form.setOrderId("DL200228H011");
        form.setStartCode("SH");
        form.setEndCode("NY");
        form.setSailTime(LocalDateTime.now());
        form.setVoyageDay(20);
        form.setUnit(2);
        form.setCreateTime(LocalDateTime.now());

        //1????????????1??????(PS:?????????1:N)
        List<OceanCounterForm> oceanCounterForms = oceanCounterForms();
        form.setOceanCounterForms(oceanCounterForms);

        TestUtils.JSONObjectPrintNull(form);
    }

    /**
     * 1????????????1??????(PS:?????????1:N)
     * @return
     */
    public List<OceanCounterForm> oceanCounterForms(){

        //1????????????1??????(PS:?????????1:N)
        List<OceanCounterForm> oceanCounterForms = new ArrayList<>();
        OceanCounterForm form1 = new OceanCounterForm();
        form1.setId(null);
        form1.setCntrNo("A0001");
        form1.setCabinetCode("20GP");
        form1.setVolume(100.00);
        form1.setCost(new BigDecimal(1000.00));
        form1.setCid(1);
        form1.setStatus("1");
        form1.setObId(null);
        form1.setCreateTime(LocalDateTime.now());

//        //1????????????N??????
//        List<OceanWaybillForm> oceanWaybillFormList = oceanWaybillFormList();
//        form1.setOceanWaybillFormList(oceanWaybillFormList);

        oceanCounterForms.add(form1);

        return oceanCounterForms;
    }

//    /**
//     * 1????????????N??????
//     * @return
//     */
//    public List<OceanWaybillForm> oceanWaybillFormList(){
//        // 1????????????N??????
//        List<OceanWaybillForm> oceanWaybillFormList = new ArrayList<>();
//
//        OceanWaybillForm form1 = new OceanWaybillForm();
//        form1.setId(null);
//        form1.setWaybillNo("YS202006000132");
//        form1.setDescribe("?????????1");
//        form1.setShippingInformation("????????? ????????? ????????? ???????????????333???");
//        form1.setOceanCounterId(null);//????????????
//
//
//        //1????????????N??????
////        List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList = oceanWaybillCaseRelationFormList();
////        form1.setOceanWaybillCaseRelationFormList(oceanWaybillCaseRelationFormList);
//
//        oceanWaybillFormList.add(form1);
//
//        return oceanWaybillFormList;
//    }

//    /**
//     * 1????????????N??????
//     * @return
//     */
//    public List<OceanWaybillCaseRelationForm>  oceanWaybillCaseRelationFormList(){
//        //1????????????N??????
//        List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList = new ArrayList<>();
//        OceanWaybillCaseRelationForm form1 = new OceanWaybillCaseRelationForm();
//
//        form1.setId(null);
//        form1.setOceanWaybillId(null);
//        form1.setOrderCaseId(1L);
//        form1.setCustomerId(3L);
//        oceanWaybillCaseRelationFormList.add(form1);
//
//        return oceanWaybillCaseRelationFormList;
//    }

    /**
     * ????????????json
     */
    @Test
    public void test4(){
        OceanBillForm form = new OceanBillForm();
        form.setId(1L);
        form.setTid(1);
//        form.setSupplierCode("G001");
        form.setOrderId("DL200228H011");
        form.setStartCode("SH");
        form.setEndCode("NY");
        form.setSailTime(LocalDateTime.now());
        form.setVoyageDay(20);
        form.setUnit(2);
        form.setCreateTime(LocalDateTime.now());
        TestUtils.JSONObjectPrintNull(form);

    }

    @Test
    public void test5(){

        OceanBillForm form = new OceanBillForm();
        form.setId(1L);
        form.setTid(1);
//        form.setSupplierCode("G001");
        form.setOrderId("DL200228H011");
        form.setStartCode("SH");
        form.setEndCode("NY");
        form.setSailTime(LocalDateTime.now());
        form.setVoyageDay(20);
        form.setUnit(2);
        form.setCreateTime(LocalDateTime.now());
        form.setTaskId(1);//1	TD001	???????????????1	1	1

        List<OceanCounterForm> oceanCounterForms = new ArrayList<>();
        OceanCounterForm form1 = new OceanCounterForm();
        form1.setId(null);
        form1.setCntrNo("A0001");
        form1.setCabinetCode("20GP");
        form1.setVolume(100.00);
        form1.setCost(new BigDecimal("1000.00"));
        form1.setCid(1);
        form1.setStatus("1");
        form1.setObId(null);
        form1.setCreateTime(LocalDateTime.now());
        oceanCounterForms.add(form1);
        form.setOceanCounterForms(oceanCounterForms);

        TestUtils.JSONObjectPrintNull(form);
    }



}
