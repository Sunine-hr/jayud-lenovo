import com.jayud.mall.model.bo.TransportWayForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class TransportWayTest {

    /**
     * 查询运输方式
     */
    @Test
    public void test1(){
        TransportWayForm form = new TransportWayForm();
        form.setCodeName("海运");
        TestUtils.JSONObjectPrint(form);
    }


}
