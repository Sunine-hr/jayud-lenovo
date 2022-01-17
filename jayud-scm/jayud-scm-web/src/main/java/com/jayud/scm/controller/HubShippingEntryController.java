package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
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
import java.util.ArrayList;
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
        if(ObjectUtil.isEmpty(bookingId)){
            return CommonResult.error(-1,"委托单id不能为空");
        }
        List<BookingOrderEntryVO> bookingOrderEntryList = bookingOrderEntryService.findBookingOrderEntryByBookingId(bookingId);

        List<BookingOrderEntryVO> bookingOrderEntryList1 = new ArrayList<>();
        for (int i = 0; i < bookingOrderEntryList.size(); i++) {
            BookingOrderEntryVO bookingOrderEntryVO = bookingOrderEntryList.get(i);
            List<HubShippingEntry> hubShippingEntries = hubShippingEntryService.getShippingEntryByBookingEntryId(bookingOrderEntryVO.getId());
            if(CollectionUtil.isNotEmpty(hubShippingEntries)){
                HubShippingEntry hubShippingEntry1 = new HubShippingEntry();
                for (HubShippingEntry hubShippingEntry : hubShippingEntries) {
                    hubShippingEntry1.setQty((hubShippingEntry1.getQty()!=null ?hubShippingEntry1.getQty():new BigDecimal(0)).add(hubShippingEntry.getQty()!=null ?hubShippingEntry.getQty():new BigDecimal(0)));
                    hubShippingEntry1.setCbm((hubShippingEntry1.getCbm()!=null ?hubShippingEntry1.getCbm():new BigDecimal(0)).add(hubShippingEntry.getCbm()!=null ?hubShippingEntry.getCbm():new BigDecimal(0)));
                    hubShippingEntry1.setGw((hubShippingEntry1.getGw()!=null ?hubShippingEntry1.getGw():new BigDecimal(0)).add(hubShippingEntry.getGw()!=null ?hubShippingEntry.getGw():new BigDecimal(0)));
                    hubShippingEntry1.setNw((hubShippingEntry1.getNw()!=null ?hubShippingEntry1.getNw():new BigDecimal(0)).add(hubShippingEntry.getNw()!=null ?hubShippingEntry.getNw():new BigDecimal(0)));
                }

                if(hubShippingEntry1.getQty().compareTo(bookingOrderEntryVO.getQty()) > -1){
                    continue;
                }else{
                    bookingOrderEntryVO.setQty(bookingOrderEntryVO.getQty().subtract(hubShippingEntry1.getQty()));
                    bookingOrderEntryVO.setNw((bookingOrderEntryVO.getNw()!=null ?bookingOrderEntryVO.getNw():new BigDecimal(0)).subtract(hubShippingEntry1.getNw()));
                    bookingOrderEntryVO.setCbm((bookingOrderEntryVO.getCbm()!=null ?bookingOrderEntryVO.getCbm():new BigDecimal(0)).subtract(hubShippingEntry1.getCbm()));
                    bookingOrderEntryVO.setGw((bookingOrderEntryVO.getGw()!=null ?bookingOrderEntryVO.getGw():new BigDecimal(0)).subtract(hubShippingEntry1.getGw()));
                }
            }
            bookingOrderEntryVO.setBookingEntryId(bookingOrderEntryVO.getId());
            bookingOrderEntryList1.add(bookingOrderEntryVO);
        }
        if(org.springframework.util.CollectionUtils.isEmpty(bookingOrderEntryList1)){
            return CommonResult.success();
        }
        return CommonResult.success(bookingOrderEntryList1);
    }
}

