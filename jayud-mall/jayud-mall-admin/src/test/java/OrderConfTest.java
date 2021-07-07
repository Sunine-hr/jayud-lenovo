import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OceanConfDetail;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderConfTest {

    @Test
    public void test1(){
        QueryOrderConfForm form = new QueryOrderConfForm();
        form.setOrderNo("PZ202006SEA001");
        //form.setHarbourCode("NY");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test2(){
        Long id = 1L;
        OrderConfForm form = new OrderConfForm();
        form.setId(id);
        form.setOrderNo("PZ202006SEA001");
        form.setTid(1);
        //form.setHarbourCode("NY");
//        form.setStatus("1");
        form.setUserId(1);
        form.setUserName("admin");
        form.setCreateTime(LocalDateTime.now());
        List<OceanConfDetail> oceanConfDetailList = getOceanConfDetailList(id);
        //form.setOceanConfDetailList(oceanConfDetailList);
        TestUtils.JSONObjectPrint(form);
    }

    public List<OceanConfDetail> getOceanConfDetailList(Long orderId){
        List<OceanConfDetail> oceanConfDetailList = new ArrayList<>();
        //报价
        OceanConfDetail bj1 = new OceanConfDetail();
        bj1.setOrderId(orderId);
        bj1.setIdCode(1);
        bj1.setTypes(1);
        bj1.setStatus("1");
        oceanConfDetailList.add(bj1);

        OceanConfDetail bj2 = new OceanConfDetail();
        bj2.setOrderId(orderId);
        bj2.setIdCode(2);
        bj2.setTypes(1);
        bj2.setStatus("1");
        oceanConfDetailList.add(bj2);

        //提单
        OceanConfDetail td1 = new OceanConfDetail();
        td1.setOrderId(orderId);
        td1.setIdCode(1);
        td1.setTypes(2);
        td1.setStatus("1");
        oceanConfDetailList.add(td1);

        return oceanConfDetailList;
    }

    @Test
    public void test3(){
        OrderConfForm form = new OrderConfForm();
        form.setOrderNo("JYD-PZ-210100007");

        OceanConfDetail oceanConfDetail = new OceanConfDetail();
        oceanConfDetail.setIdCode(1);
        List<OceanConfDetail> offerInfoDetailList = new ArrayList<>();
        offerInfoDetailList.add(oceanConfDetail);
        form.setOfferInfoDetailList(offerInfoDetailList);


        OceanConfDetail oceanConfDetail1 = new OceanConfDetail();
        oceanConfDetail1.setIdCode(1);
        List<OceanConfDetail> oceanBillDetailList = new ArrayList<>();
        oceanBillDetailList.add(oceanConfDetail1);
        form.setOceanBillDetailList(oceanBillDetailList);

        TestUtils.JSONObjectPrint(form);


    }


}
