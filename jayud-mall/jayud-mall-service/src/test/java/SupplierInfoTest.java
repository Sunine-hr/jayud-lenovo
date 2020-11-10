import com.jayud.mall.model.bo.QuerySupplierInfoForm;
import com.jayud.mall.model.bo.SupplierInfoForm;
import org.junit.Test;

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


}
