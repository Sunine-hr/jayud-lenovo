import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class OperationTeamTest {

    @Test
    public void test1(){
        OperationTeamForm form = new OperationTeamForm();
        form.setGroupCode("YYFW01");
        TestUtils.JSONObjectPrint(form);

    }

}
