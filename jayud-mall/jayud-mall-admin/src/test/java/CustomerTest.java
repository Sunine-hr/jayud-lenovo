import com.jayud.mall.model.bo.CustomerAuditForm;
import com.jayud.mall.model.bo.CustomerForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void test1(){
        CustomerForm form = new CustomerForm();
        form.setId(1);
        form.setCompany("深圳市坤鑫国际货运代理有限公司");
        TestUtils.JSONObjectPrint(form);


    }

    @Test
    public void test2(){
        CustomerForm form = new CustomerForm();
        form.setId(1);
        form.setAuditStatus(1);
        form.setSalesmanId(149);
//        form.setOperationTeamId(1L);
        form.setRemark("这个是我`吴提柜`审核的哈~");
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test3(){
        CustomerAuditForm form = new CustomerAuditForm();
        form.setId(3);
        form.setAuditStatus(1);//1审核通过
        form.setSalesmanId(1);
//        form.setOperationTeamId(1L);
        form.setRemark("审核备注A");
        TestUtils.JSONObjectPrint(form);
    }


}
