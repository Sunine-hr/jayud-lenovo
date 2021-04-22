import com.jayud.mall.model.bo.OperationTeamMemberForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class OperationTeamMemberTest {

    @Test
    public void test1(){
        OperationTeamMemberForm form = new OperationTeamMemberForm();
        form.setOperationTeamId(1L);
        TestUtils.JSONObjectPrint(form);


    }
}
