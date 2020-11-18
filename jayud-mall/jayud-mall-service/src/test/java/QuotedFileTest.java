import com.jayud.mall.model.bo.QuotedFileForm;
import org.junit.Test;

public class QuotedFileTest {

    @Test
    public void test1(){
        QuotedFileForm form = new QuotedFileForm();
        form.setId(1L);
        form.setGroupCode("A");
        form.setIdCode("A01");
        form.setFileName("电子委托书");
        form.setOptions(1);
        form.setIsCheck(1);
        form.setDescribe("电子委托书...");
        form.setStatus("1");
        TestUtils.ObjectMapperPrint(form);
    }

}