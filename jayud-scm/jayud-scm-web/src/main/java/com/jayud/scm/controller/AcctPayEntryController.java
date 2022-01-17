package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddAcctPayEntryForm;
import com.jayud.scm.model.bo.AddAcctPayForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctPay;
import com.jayud.scm.model.po.AcctPayEntry;
import com.jayud.scm.model.vo.AcctPayEntryVO;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.BookingOrderVO;
import com.jayud.scm.service.IAcctPayEntryService;
import com.jayud.scm.service.IAcctPayService;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.IBookingOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应付款表（付款单明细表） 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/acctPayEntry")
@Api(tags = "付款单明细管理")
@Slf4j
public class AcctPayEntryController {

    @Autowired
    private IAcctPayEntryService acctPayEntryService;

    @Autowired
    private IAcctPayService acctPayService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @ApiOperation(value = "新增应付款单")
    @PostMapping(value = "/saveOrUpdateAcctPayEntry")
    public CommonResult saveOrUpdateAcctPayEntry(@RequestBody AddAcctPayEntryForm form) {

        if(form.getPayId() != null){
            AcctPay acctPay = acctPayService.getById(form.getPayId());
            if(form.getApMoney().compareTo(acctPay.getApMoney()) == 1){
                return CommonResult.error(444,"应付款金额不能大于预付付款单金额");
            }
        }

        BookingOrderVO bookingOrderVO = bookingOrderService.getBookingOrderById(form.getOrderId());
        List<AcctPayEntry> acctPayEntries = acctPayEntryService.getAcctPayEntryByOrderId(bookingOrderVO.getId());
        BigDecimal acctPayEntrySum = new BigDecimal(0);
        BigDecimal bookingOrderSum = new BigDecimal(0);
        if(CollectionUtil.isNotEmpty(acctPayEntries)){
            for (AcctPayEntry acctPayEntry : acctPayEntries) {
                acctPayEntrySum = acctPayEntrySum.add(acctPayEntry.getApMoney());
            }
        }
        if(CollectionUtil.isNotEmpty(bookingOrderVO.getBookingOrderEntryList())){
            for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderVO.getBookingOrderEntryList()) {
                bookingOrderSum = bookingOrderSum.add(bookingOrderEntryVO.getTotalHgMoney());
            }
        }
        acctPayEntrySum = acctPayEntrySum.add(form.getApMoney());
        if(acctPayEntrySum.compareTo(bookingOrderSum) == 1){
            return CommonResult.error(444,"应付款金额总和已大于委托单报关总价格");
        }

