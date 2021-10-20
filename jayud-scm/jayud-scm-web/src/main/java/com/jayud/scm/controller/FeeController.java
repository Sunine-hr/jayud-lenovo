package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.vo.CustomerRelationerVO;
import com.jayud.scm.model.vo.FeeListVO;
import com.jayud.scm.model.vo.FeeVO;
import com.jayud.scm.service.IBDataDicEntryService;
import com.jayud.scm.service.IFeeLadderService;
import com.jayud.scm.service.IFeeListService;
import com.jayud.scm.service.IFeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 结算方案条款 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@RestController
@RequestMapping("/fee")
@Api(tags = "结算方案条款")
public class FeeController {

    @Autowired
    private IFeeService feeService;

    @Autowired
    private IFeeListService feeListService;

    @Autowired
    private IFeeLadderService feeLadderService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @ApiOperation(value = "根据条件分页查询所有该客户的结算方案条款")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryCommonForm form) {
        IPage<FeeVO> page = this.feeService.findByPage(form);
        if(page.getRecords().size()>0){
            for (FeeVO record : page.getRecords()) {
                List<FeeListVO> feeListVOS = feeListService.getFeeListByFeeId(record.getId());
                record.setFeeListVOS(feeListVOS);

//                record.setDeputyStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1019",record.getDeputyStyle()));
//                record.setIncoterms(ibDataDicEntryService.getTextByDicCodeAndDataValue("1002",record.getIncoterms()));
//                record.setPayStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1020",record.getPayStyle()));
//                record.setReStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1022",record.getReStyle()));
//                record.setProductClass(ibDataDicEntryService.getTextByDicCodeAndDataValue("1024",record.getProductClass()));
//                record.setAccountStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1025",record.getAccountStyle()));
//                record.setShippingFeeStyle(ibDataDicEntryService.getTextByDicCodeAndDataValue("1023",record.getShippingFeeStyle()));
//                record.setInterestType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1026",record.getInterestType()));
//                record.setGRateType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1027",record.getGRateType()));
//                record.setPRateType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1028",record.getPRateType()));
//                record.setRateValue(ibDataDicEntryService.getTextByDicCodeAndDataValue("1029",record.getRateValue()));
//                if(record.getInvType() != null){
//                    record.setInvType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1030",record.getInvType()));
//                }
//                if(record.getQuotaType() != null){
//                    record.setQuotaType(ibDataDicEntryService.getTextByDicCodeAndDataValue("1021",record.getQuotaType()));
//                }
            }
        }
        CommonPageResult pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "新增客户结算方案条款")
    @PostMapping(value = "/saveOrUpdateFee")
    public CommonResult saveOrUpdateFee(@RequestBody AddFeeForm form) {
        boolean result = feeService.saveOrUpdateFee(form);
        if(!result){
            return CommonResult.error(444,"新增客户结算方案条款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取客户结算方案条款信息的详情")
    @PostMapping(value = "/getFeeById")
    public CommonResult<FeeVO> getFeeById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        FeeVO feeVO = feeService.getFeeById(id);
        List<FeeListVO> feeListVOS = feeListService.getFeeListByFeeId(feeVO.getId());
        feeVO.setFeeListVOS(feeListVOS);
        return CommonResult.success(feeVO);
    }

    @ApiOperation(value = "删除结算方案条款")
    @PostMapping(value = "/deleteFeeByIds")
    public CommonResult deleteFeeByIds(@RequestBody DeleteForm form) {
        boolean result = feeService.deleteFeeByIds(form);
        if(!result){
            return CommonResult.error(444,"删除结算方案条款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "复制")
    @PostMapping(value = "/copyFee")
    public CommonResult copyFee(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        boolean result = feeService.copyFee(id);
        if(!result){
            return CommonResult.error(444,"复制结算方案条款失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "阶梯价设置")
    @PostMapping(value = "/stepPriceSetting")
    public CommonResult stepPriceSetting(@RequestBody AddFeeLadderSettingForm form) {
        boolean result = feeLadderService.stepPriceSetting(form);
        if(!result){
            return CommonResult.error(444,"添加结算方案阶梯价失败");
        }
        return CommonResult.success();
    }

}

