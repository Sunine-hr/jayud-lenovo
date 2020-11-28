import com.jayud.mall.model.bo.TaskMemberRelationForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class TaskMemberRelationTest {

    @Test
    public void test1(){
        TaskMemberRelationForm form = new TaskMemberRelationForm();
        form.setOperationTeamId(1L);
        TestUtils.JSONObjectPrint(form);


    }
}
