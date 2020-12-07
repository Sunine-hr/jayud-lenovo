import com.jayud.mall.model.bo.QuotationTemplateForm;
import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.model.bo.TemplateFileForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuotationTemplateTest {

    /**
     * 构造保存json
     * <p>添加报价模板-整柜</p>
     */
    @Test
    public void test1(){
        Long id = 13L;

        QuotationTemplateForm form = new QuotationTemplateForm();
        form.setId(id);//自增id
        form.setTypes(1);//模板类型(1整柜 2散柜)
        form.setSid(1);//服务分类(service_group sid)
        form.setNames("美西-海卡大船A");//报价名
//        form.setPicUrl("url1,url2,url3");//报价图片，多张用逗号分割
        form.setTid(1);//运输方式(transport_way id)
        form.setStartShipment("上海");//起运港
        form.setDestinationPort("纽约");//目的港
//        form.setArriveWarehouse("1,2,3");//可达仓库(fab_warehouse.id),多个用逗号分隔
//        form.setVisibleUid("1,2,3");//可见客户(0所客户，多客户时逗号分隔用户ID)
        form.setSailTime(LocalDateTime.now());//开船日期
        form.setCutOffTime(LocalDateTime.now());//截单日期
        form.setJcTime(LocalDateTime.now());//截仓日期
        form.setJkcTime(LocalDateTime.now());//截亏仓日期
//        form.setGid("3,4,5");//货物类型(goods_type types=2 id),多个用逗号分隔
                        //3     带磁      普货
                        //4     带电      普货
                        //5     粉末      普货
//        form.setAreaId("1,2");//集货仓库(shipping_area id),多个都号分隔           内陆费的规格项
                        //1 苏州仓
                        //2 上海仓
//        form.setQid("14,15,16");//报价类型(goods_type types=1 id),多个用逗号分隔   海运费的规格项
                        //14    20GP    整柜
                        //15    40GP    整柜
                        //16    40HC    整柜
        form.setTaskId(1);//任务分组id(task_group id)
        form.setRemarks("我是操作信息");//操作信息
        form.setStatus("1");//状态(0无效 1有效)
        form.setUserId(1);
        form.setUserName("admin");
        form.setCreateTime(LocalDateTime.now());
        form.setUpdateTime(LocalDateTime.now());

        /*应收费用明细List*/
        List<TemplateCopeReceivableForm> templateCopeReceivableFormList = getTemplateCopeReceivableFormList((id != null) ? id.intValue() : null);
        form.setTemplateCopeReceivableFormList(templateCopeReceivableFormList);

        /*应付费用明细list*/
        List<TemplateCopeWithForm> templateCopeWithFormList = getTemplateCopeWithFormList((id != null) ? id.intValue() : null);
        form.setTemplateCopeWithFormList(templateCopeWithFormList);

        /*文件信息明细list*/
        List<TemplateFileForm> templateFileFormList = getTemplateFileFormList((id != null) ? id.intValue() : null);
        form.setTemplateFileFormList(templateFileFormList);

//        TestUtils.ObjectMapperPrint(form);
        TestUtils.JSONObjectPrint(form);

    }

    /**
     * 获取应收费用明细List
     * @param qie   报价模板id
     * @return
     */
    public List<TemplateCopeReceivableForm> getTemplateCopeReceivableFormList(Integer qie){
        List<TemplateCopeReceivableForm> list = new ArrayList<>();
        //海运费
        TemplateCopeReceivableForm hyf1 = new TemplateCopeReceivableForm();
//        hyf1.setId(1L);
        hyf1.setQie(qie);
        hyf1.setCostName("海运费");
        hyf1.setSpecificationCode("20GP");
        hyf1.setCalculateWay(1);
        hyf1.setCount(15);
        hyf1.setUnit(1);
        hyf1.setSource(1);
        hyf1.setUnitPrice(new BigDecimal(2000.00));
        hyf1.setCid(1);
        hyf1.setAmount(new BigDecimal(2000.00));
        hyf1.setRemarks("海运费");
        list.add(hyf1);

        TemplateCopeReceivableForm hyf2 = new TemplateCopeReceivableForm();
//        hyf2.setId(2L);
        hyf2.setQie(qie);
        hyf2.setCostName("海运费");
        hyf2.setSpecificationCode("40GP");
        hyf2.setCalculateWay(1);
        hyf2.setCount(15);
        hyf2.setUnit(1);
        hyf2.setSource(1);
        hyf2.setUnitPrice(new BigDecimal(2000.00));
        hyf2.setCid(1);
        hyf2.setAmount(new BigDecimal(2000.00));
        hyf2.setRemarks("海运费");
        list.add(hyf2);

        TemplateCopeReceivableForm hyf3 = new TemplateCopeReceivableForm();
//        hyf3.setId(3L);
        hyf3.setQie(qie);
        hyf3.setCostName("海运费");
        hyf3.setSpecificationCode("40HC");
        hyf3.setCalculateWay(1);
        hyf3.setCount(15);
        hyf3.setUnit(1);
        hyf3.setSource(1);
        hyf3.setUnitPrice(new BigDecimal(2000.00));
        hyf3.setCid(1);
        hyf3.setAmount(new BigDecimal(2000.00));
        hyf3.setRemarks("海运费");
        list.add(hyf3);

        //内陆费
        TemplateCopeReceivableForm nlf1 = new TemplateCopeReceivableForm();
//        nlf1.setId(4L);
        nlf1.setQie(qie);
        nlf1.setCostName("内陆费");
        nlf1.setSpecificationCode("苏州仓");
        nlf1.setCalculateWay(1);
        nlf1.setCount(15);
        nlf1.setUnit(1);
        nlf1.setSource(1);
        nlf1.setUnitPrice(new BigDecimal(2000.00));
        nlf1.setCid(1);
        nlf1.setAmount(new BigDecimal(2000.00));
        nlf1.setRemarks("内陆费");
        list.add(nlf1);

        TemplateCopeReceivableForm nlf2 = new TemplateCopeReceivableForm();
//        nlf2.setId(5L);
        nlf2.setQie(qie);
        nlf2.setCostName("内陆费");
        nlf2.setSpecificationCode("上海仓");
        nlf2.setCalculateWay(1);
        nlf2.setCount(15);
        nlf2.setUnit(1);
        nlf2.setSource(1);
        nlf2.setUnitPrice(new BigDecimal(2000.00));
        nlf2.setCid(1);
        nlf2.setAmount(new BigDecimal(2000.00));
        nlf2.setRemarks("内陆费");
        list.add(nlf2);
        return list;
    }

    /**
     * 获取应付费用明细list
     * @param qie   报价模板id
     * @return
     */
    private List<TemplateCopeWithForm> getTemplateCopeWithFormList(Integer qie) {
        List<TemplateCopeWithForm> list = new ArrayList<>();
        //订船费
        TemplateCopeWithForm form1 = new TemplateCopeWithForm();
//        form1.setId(1L);
        form1.setQie(qie);
        form1.setCostName("订船费");
        form1.setSupplierCode("xx供应商");
        form1.setCalculateWay(1);
        form1.setCount(1);
        form1.setUnit(1);
        form1.setSource(1);
        form1.setUnitPrice(new BigDecimal(1000.00));
        form1.setCid(1);
        form1.setAmount(new BigDecimal(1000.00));
        form1.setRemarks("订船费");
        list.add(form1);
        //仓库服务费
        TemplateCopeWithForm form2 = new TemplateCopeWithForm();
//        form2.setId(1L);
        form2.setQie(qie);
        form2.setCostName("仓库服务费");
        form2.setSupplierCode("xx供应商");
        form2.setCalculateWay(1);
        form2.setCount(1);
        form2.setUnit(1);
        form2.setSource(1);
        form2.setUnitPrice(new BigDecimal(1000.00));
        form2.setCid(1);
        form2.setAmount(new BigDecimal(1000.00));
        form2.setRemarks("仓库服务费");
        list.add(form2);
        //上门提货费
        TemplateCopeWithForm form3 = new TemplateCopeWithForm();
//        form3.setId(3L);
        form3.setQie(qie);
        form3.setCostName("上门提货费");
        form3.setSupplierCode("xx供应商");
        form3.setCalculateWay(1);
        form3.setCount(1);
        form3.setUnit(1);
        form3.setSource(1);
        form3.setUnitPrice(new BigDecimal(1000.00));
        form3.setCid(1);
        form3.setAmount(new BigDecimal(1000.00));
        form3.setRemarks("上门提货费");
        list.add(form3);
        return list;
    }

    /**
     * 获取文件信息明细list
     * @param qie
     * @return
     */
    private List<TemplateFileForm> getTemplateFileFormList(Integer qie) {
        List<TemplateFileForm> list = new ArrayList<>();

        /**报关服务**/

        //电子委托书
        TemplateFileForm form1 = new TemplateFileForm();
//        form1.setId(1L);
        form1.setQie(qie);
        form1.setFileName("电子委托书");
        form1.setOptions(1);
        form1.setRemarks("买单报关");
        form1.setQfId(1);
        list.add(form1);

        //装箱清单
        TemplateFileForm form2 = new TemplateFileForm();
//        form2.setId(2L);
        form2.setQie(qie);
        form2.setFileName("装箱清单");
        form2.setOptions(1);
        form2.setRemarks("买单报关");
        form2.setQfId(2);
        list.add(form2);

        //出入境证明
        TemplateFileForm form3 = new TemplateFileForm();
//        form3.setId(3L);
        form3.setQie(qie);
        form3.setFileName("出入境证明");
        form3.setOptions(1);
        form3.setRemarks("买单报关");
        form3.setQfId(3);
        list.add(form3);


        /**********************************************/
        //电子委托书
        TemplateFileForm form4 = new TemplateFileForm();
//        form1.setId(1L);
        form4.setQie(qie);
        form4.setFileName("电子委托书");
        form4.setOptions(1);
        form4.setRemarks("独立报关");
        form4.setQfId(4);
        list.add(form4);

        //装箱清单
        TemplateFileForm form5 = new TemplateFileForm();
//        form2.setId(2L);
        form5.setQie(qie);
        form5.setFileName("装箱清单");
        form5.setOptions(1);
        form5.setRemarks("独立报关");
        form5.setQfId(5);
        list.add(form5);

        //出入境证明
        TemplateFileForm form6 = new TemplateFileForm();
//        form3.setId(3L);
        form6.setQie(qie);
        form6.setFileName("出入境证明");
        form6.setOptions(1);
        form6.setRemarks("独立报关");
        form6.setQfId(6);
        list.add(form6);

        /**清关服务**/
        //电子委托书
        TemplateFileForm form7 = new TemplateFileForm();
//        form1.setId(1L);
        form7.setQie(qie);
        form7.setFileName("电子委托书");
        form7.setOptions(1);
        form7.setRemarks("买单报关");
        form7.setQfId(7);
        list.add(form7);

        //装箱清单
        TemplateFileForm form8 = new TemplateFileForm();
//        form2.setId(2L);
        form8.setQie(qie);
        form8.setFileName("装箱清单");
        form8.setOptions(1);
        form8.setRemarks("买单报关");
        form8.setQfId(8);
        list.add(form8);

        //出入境证明
        TemplateFileForm form9 = new TemplateFileForm();
//        form3.setId(3L);
        form9.setQie(qie);
        form9.setFileName("出入境证明");
        form9.setOptions(1);
        form9.setRemarks("买单报关");
        form9.setQfId(9);
        list.add(form9);


        /**********************************************/
        //电子委托书
        TemplateFileForm form10 = new TemplateFileForm();
//        form1.setId(1L);
        form10.setQie(qie);
        form10.setFileName("电子委托书");
        form10.setOptions(1);
        form10.setRemarks("独立报关");
        form10.setQfId(10);
        list.add(form10);

        //装箱清单
        TemplateFileForm form11 = new TemplateFileForm();
//        form2.setId(2L);
        form11.setQie(qie);
        form11.setFileName("装箱清单");
        form11.setOptions(1);
        form11.setRemarks("独立报关");
        form11.setQfId(11);
        list.add(form11);

        //出入境证明
        TemplateFileForm form12 = new TemplateFileForm();
//        form3.setId(3L);
        form12.setQie(qie);
        form12.setFileName("出入境证明");
        form12.setOptions(1);
        form12.setRemarks("独立报关");
        form12.setQfId(12);
        list.add(form12);

        return list;
    }


    @Test
    public void test2(){
        Long id = null;
        System.out.println((id != null) ? id.intValue() : null);
    }


}
