import com.jayud.common.enums.OrderEnum;
import com.jayud.mall.model.bo.OrderInfoForm;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.OrderShopVO;
import com.jayud.mall.utils.TestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoTest {

    /**
     * 保存订单数据
     */
    @Test
    public void test1(){
        OrderInfoForm form = new OrderInfoForm();
        form.setId(null);

        form.setOrderNo(null);//订单号
        form.setCustomerId(1);
        form.setOfferInfoId(1);
        form.setStoreGoodsWarehouseCode("SH");
        form.setStoreGoodsWarehouseName("上海仓");
        form.setDestinationWarehouseCode("YMX1");
        form.setDestinationWarehouseName("亚马逊1仓");
        form.setIsPick(1);//是否上门提货(0否 1是,order_pick)
        form.setStatus(OrderEnum.DRAFT.getCode());
        form.setStatusName(OrderEnum.DRAFT.getName());
        form.setCreateUserId(null);
        form.setCreateUserName(null);
        form.setOrderOrigin("1");//订单来源，默认为1，web端；
        form.setBolNo(null);//提单号
        form.setNeedDeclare(1);
        form.setNeedClearance(1);
        form.setRemark("我是订单备注，备注一下，我是中国工商银行，ICBC，爱存不存。");

        /*订单对应箱号信息:order_case*/
        List<OrderCaseVO> orderCaseVOList = createOrderCaseVOList();
        form.setOrderCaseVOList(orderCaseVOList);

        /*订单对应商品：order_shop*/
        List<OrderShopVO> orderShopVOList = createOrderShopVOList();
        form.setOrderShopVOList(orderShopVOList);

        /*订单对应提货信息表：order_pick*/
        List<OrderPickVO> orderPickVOList = createOrderPickVOList();
        form.setOrderPickVOList(orderPickVOList);

        TestUtils.JSONObjectPrint(form);

    }




    private List<OrderCaseVO> createOrderCaseVOList() {
        List<OrderCaseVO> list = new ArrayList<>();

        OrderCaseVO orderCaseVO1 = new OrderCaseVO();
        orderCaseVO1.setOrderId(null);
        orderCaseVO1.setCartonNo("XH202006170001");
        orderCaseVO1.setFabNo("204364");
        orderCaseVO1.setAsnLength(new BigDecimal("125.00"));
        orderCaseVO1.setAsnWidth(new BigDecimal("125.00"));
        orderCaseVO1.setAsnHeight(new BigDecimal("124.00"));
        orderCaseVO1.setAsnWeight(new BigDecimal("124.89"));
        orderCaseVO1.setAsnVolume(new BigDecimal("1.24"));
        orderCaseVO1.setAsnWeighDate(LocalDateTime.now());
        list.add(orderCaseVO1);

        OrderCaseVO orderCaseVO2 = new OrderCaseVO();
        orderCaseVO2.setOrderId(null);
        orderCaseVO2.setCartonNo("XH202006170002");
        orderCaseVO2.setFabNo("204365");
        orderCaseVO2.setAsnLength(new BigDecimal("125.00"));
        orderCaseVO2.setAsnWidth(new BigDecimal("125.00"));
        orderCaseVO2.setAsnHeight(new BigDecimal("124.00"));
        orderCaseVO2.setAsnWeight(new BigDecimal("124.89"));
        orderCaseVO2.setAsnVolume(new BigDecimal("1.24"));
        orderCaseVO2.setAsnWeighDate(LocalDateTime.now());
        list.add(orderCaseVO2);

        return list;
    }

    private List<OrderShopVO> createOrderShopVOList() {
        List<OrderShopVO> list = new ArrayList<>();

        OrderShopVO orderShopVO1 = new OrderShopVO();
        orderShopVO1.setOrderId(null);
        orderShopVO1.setGoodId(6);
        orderShopVO1.setQuantity(2);
        orderShopVO1.setCreateTime(LocalDateTime.now());
        list.add(orderShopVO1);

        OrderShopVO orderShopVO2 = new OrderShopVO();
        orderShopVO2.setOrderId(null);
        orderShopVO2.setGoodId(7);
        orderShopVO2.setQuantity(2);
        orderShopVO2.setCreateTime(LocalDateTime.now());
        list.add(orderShopVO2);

        OrderShopVO orderShopVO3 = new OrderShopVO();
        orderShopVO3.setOrderId(null);
        orderShopVO3.setGoodId(8);
        orderShopVO3.setQuantity(2);
        orderShopVO3.setCreateTime(LocalDateTime.now());
        list.add(orderShopVO3);

        return list;
    }

    private List<OrderPickVO> createOrderPickVOList() {
        List<OrderPickVO> list = new ArrayList<>();

        OrderPickVO orderPickVO1 = new OrderPickVO();
        orderPickVO1.setOrderId(null);
        orderPickVO1.setPickNo(null);
        orderPickVO1.setWarehouseNo("20201100001");
        orderPickVO1.setPickTime(LocalDateTime.of(2020, 12, 12, 15, 15, 15, 123));
        orderPickVO1.setWeight(new BigDecimal("12.55"));
        orderPickVO1.setVolume(new BigDecimal("12.55"));
        orderPickVO1.setTotalCarton(4);
        orderPickVO1.setRemark("我是备注，也是中国工商银行，ICBC，爱存不存。");
        orderPickVO1.setAddressId(1);
        orderPickVO1.setCreateTime(LocalDateTime.now());
        list.add(orderPickVO1);

        OrderPickVO orderPickVO2 = new OrderPickVO();
        orderPickVO2.setOrderId(null);
        orderPickVO2.setPickNo(null);
        orderPickVO2.setWarehouseNo("20201100002");
        orderPickVO2.setPickTime(LocalDateTime.of(2020, 12, 12, 15, 15, 15, 123));
        orderPickVO2.setWeight(new BigDecimal("12.55"));
        orderPickVO2.setVolume(new BigDecimal("12.55"));
        orderPickVO2.setTotalCarton(4);
        orderPickVO2.setRemark("我是备注，也是中国工商银行，ICBC，爱存不存。");
        orderPickVO2.setAddressId(2);
        orderPickVO2.setCreateTime(LocalDateTime.now());
        list.add(orderPickVO2);


        return list;
    }

    @Test
    public void test2(){
        LocalDateTime of = LocalDateTime.of(2018, 1, 13, 9, 43, 20, 123);
        System.out.println(of);
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }

    /**
     * 草稿-提交和取消
     */
    @Test
    public void test3(){
        OrderInfoForm form = new OrderInfoForm();
        form.setId(1L);
        TestUtils.JSONObjectPrint(form);

    }


}
