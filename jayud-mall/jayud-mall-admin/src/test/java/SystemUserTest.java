import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SystemUserTest {

    @Test
    public void test1(){

        SaveSystemUserForm form = new SaveSystemUserForm();
        form.setId(161L);
        form.setName("张少娜");
        form.setCompany("佳裕达xxx公司");
        form.setWkno("520");
        form.setEnUserName("Zhang Shaona");
        form.setPhone("18900008989");
        form.setEmail("18900008989@qq.com");
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        form.setRoleIds(roleIds);
        TestUtils.JSONObjectPrint(form);

    }
}
