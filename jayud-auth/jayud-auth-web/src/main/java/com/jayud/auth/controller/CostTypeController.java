package com.jayud.auth.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.auth.model.enums.StatusEnum;
import com.jayud.auth.model.po.CostInfo;
import com.jayud.auth.service.ICostInfoService;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.ResultEnum;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ICostTypeService;
import com.jayud.auth.model.po.CostType;

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
 * 费用类别 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "费用类别")
@RestController
@RequestMapping("/costType")
public class CostTypeController {



    @Autowired
    public ICostTypeService costTypeService;

    @Autowired
    private ICostInfoService costInfoService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: costType
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CostType>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CostType>> selectPage(CostType costType,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(costTypeService.selectPage(costType,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: costType
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.CostType>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CostType>> selectList(CostType costType,
                                                HttpServletRequest req) {
        return BaseResult.ok(costTypeService.selectList(costType));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: costType
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CostType costType ){
        costTypeService.save(costType);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: costType
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CostType costType ){
        costTypeService.updateById(costType);
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
        costTypeService.phyDelById(id);
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
        costTypeService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.CostType>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CostType> queryById(@RequestParam(name="id",required=true) int id) {
        CostType costType = costTypeService.getById(id);
        return BaseResult.ok(costType);
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
    @ApiOperation("根据查询条件导出费用类别")
    @PostMapping(path = "/exportCostType")
    public void exportCostType(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "主键ID",
                "费用类型CODE",
                "费用类型名称",
                "是否代垫代收（1-是 0-否）",
                "状态（0无效 1有效）",
                "创建时间",
                "创建人",
                "更新时间",
                "更新人",
                "描述"
            );
            List<LinkedHashMap<String, Object>> dataList = costTypeService.queryCostTypeForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "费用类别", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }

    @ApiOperation(value = "获取启用费用类型")
    @PostMapping(value = "/api/getCostTypes")
    public CommonResult<List<CostType>> getCostTypes() {
        List<CostType> costTypes = costTypeService.list(new QueryWrapper<>(new CostType().setStatus(StatusEnum.ENABLE.getCode())));
        return CommonResult.success(costTypes);
    }

    @ApiOperation(value = "费用类别,idCode=费用名称的隐藏值")
    @PostMapping(value = "/api/initCostType")
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String, Object> param) {
        String idCode = MapUtil.getStr(param, CommonConstant.ID_CODE);
        QueryWrapper queryCostInfo = new QueryWrapper();
        queryCostInfo.eq(SqlConstant.ID_CODE, idCode);
        CostInfo costInfo = costInfoService.getOne(queryCostInfo);
        if (costInfo == null || StringUtil.isNullOrEmpty(costInfo.getCids())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        String[] cids = costInfo.getCids().split(CommonConstant.COMMA);
        List<InitComboxVO> costTypeComboxs = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, CommonConstant.VALUE_1);
        queryWrapper.in(SqlConstant.ID, cids);
        List<CostType> costTypes = costTypeService.list(queryWrapper);
        for (CostType costType : costTypes) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setName(costType.getCodeName());
            initComboxVO.setId(costType.getId());
            costTypeComboxs.add(initComboxVO);
        }
        return CommonResult.success(costTypeComboxs);
    }

}
