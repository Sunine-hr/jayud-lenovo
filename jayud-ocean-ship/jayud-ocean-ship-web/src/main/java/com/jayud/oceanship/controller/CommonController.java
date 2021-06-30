package com.jayud.oceanship.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.service.ICabinetSizeService;
import com.jayud.oceanship.service.ICabinetTypeService;
import com.jayud.oceanship.service.ISeaPortService;
import com.jayud.oceanship.service.ITermsService;
import com.jayud.oceanship.po.CabinetType;
import com.jayud.oceanship.vo.CabinetTypeVO;
import com.jayud.oceanship.vo.InitComboxListVO;
import com.jayud.oceanship.vo.InitComboxSVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "海运模块公用接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private ISeaPortService seaPortService;

    @Autowired
    private ITermsService terms;

    @Autowired
    private ICabinetTypeService cabinetTypeService;

    @Autowired
    private ICabinetSizeService cabinetSizeService;

    @ApiOperation(value = "下拉框(审核通过的供应商)")
    @PostMapping(value = "/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        CommonResult<List<InitComboxVO>> result = omsClient.initSupplierInfo();
//        if (result.getMsg().equals("成功")) {
//            log.warn("远程调用审核通过的供应商失败 msg={}", result.getMsg());
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
        return CommonResult.success(result.getData());
    }

    @ApiOperation(value = "主订单下拉选项-船舶港口，贸易类型")
    @PostMapping(value = "/mainOrder/initSea")
    public CommonResult<Map<String, Object>> initSea() {
        List<InitComboxStrVO> initComboxStrVOS = this.seaPortService.initSeaPort();
        List<InitComboxVO> initComboxVO = this.terms.initTerms();
        List<CabinetTypeVO> list = cabinetTypeService.initCabinetType();
        List<InitComboxSVO> data = omsClient.initDictNameByDictTypeCode("transportClause").getData();
        List<InitComboxSVO> data1 = omsClient.initDictNameByDictTypeCode("deliveryMode").getData();


        Map<String, Object> response = new HashMap<>();
        //空运港口下拉选项
        response.put("seaPorts", initComboxStrVOS);
        //贸易类型下拉选项
        response.put("seaTerms", initComboxVO);

        response.put("cabinetType",list);
        response.put("cabinetSize",list.get(0).getCabinetSizes());
        response.put("transportClause",data);
        response.put("deliveryMode",data1);

        return CommonResult.success(response);
    }


    @ApiOperation(value = "主订单下拉选项-船舶港口，贸易类型")
    @PostMapping(value = "/initCompany")
    public CommonResult<Map<String, Object>> initCompany(@RequestBody Map<String,Object> map) {
        Long id = MapUtil.getLong(map, "id");
        List<InitComboxSVO> data2 = omsClient.initDictNameByDictTypeCode("company").getData();
        InitComboxListVO initComboxListVO = new InitComboxListVO();
        initComboxListVO.setInitComboxSVOList(data2);
        initComboxListVO.setDefaultValue(data2.get(0).getValue());
        Map<String, Object> response = new HashMap<>();
        response.put("company",initComboxListVO);
        return CommonResult.success(response);
    }

}
