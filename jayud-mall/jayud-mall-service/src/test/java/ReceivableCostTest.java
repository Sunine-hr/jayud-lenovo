import com.jayud.mall.model.bo.ReceivableCostForm;
import org.junit.Test;

public class ReceivableCostTest {

    @Test
    public void test1(){
        ReceivableCostForm form = new ReceivableCostForm();
        form.setId(1L);
        form.setIdCode("YS01");
        form.setCostName("海运费");
        form.setIdentifying(1);
        form.setStatus("1");
        TestUtils.ObjectMapperPrint(form);
    }

}
