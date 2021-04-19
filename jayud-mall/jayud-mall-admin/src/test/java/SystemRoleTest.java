import com.jayud.mall.model.bo.SaveRoleForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SystemRoleTest {

    @Test
    public void test1(){
        SaveRoleForm form = new SaveRoleForm();
        form.setId(1L);
        form.setRoleName("管理员");
        form.setRoleDescribe("管理员，管理系统");
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L);
        form.setMenuIds(menuIds);
        TestUtils.JSONObjectPrint(form);

    }
}
