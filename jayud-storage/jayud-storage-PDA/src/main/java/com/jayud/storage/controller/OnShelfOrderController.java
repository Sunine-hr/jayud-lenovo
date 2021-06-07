package com.jayud.storage.controller;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.storage.model.bo.GoodsLocationRecordForm;
import com.jayud.storage.model.bo.InGoodsOperationRecordForm;
import com.jayud.storage.model.bo.QueryPutGoodForm;
import com.jayud.storage.model.bo.StorageInProcessOptForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.vo.InGoodsOperationRecordBatchNoVO;
import com.jayud.storage.model.vo.InGoodsOperationRecordVO;
import com.jayud.storage.model.vo.OnShelfOrderVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.ILocationService;
import com.jayud.storage.service.IStorageInputOrderService;
import com.jayud.storage.utils.DateDayUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.CommandMap;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PDA上架订单管理 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@Api(tags = "PDA上架订单管理")
@RequestMapping("/onShelfOrder")
public class OnShelfOrderController {

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IStorageInputOrderService storageInputOrderService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private ILocationService locationService;

    @ApiOperation("通过批次号查询入库商品信息")
    @PostMapping("/findByWarehousingBatchNo")
    public CommonResult findByWarehousingBatchNo(@RequestBody QueryPutGoodForm form) {
        List<InGoodsOperationRecord> listByWarehousingBatchNo = inGoodsOperationRecordService.getListByWarehousingBatchNo(form.getWarehousingBatchNo());
        return CommonResult.success(listByWarehousingBatchNo);
    }

    @ApiOperation("通过批次号和sku查询入库商品信息")
    @PostMapping("/findByWarehousingBatchNoAndSku")
    public CommonResult findByWarehousingBatchNoAndSku(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        String sku = MapUtil.getStr(map, "sku");
        if(warehousingBatchNo != null && sku == null){
            List<InGoodsOperationRecord> listByWarehousingBatchNo = inGoodsOperationRecordService.getListByWarehousingBatchNo(warehousingBatchNo);
            InGoodsOperationRecordBatchNoVO inGoodsOperationRecordBatchNoVO = new InGoodsOperationRecordBatchNoVO();
            for (InGoodsOperationRecord inGoodsOperationRecord : listByWarehousingBatchNo) {
                StorageInputOrder byId = storageInputOrderService.getById(inGoodsOperationRecord.getOrderId());
                inGoodsOperationRecordBatchNoVO.setWarehouseNumber(byId.getWarehouseNumber());
                inGoodsOperationRecordBatchNoVO.setOrderNo(byId.getOrderNo());
            }
            return CommonResult.success(inGoodsOperationRecordBatchNoVO) ;
        }
        if(warehousingBatchNo != null && sku != null){
            InGoodsOperationRecord listByWarehousingBatchNoAndSku = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(warehousingBatchNo, sku);

            //获取入仓号
            StorageInputOrder byId = storageInputOrderService.getById(listByWarehousingBatchNoAndSku.getOrderId());

            //获取已入库数据
            List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(listByWarehousingBatchNoAndSku.getId());
            Integer number = 0;
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                number= number + goodsLocationRecord.getNumber();
            }

            InGoodsOperationRecordBatchNoVO convert = ConvertUtil.convert(listByWarehousingBatchNoAndSku, InGoodsOperationRecordBatchNoVO.class);
            convert.setWarehouseNumber(byId.getWarehouseNumber());
            convert.setNumber(convert.getNumber()-number);
            return CommonResult.success(convert) ;
        }
        return CommonResult.success();
    }

    @ApiOperation("货物上架")
    @PostMapping("/goodsOnShelves")
    public CommonResult goodsOnShelves(@RequestBody StorageInProcessOptForm form) {
        if(CollectionUtils.isNotEmpty(form.getInGoodsOperationRecords())){
            form.setId(form.getInGoodsOperationRecords().get(0).getOrderId());
            form.setOrderId(form.getInGoodsOperationRecords().get(0).getOrderId());
            List<InGoodsOperationRecordForm> inGoodsOperationRecords = form.getInGoodsOperationRecords();
            for (InGoodsOperationRecordForm inGoodsOperationRecord : inGoodsOperationRecords) {
                List<GoodsLocationRecordForm> goodsLocationRecordForms = inGoodsOperationRecord.getGoodsLocationRecordForms();
                Integer number = 0;
                for (GoodsLocationRecordForm goodsLocationRecordForm : goodsLocationRecordForms) {
                    if(goodsLocationRecordForm.getKuCode() != null){
                        //判断库位是否存在
                        boolean result = locationService.isExistence(goodsLocationRecordForm.getKuCode());
                        if(!result){
                            return CommonResult.error(444,goodsLocationRecordForm.getKuCode()+"库位不存在");
                        }
                    }
                    number = number + goodsLocationRecordForm.getNumber();
                }
                //获取该商品已入库的信息
                List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(inGoodsOperationRecord.getId());
                Integer number1 = 0;
                for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                    number1 = number1 + goodsLocationRecord.getNumber();
                }
                if(inGoodsOperationRecord.getNumber() < (number + number1)){
                    return CommonResult.error(444,inGoodsOperationRecord.getName()+"待入库数量为"+(inGoodsOperationRecord.getNumber()-number1));
                }
            }
        }else{
            return CommonResult.error(444,"入库商品信息不为空");
        }
        boolean b = storageInputOrderService.warehousingEntry(form);
        if(!b){
            return CommonResult.error(444,"商品上架失败");
        }
        return CommonResult.success();
    }


    @ApiOperation("根据条件获取上架订单记录")
    @PostMapping("/getListByQueryForm")
    public CommonResult getListByQueryForm(@RequestBody QueryPutGoodForm form) {
        if(form.getCreateTime().length <= 0){
            String[] strings = new String[]{DateDayUtils.getFirst(),DateDayUtils.getLast()};
            form.setCreateTime(strings);
        }
        form.setStartTime();
        List<OnShelfOrderVO> onShelfOrderVOS = storageInputOrderService.getListByQueryForm(form);
        return CommonResult.success(onShelfOrderVOS);
    }

    @ApiOperation("根据条件获取上架订单记录详情")
    @PostMapping("/getOrderDetails")
    public CommonResult getOrderDetails(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        String orderNo = MapUtil.getStr(map, "orderNo");
        List<InGoodsOperationRecordVO> inGoodsOperationRecords = inGoodsOperationRecordService.getListByWarehousingBatchNoAndOrderNo(warehousingBatchNo,orderNo);
        return CommonResult.success(inGoodsOperationRecords);
    }


}
