package com.jayud.oceanship.controller;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResult<Map<String, Object>> initAir() {
        List<InitComboxStrVO> initComboxStrVOS = this.seaPortService.initSeaPort();
        List<InitComboxVO> initComboxVO = this.terms.initTerms();
        List<CabinetTypeVO> list = cabinetTypeService.initCabinetType();

        Map<String, Object> response = new HashMap<>();
        //空运港口下拉选项
        response.put("seaPorts", initComboxStrVOS);
        //贸易类型下拉选项
        response.put("seaTerms", initComboxVO);

        response.put("cabinetType",list);
        return CommonResult.success(response);
    }


}
