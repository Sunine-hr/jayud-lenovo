package com.jayud.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxStrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ICurrencyInfoService;
import com.jayud.auth.model.po.CurrencyInfo;

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
 * 币种 控制类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Slf4j
@Api(tags = "币种")
@RestController
@RequestMapping("/currencyInfo")
public class CurrencyInfoController {



    @Autowired
    public ICurrencyInfoService currencyInfoService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: currencyInfo
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.CurrencyInfo>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<CurrencyInfo>> selectPage(CurrencyInfo currencyInfo,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(currencyInfoService.selectPage(currencyInfo,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-04-11
    * @param: currencyInfo
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.CurrencyInfo>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CurrencyInfo>> selectList(CurrencyInfo currencyInfo,
                                                HttpServletRequest req) {
        return BaseResult.ok(currencyInfoService.selectList(currencyInfo));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-04-11
    * @param: currencyInfo
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CurrencyInfo currencyInfo ){
        currencyInfoService.save(currencyInfo);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-04-11
     * @param: currencyInfo
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CurrencyInfo currencyInfo ){
        currencyInfoService.updateById(currencyInfo);
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
        currencyInfoService.phyDelById(id);
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
        currencyInfoService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.CurrencyInfo>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CurrencyInfo> queryById(@RequestParam(name="id",required=true) int id) {
        CurrencyInfo currencyInfo = currencyInfoService.getById(id);
        return BaseResult.ok(currencyInfo);
    }

    @ApiOperation(value = "币种")
    @RequestMapping(value = "/api/initCurrencyInfo")
    public ApiResult initCurrencyInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.STATUS, 1);
        List<CurrencyInfo> currencyInfos = currencyInfoService.list(queryWrapper);
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (CurrencyInfo currencyInfo : currencyInfos) {
            InitComboxStrVO initComboxVO = new InitComboxStrVO();
            initComboxVO.setCode(currencyInfo.getCurrencyCode());
            initComboxVO.setName(currencyInfo.getCurrencyName());
            initComboxVO.setId(currencyInfo.getId());
            initComboxStrVOS.add(initComboxVO);
        }
        return ApiResult.ok(initComboxStrVOS);
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
    @ApiOperation("根据查询条件导出币种")
    @PostMapping(path = "/exportCurrencyInfo")
    public void exportCurrencyInfo(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自增加id",
                "编码",
                "币种代码",
                "币种名称",
                "状态(0无效 1有效)",
                "创建人",
                "创建时间"
            );
            List<LinkedHashMap<String, Object>> dataList = currencyInfoService.queryCurrencyInfoForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "币种", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
