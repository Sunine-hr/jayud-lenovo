package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddCheckOrderEntryForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderEntry;
import com.jayud.scm.model.vo.BookingOrderEntryVO;
import com.jayud.scm.model.vo.CheckOrderEntryVO;
import com.jayud.scm.service.IBookingOrderEntryService;
import com.jayud.scm.service.ICheckOrderEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提验货单明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/checkOrderEntry")
@Api(tags = "提货验货明细管理")
public class CheckOrderEntryController {

    @Autowired
    private ICheckOrderEntryService checkOrderEntryService;

    @Autowired
    private IBookingOrderEntryService bookingOrderEntryService;

    @ApiOperation(value = "根据提验货单获取提货验货单明细")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<CheckOrderEntryVO>> findByPage(@RequestBody QueryCommonForm form) {
        IPage<CheckOrderEntryVO> page = this.checkOrderEntryService.findByPage(form);
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增提货验货单明细")
    @PostMapping(value = "/addCheckOrderEntry")
    public CommonResult addCheckOrderEntry(@RequestBody List<AddCheckOrderEntryForm> form) {

        if(CollectionUtils.isEmpty(form)){
            return CommonResult.error(444,"新增数据不能为空");
        }

        boolean result = checkOrderEntryService.addCheckOrderEntry(form);
        if(!result){
            return CommonResult.error(444,"新增提货验货单明细失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "保存提货验货单明细")
    @PostMapping(value = "/updateCheckOrderEntry")
    public CommonResult updateCheckOrderEntry(@RequestBody List<AddCheckOrderEntryForm> form) {
        if(CollectionUtils.isEmpty(form)){
            return CommonResult.error(444,"保存数据不能为空");
        }

        boolean result = checkOrderEntryService.updateCheckOrderEntry(form);
        if(!result){
            return CommonResult.error(444,"保存提货验货单明细失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取新增的提验货明细列表")
    @PostMapping(value = "/getBookingOrderEntryList")
    public CommonResult<List<BookingOrderEntryVO>> getBookingOrderEntryList(@RequestBody Map<String,Object> map) {
        Integer checkOrderId = MapUtil.getInt(map, "checkOrderId");
        Integer bookingId = MapUtil.getInt(map, "bookingId");
        List<BookingOrderEntryVO> bookingOrderEntryByBookingId = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingId);
        List<CheckOrderEntry> checkOrderEntries = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrderId.longValue());

        for (int i = 0; i < bookingOrderEntryByBookingId.size(); i++) {
            bookingOrderEntryByBookingId.get(i).setCheckId(checkOrderId);
            for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
                if(bookingOrderEntryByBookingId.get(i).getId().equals(checkOrderEntry.getBookingEntryId())){
                    bookingOrderEntryByBookingId.remove(i);
                }
            }
        }

        if(CollectionUtils.isEmpty(bookingOrderEntryByBookingId)){
            return CommonResult.success();
        }
        return CommonResult.success(bookingOrderEntryByBookingId);
    }

    @ApiOperation(value = "获取新增的提验货明细列表通过委托单id")
    @PostMapping(value = "/findBookingOrderEntryByBookingId")
    public CommonResult<List<BookingOrderEntryVO>> findBookingOrderEntryByBookingId(@RequestBody Map<String,Object> map) {
        Integer bookingId = MapUtil.getInt(map, "bookingId");
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingId);
        List<CheckOrderEntry> checkOrderEntries = checkOrderEntryService.getCheckOrderEntryByBookingId(bookingId);
        if(CollectionUtils.isNotEmpty(checkOrderEntries)){
            for (int i = 0; i < bookingOrderEntryList.size(); i++) {
                for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {
                    if(bookingOrderEntryList.get(i).getId().equals(checkOrderEntry.getBookingEntryId())){
                        bookingOrderEntryList.remove(i);
                    }
                }
            }

        }

        if(CollectionUtils.isEmpty(bookingOrderEntryList)){
            return CommonResult.success(new ArrayList<>());
        }
        return CommonResult.success(bookingOrderEntryList);
    }

}

