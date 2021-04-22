import com.jayud.mall.model.bo.QueryOfferInfoFareForm;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

public class OfferInfoTest {

    /**
     * 查询运价（报价）分页
     */
    @Test
    public void test1(){
        QueryOfferInfoFareForm fareForm = new QueryOfferInfoFareForm();
        fareForm.setStateCode("US");
        fareForm.setTid(1);
        fareForm.setTypes(1);
        TestUtils.JSONObjectPrint(fareForm);


    }

}
