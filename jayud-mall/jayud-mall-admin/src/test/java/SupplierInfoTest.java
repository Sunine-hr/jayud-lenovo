import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SupplierInfoTest {

    @Test
    public void test1(){
        QuerySupplierInfoForm form = new QuerySupplierInfoForm();
        form.setKeyword("HEUNG-A");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test2(){
        QuerySupplierInfoForm form = new QuerySupplierInfoForm();
        form.setSupplierChName("EMC");
        form.setSupplierEnName("EMC");
        form.setStatus("1");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test3(){
        SupplierInfoForm form = new SupplierInfoForm();
        form.setId(1L);
        form.setSupplierCode("HEUNG-A");
        form.setSupplierChName("EMC");
        form.setSupplierEnName("EMC");
        form.setContacts("张三");
        form.setCompanyName("鹏飞运输公司");
        form.setContactNumber("0755-17177878");
        form.setContactPhone("13245678908");
        form.setAddressFirst("深圳，罗湖，110");
        form.setStatus("1");
        TestUtils.JSONObjectPrint(form);
    }


    @Test
    public void test4(){
        SupplierInfoForm form = new SupplierInfoForm();
        form.setCompanyName("xx物流有限公司");
        //TODO服务类型
        List<Long> serviceTypeIds = Arrays.asList(1L, 2L, 3L);
        form.setServiceTypeIds(serviceTypeIds);
        form.setContacts("张三三");
        form.setContactNumber("18900000001");
        form.setZipCode("518000");
        form.setContactAddress("xx省xx市xx街道");
        form.setGrade(5);
        form.setStatus("1");
        form.setEffectiveDate(LocalDateTime.now());
        form.setExpiryDate(LocalDateTime.now());
        TestUtils.JSONObjectPrint(form);
    }


}
