import com.jayud.mall.model.bo.OceanWaybillCaseRelationForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OceanWaybillCaseRelationTest {

    @Test
    public void test1(){
        OceanWaybillCaseRelationForm form = new OceanWaybillCaseRelationForm();
        form.setOceanWaybillId(1L);

        List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList = new ArrayList<>();

        OceanWaybillCaseRelationForm form1 = new OceanWaybillCaseRelationForm();
        form1.setOrderCaseId(1L);
        form1.setCustomerId(1L);
        oceanWaybillCaseRelationFormList.add(form1);

        OceanWaybillCaseRelationForm form2 = new OceanWaybillCaseRelationForm();
        form2.setOrderCaseId(2L);
        form2.setCustomerId(1L);
        oceanWaybillCaseRelationFormList.add(form2);

        form.setOceanWaybillCaseRelationFormList(oceanWaybillCaseRelationFormList);

        TestUtils.JSONObjectPrint(form);
    }

    //修改的值
    List<Long> ids = new ArrayList<Long>(){{
        this.add(1L);
        this.add(2L);
        this.add(3L);
    }};


    //数据库原本的值
    List<Long> dbIds = new ArrayList<Long>(){{
        this.add(1L);
        this.add(3L);
        this.add(4L);
    }};


    /**
     * ids.retainAll(dbIds)
     * ids - dbIds 的 交集  1,3
     */
    @Test
    public void test2(){
        //ids - dbIds 的 交集  1,3
        boolean b = ids.retainAll(dbIds);
        ids.forEach(System.out::println); // 1,3 取的交集
        System.err.println("------");
        dbIds.forEach(System.out::println);// 1,3,4 无变化

    }

    /**
     * ids.removeAll(dbIds)
     * ids - dbIds 的 差集  2
     */
    @Test
    public void test3(){
        boolean b = ids.removeAll(dbIds);
        ids.forEach(System.out::println); // 2 取的差集
        System.err.println("------");
        dbIds.forEach(System.out::println);// 1,3,4 无变化
    }



}
