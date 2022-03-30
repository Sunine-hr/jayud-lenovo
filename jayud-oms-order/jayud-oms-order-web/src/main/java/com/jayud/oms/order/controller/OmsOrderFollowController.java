package com.jayud.oms.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import com.jayud.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.oms.order.service.IOmsOrderFollowService;
import com.jayud.oms.order.model.po.OmsOrderFollow;

import com.jayud.common.result.ListPageRuslt;
import com.jayud.common.result.PaginationBuilder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 订单状态跟进表 控制类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Slf4j
@Api(tags = "订单状态跟进表")
@RestController
@RequestMapping("/omsOrderFollow")
public class OmsOrderFollowController {



    @Autowired
    public IOmsOrderFollowService omsOrderFollowService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderFollow
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.oms.order.model.po.OmsOrderFollow>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<ListPageRuslt<OmsOrderFollow>> selectPage(OmsOrderFollow omsOrderFollow,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(PaginationBuilder.buildPageResult(omsOrderFollowService.selectPage(omsOrderFollow,currentPage,pageSize,req)));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrderFollow
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.oms.order.model.po.OmsOrderFollow>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<OmsOrderFollow>> selectList(OmsOrderFollow omsOrderFollow,
                                                HttpServletRequest req) {
        return BaseResult.ok(omsOrderFollowService.selectList(omsOrderFollow));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-23
    * @param: omsOrderFollow
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody OmsOrderFollow omsOrderFollow ){
        omsOrderFollowService.save(omsOrderFollow);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-23
     * @param: omsOrderFollow
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody OmsOrderFollow omsOrderFollow ){
        omsOrderFollowService.updateById(omsOrderFollow);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        omsOrderFollowService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        omsOrderFollowService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-23
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.oms.order.model.po.OmsOrderFollow>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<OmsOrderFollow> queryById(@RequestParam(name="id",required=true) int id) {
        OmsOrderFollow omsOrderFollow = omsOrderFollowService.getById(id);
        return BaseResult.ok(omsOrderFollow);
    }


    /**
    * @description 根据查询条件导出收货单
    * @author  jayud
    * @date   2022-03-23
    * @param: response  响应对象
    * @param: queryReceiptForm  参数queryReceiptForm
    * @param: req
    * @return: void
    **/
    @ApiOperation("根据查询条件导出订单状态跟进表")
    @PostMapping(path = "/exportOmsOrderFollow")
    public void exportOmsOrderFollow(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList(
                "自动ID",
                "订单主表ID",
                "状态类型",
                "状态信息",
                "状态时间",
                "是否同步在线",
                "备注",
                "组织机构ID",
                "多租户ID",
                "创建人名称",
                "创建时间",
                "最后修改人名称",
                "最后修改时间",
                "删除标志",
                "删除人",
                "删除时间"
            );
            List<LinkedHashMap<String, Object>> dataList = omsOrderFollowService.queryOmsOrderFollowForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "订单状态跟进表", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.toString());
        }
    }


}