        boolean result = acctPayEntryService.saveOrUpdateAcctPay(form);
        if(!result){
            return CommonResult.error(444,"添加或修改应付款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "删除应付款单")
    @PostMapping(value = "/deleteAcctPayEntry")
    public CommonResult deleteAcctPayEntry(@RequestBody DeleteForm form) {

        AcctPayEntry acctPayEntry = acctPayEntryService.getById(form.getId());
        if(acctPayEntry.getPayId() != null){
            return CommonResult.error(444,"该应付款单已经生成付款单，无法删除");
        }

        boolean result = acctPayEntryService.deleteAcctPayEntry(form);
        if(!result){
            return CommonResult.error(444,"删除应付款单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取应付款单详情")
    @PostMapping(value = "/getAcctPayEntryById")
    public CommonResult<AcctPayEntryVO> getAcctPayEntryById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        AcctPayEntry acctPayEntry = acctPayEntryService.getById(id);
        AcctPayEntryVO acctPayEntryVO = ConvertUtil.convert(acctPayEntry, AcctPayEntryVO.class);
        return CommonResult.success(acctPayEntryVO);
    }

    @ApiOperation(value = "付款")
    @PostMapping(value = "/payment")
    public CommonResult payment(@RequestBody QueryCommonForm form) {

        AcctPayEntry acctPayEntry = acctPayEntryService.getById(form.getId());
        if(acctPayEntry.getPayId() != null){
            return CommonResult.error(444,"该订单已经付款，无法重复付款");
        }
        BookingOrderVO bookingOrderVO = bookingOrderService.getBookingOrderById(acctPayEntry.getOrderId());
        AddAcctPayForm addAcctPayForm = ConvertUtil.convert(acctPayEntry, AddAcctPayForm.class);
        addAcctPayForm.setId(null);
        addAcctPayForm.setCustomerId(bookingOrderVO.getCustomerId());
        addAcctPayForm.setCustomerName(bookingOrderVO.getCustomerName());
        Integer result = acctPayService.saveAcctPay(addAcctPayForm);
        if(result == null){
            log.warn("付款操作，付款单保存失败");
            return CommonResult.error(444,"付款失败");
        }
        acctPayEntry.setPayId(result);
        boolean result1 = acctPayEntryService.saveOrUpdate(acctPayEntry);
        if(!result1){
            log.warn("付款操作，应付款单保存失败");
            return CommonResult.error(444,"付款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "合并付款")
    @PostMapping(value = "/consolidatedPayment")
    public CommonResult consolidatedPayment(@RequestBody QueryCommonForm form) {
        List<AcctPayEntry> acctPayEntries = new ArrayList<>();
        for (Integer id : form.getIds()) {
            AcctPayEntry acctPayEntry = acctPayEntryService.getById(id);
            acctPayEntries.add(acctPayEntry);
        }
        String paySupplierName = acctPayEntries.get(0).getPaySupplierName();
        String acctCode = acctPayEntries.get(0).getAcctCode();
        BigDecimal apMoney = new BigDecimal(0);
        BigDecimal proxyMoney = new BigDecimal(0);
        BigDecimal currencyName = new BigDecimal(0);
        for (AcctPayEntry acctPayEntry : acctPayEntries) {
            if(!acctPayEntry.getPaySupplierName().equals(paySupplierName)){
                return CommonResult.error(444,"收款供应商不一致，无法合并付款");
            }
            if(!acctPayEntry.getAcctCode().equals(acctCode)){
                return CommonResult.error(444,"收款银行账号不一致，无法合并付款");
            }
            if(acctPayEntry.getCurrencyName().equals(currencyName)){
                return CommonResult.error(444,"币种不一致，无法合并付款");
            }
            apMoney = apMoney.add(acctPayEntry.getApMoney());
            proxyMoney = proxyMoney.add((acctPayEntry.getProxyMoney() != null ? acctPayEntry.getProxyMoney() : new BigDecimal(0)));
        }
        AddAcctPayForm acctPayForm = ConvertUtil.convert(acctPayEntries.get(0), AddAcctPayForm.class);
        acctPayForm.setId(null);
        acctPayForm.setApMoney(apMoney);
        acctPayForm.setProxyMoney(proxyMoney);
        Integer integer = acctPayService.saveAcctPay(acctPayForm);
        if(integer == null){
            log.warn("合并付款操作，付款单保存失败");
            return CommonResult.error(444,"合并付款失败");
        }
        for (AcctPayEntry acctPayEntry : acctPayEntries) {
            acctPayEntry.setPayId(integer);
        }
        boolean result = this.acctPayEntryService.saveOrUpdateBatch(acctPayEntries);
        if(!result){
            log.warn("合并付款操作，应付款单保存失败");
            return CommonResult.error(444,"合并付款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "计算金额")
    @PostMapping(value = "/calculatedAmount")
    public CommonResult calculatedAmount(@RequestBody QueryCommonForm form) {
        BookingOrderVO bookingOrderVO = bookingOrderService.getBookingOrderById(form.getId());
        List<AcctPayEntry> acctPayEntries = acctPayEntryService.getAcctPayEntryByOrderId(bookingOrderVO.getId());
        BigDecimal acctPayEntrySum = new BigDecimal(0);
        BigDecimal bookingOrderSum = new BigDecimal(0);
        if(CollectionUtil.isNotEmpty(acctPayEntries)){
            for (AcctPayEntry acctPayEntry : acctPayEntries) {
                acctPayEntrySum = acctPayEntrySum.add(acctPayEntry.getApMoney());
            }
        }
        if(CollectionUtil.isNotEmpty(bookingOrderVO.getBookingOrderEntryList())){
            for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderVO.getBookingOrderEntryList()) {
                bookingOrderSum = bookingOrderSum.add(bookingOrderEntryVO.getTotalHgMoney());
            }
        }

        return CommonResult.success(bookingOrderSum.subtract(acctPayEntrySum));
    }

}

