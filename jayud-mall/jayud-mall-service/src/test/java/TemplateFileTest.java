import com.jayud.mall.model.bo.TemplateFileForm;
import org.junit.Test;

public class TemplateFileTest {

    @Test
    public void test1(){
        TemplateFileForm form = new TemplateFileForm();
        form.setId(1L);
        form.setQie(1);
        form.setFileName("电子委托书");
        form.setOptions(1);
        form.setRemarks("电子委托书");
        TestUtils.ObjectMapperPrint(form);
    }

}
