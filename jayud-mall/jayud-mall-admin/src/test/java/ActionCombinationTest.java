import com.jayud.mall.model.bo.ActionCombinationForm;
import com.jayud.mall.model.po.ActionCombinationItemRelation;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ActionCombinationTest {

    @Test
    public void test1(){
        ActionCombinationForm form = new ActionCombinationForm();
        form.setCombinationName("海卡大船操作步骤");

        List<ActionCombinationItemRelation> list = new ArrayList<>();
        ActionCombinationItemRelation actionCombinationItemRelation = new ActionCombinationItemRelation();
        actionCombinationItemRelation.setActionItemId(1);
        actionCombinationItemRelation.setPrefixDeclare(1);
        actionCombinationItemRelation.setTimeNumber(12);
        actionCombinationItemRelation.setTimeUnit(1);
        actionCombinationItemRelation.setSort(1);
        list.add(actionCombinationItemRelation);

        form.setActionCombinationItemRelationList(list);

        TestUtils.JSONObjectPrint(form);
    }
}
