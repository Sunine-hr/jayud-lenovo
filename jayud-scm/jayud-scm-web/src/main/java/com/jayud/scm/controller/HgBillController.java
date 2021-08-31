package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.IHgBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/hgBill")
@Api(tags = "报关列表管理")
public class HgBillController {

    @Autowired
    private IHgBillService hgBillService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @ApiOperation(value = "根据委托单id获取报关单信息")
    @PostMapping(value = "/getHgBillByBookingId")
    public CommonResult getHgBillByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            if(bookingOrder.getBillId() != null){
                list.add(bookingOrder.getBillId());
            }
        }
        form.setIds(list);
        List<HgBillVO> hgBillVOS = hgBillService.getHgBillByBookingId(form);
        return CommonResult.success(hgBillVOS);
    }

    @ApiOperation(value = "报关单号日期录入")
    @PostMapping(value = "/entryCustomDate")
    public CommonResult entryCustomDate(@RequestBody QueryCommonForm form) {
        boolean update = hgBillService.entryCustomDate(form);
        if(!update){
            return CommonResult.error(444,"报关单号日期录入操作失败");
        }
        return CommonResult.success();
    }


}

