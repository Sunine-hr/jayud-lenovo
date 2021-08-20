package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHubShippingEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrderEntry;
import com.jayud.scm.model.po.HubShippingEntry;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.IHubShippingEntryService;
import com.jayud.scm.service.IHubShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubShippingEntry")
@Api(tags = "出库明细管理")
public class HubShippingEntryController {

    @Autowired
    private IHubShippingEntryService hubShippingEntryService;

    @Autowired
    private IBookingOrderEntryService bookingOrderEntryService;

    @ApiOperation(value = "根据出库单id获取出库单详情")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<HubShippingEntryVO> page = this.hubShippingEntryService.findByPage(form);
        for (HubShippingEntryVO record : page.getRecords()) {

        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增或修改出库单明细")
    @PostMapping(value = "/saveOrUpdateHubShippingEntry")
    public CommonResult saveOrUpdateHubShippingEntry(@RequestBody List<AddHubShippingEntryForm> form) {
        if(CollectionUtils.isEmpty(form)){
            return CommonResult.error(444,"增加数据不为空");
        }

        //todo 新增或修改，所填的值不能超过带出来的委托单的值
        for (AddHubShippingEntryForm addHubShippingEntryForm : form) {
            BookingOrderEntry bookingOrderEntry = bookingOrderEntryService.getById(addHubShippingEntryForm.getBookingEntryId());
            if(bookingOrderEntry.getQty().compareTo(addHubShippingEntryForm.getQty()) == -1){
                return CommonResult.error(444,addHubShippingEntryForm.getItemName()+"的数量最大为"+bookingOrderEntry.getQty());
            }
        }

        boolean result = hubShippingEntryService.saveOrUpdateHubShippingEntry(form);
        if(!result){
            return CommonResult.error(444,"新增或修改出库订单明细失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取新增的出库单明细列表")
    @PostMapping(value = "/getHubShippingEntryList")
    public CommonResult<List<BookingOrderEntryVO>> getHubShippingEntryList(@RequestBody Map<String,Object> map) {
        Integer shippingId = MapUtil.getInt(map, "shippingId");
        Integer bookingId = MapUtil.getInt(map, "bookingId");
        List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingId);
        List<HubShippingEntry> shippingEntryByShippingId = hubShippingEntryService.getShippingEntryByShippingId(shippingId.longValue());
        for (BookingOrderEntryVO bookingOrderEntryVO : bookingOrderEntryByBookingId) {
            for (HubShippingEntry hubShippingEntry : shippingEntryByShippingId) {
                if(bookingOrderEntryVO.getId().equals(hubShippingEntry.getBookingEntryId())){
                    if(bookingOrderEntryVO.getQty().compareTo(hubShippingEntry.getQty()) == -1){
                        bookingOrderEntryVO.setQty(bookingOrderEntryVO.getQty().subtract(hubShippingEntry.getQty()));
                        bookingOrderEntryVO.setPackages(bookingOrderEntryVO.getPackages().subtract(BigDecimal.valueOf(hubShippingEntry.getPackages())));
                        bookingOrderEntryVO.setGw(bookingOrderEntryVO.getGw().subtract(hubShippingEntry.getGw()));
                        bookingOrderEntryVO.setNw(bookingOrderEntryVO.getNw().subtract(hubShippingEntry.getNw()));
                        bookingOrderEntryVO.setCbm(bookingOrderEntryVO.getCbm().subtract(hubShippingEntry.getCbm()));
                    }else{
                        bookingOrderEntryByBookingId.remove(bookingOrderEntryVO);
                    }
                }
            }
            if(CollectionUtils.isEmpty(bookingOrderEntryByBookingId)){
                return CommonResult.success();
            }
        }
        return CommonResult.success(bookingOrderEntryByBookingId);
    }
}

