package com.jayud.auth.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.auth.model.enums.StatusEnum;
import com.jayud.auth.model.po.CurrencyInfo;
import com.jayud.auth.service.ICurrencyInfoService;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.ResultEnum;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ICostInfoService;
import com.jayud.auth.model.po.CostInfo;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 费用名描述 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "费用名描述")
@RestController
@RequestMapping("/costInfo")
public class CostInfoController {



    @Autowired
    public ICostInfoService costInfoService;

    @Autowired
    private ICurrencyInfoService currencyInfoService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: costInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CostInfo>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CostInfo>> selectPage(CostInfo costInfo,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(costInfoService.selectPage(costInfo,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: costInfo
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.CostInfo>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CostInfo>> selectList(CostInfo costInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(costInfoService.selectList(costInfo));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: costInfo
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CostInfo costInfo ){
        costInfoService.save(costInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: costInfo
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CostInfo costInfo ){
        costInfoService.updateById(costInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        costInfoService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        costInfoService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.CostInfo>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CostInfo> queryById(@RequestParam(name="id",required=true) int id) {
        CostInfo costInfo = costInfoService.getById(id);
        return BaseResult.ok(costInfo);
    }



    @ApiOperation(value = "获取启用费用名称")
    @PostMapping(value = "/api/getCostInfos")
    public CommonResult<List<CostInfo>> getCostInfos() {
        //费用名称
        List<CostInfo> costInfos = costInfoService.list(new QueryWrapper<>(new CostInfo().setStatus(StatusEnum.ENABLE.getCode())));
        return CommonResult.success(costInfos);
    }

    @ApiOperation(value = "录入费用:应收/付项目/币种 ")
    @PostMapping(value = "/api/initCost")
    public CommonResult initCost(@RequestBody Map<String, Object> param) {
        String createdTimeStr = MapUtil.getStr(param, "createdTimeStr");
        if (StringUtil.isNullOrEmpty(createdTimeStr)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Map<String, Object> result = new HashMap<>();
        List<CostInfo> costInfos = costInfoService.findCostInfo();//费用项目
        List<InitComboxStrVO> paymentCombox = new ArrayList<>();
        List<InitComboxStrVO> receivableCombox = new ArrayList<>();
        for (CostInfo costInfo : costInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(costInfo.getIdCode());
            comboxStrVO.setName(costInfo.getName());
            receivableCombox.add(comboxStrVO);//后期没做应收应付项目的区分
            paymentCombox.add(comboxStrVO);
        }
        result.put("paymentCost", paymentCombox);
        result.put("receivableCost", receivableCombox);

        //币种
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        List<CurrencyInfo> currencyInfos = currencyInfoService.findCurrencyInfo(createdTimeStr);
        for (CurrencyInfo currencyInfo : currencyInfos) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(currencyInfo.getCurrencyCode());
            comboxStrVO.setName(currencyInfo.getCurrencyName());
            comboxStrVO.setNote(String.valueOf(currencyInfo.getExchangeRate()));
            initComboxStrVOS.add(comboxStrVO);
        }
        result.put("currency", initComboxStrVOS);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "根据费用名称查询费用类型")
    @PostMapping(value = "/api/initCostTypeByCostInfoCode")
    public CommonResult<Map<String, List<InitComboxVO>>> initCostTypeByCostInfoCode(){
        return CommonResult.success(this.costInfoService.initCostTypeByCostInfoCode());
    }




    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-04-11
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出费用名描述")
    @PostMapping(path = "/exportCostInfo")
    public void exportCostInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自增id",
                "费用code",
                "费用名",
                "费用名状态(0无效 1有效)",
                "费用类型(cost_type id)多个id用','隔开",
                "描述",
                "创建时间",
                "创建人",
                "状态(1应收 2应付)",
                "更新人",
                "更新时间",
                "是否展示给司机",
                "实报实销"
            );
            List<LinkedHashMap<String, Object>> dataList = costInfoService.queryCostInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "费用名描述", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
