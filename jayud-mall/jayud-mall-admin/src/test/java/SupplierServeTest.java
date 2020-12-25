import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeForm;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupplierServeTest {

    @Test
    public void test1(){
        QuerySupplierServeForm form = new QuerySupplierServeForm();
        form.setServeName("海运40HQ报价");
//        form.setSupplierCode("G001");
//        form.setTransportPay(1);
        TestUtils.JSONObjectPrint(form);
    }

    @Test
    public void test2(){
        String supplierCode = "G001";
        String serveCode = "F001";

        SupplierServeForm form = new SupplierServeForm();
        form.setId(1L);
//        form.setSupplierCode(supplierCode);
        form.setServeCode(serveCode);
        form.setServeName("海运40HQ报价");
//        form.setTransportPay(1);
//        form.setStatus("1");
        form.setEffectiveDate(LocalDateTime.now());
        form.setExpiryDate(LocalDateTime.now());

        List<SupplierCostVO> supplierCostVOList = getSupplierCostVOList(supplierCode, serveCode);

        form.setSupplierCostVOList(supplierCostVOList);

        TestUtils.JSONObjectPrint(form);
    }

    public List<SupplierCostVO> getSupplierCostVOList(String supplierCode, String serveName){
        List<SupplierCostVO> supplierCostVOList = new ArrayList<>();

        SupplierCostVO supplierCostVO1 = new SupplierCostVO();
        supplierCostVO1.setSupplierCode(supplierCode);
        supplierCostVO1.setServeCode(serveName);
        supplierCostVO1.setCostCode("H0001");
        supplierCostVO1.setCostName("报关费");
        supplierCostVO1.setTid(1);
        supplierCostVO1.setCountWay(1);
        supplierCostVO1.setAmount(1);
        supplierCostVO1.setAmountUnit(1);
        supplierCostVO1.setAmountSource(1);
        supplierCostVO1.setUnitPrice(new BigDecimal(150.00));
        supplierCostVO1.setUnit(1);
        supplierCostVO1.setCid(1);
        supplierCostVO1.setStatus("1");
        supplierCostVOList.add(supplierCostVO1);

        SupplierCostVO supplierCostVO2 = new SupplierCostVO();
        supplierCostVO2.setSupplierCode(supplierCode);
        supplierCostVO2.setServeCode(serveName);
        supplierCostVO2.setCostCode("H0002");
        supplierCostVO2.setCostName("海运费");
        supplierCostVO2.setTid(1);
        supplierCostVO2.setCountWay(1);
        supplierCostVO2.setAmount(1);
        supplierCostVO2.setAmountUnit(2);
        supplierCostVO2.setAmountSource(1);
        supplierCostVO2.setUnitPrice(new BigDecimal(10.00));
        supplierCostVO2.setUnit(1);
        supplierCostVO2.setCid(1);
        supplierCostVO2.setStatus("1");
        supplierCostVOList.add(supplierCostVO2);

        SupplierCostVO supplierCostVO3 = new SupplierCostVO();
        supplierCostVO3.setSupplierCode(supplierCode);
        supplierCostVO3.setServeCode(serveName);
        supplierCostVO3.setCostCode("H0003");
        supplierCostVO3.setCostName("提货费");
        supplierCostVO3.setTid(1);
        supplierCostVO3.setCountWay(1);
        supplierCostVO3.setAmount(1);
        supplierCostVO3.setAmountUnit(3);
        supplierCostVO3.setAmountSource(2);
        supplierCostVO3.setUnitPrice(new BigDecimal(200.00));
        supplierCostVO3.setUnit(1);
        supplierCostVO3.setCid(1);
        supplierCostVO3.setStatus("1");
        supplierCostVOList.add(supplierCostVO3);


        return supplierCostVOList;
    }


}
