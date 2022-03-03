package com.jayud.auth.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.IBNoRuleService;
import com.jayud.auth.model.po.BNoRule;

import java.util.List;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 编号规则表 控制类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Api(tags = "编号规则表")
@RestController
@RequestMapping("/bNoRule")
public class BNoRuleController {



    @Autowired
    public IBNoRuleService bNoRuleService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: bNoRule
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BNoRule>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<BNoRule>> selectPage(BNoRule bNoRule,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(bNoRuleService.selectPage(bNoRule,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @date   2022-03-01
    * @param: bNoRule
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.BNoRule>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<BNoRule>> selectList(BNoRule bNoRule,
                                                HttpServletRequest req) {
        return BaseResult.ok(bNoRuleService.selectList(bNoRule));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-01
    * @param: bNoRule
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody BNoRule bNoRule ){

        BNoRule noRulesBySheetCode = bNoRuleService.getNoRulesBySheetCode(bNoRule.getNoCode());
        if(null == bNoRule.getId() && null != noRulesBySheetCode ){
            return BaseResult.error(444,"该编码已存在");
        }
        if(null != bNoRule.getId() && bNoRule.getId().equals(noRulesBySheetCode.getId())){
            return BaseResult.error(444,"该编码已存在");
        }

        bNoRuleService.save(bNoRule);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-01
     * @param: bNoRule
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody BNoRule bNoRule ){
        bNoRuleService.updateById(bNoRule);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-02
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        bNoRuleService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-01
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        bNoRuleService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.BNoRule>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<BNoRule> queryById(@RequestParam(name="id",required=true) int id) {
        BNoRule bNoRule = bNoRuleService.getById(id);
        return BaseResult.ok(bNoRule);
    }



    /**
     * 获取单据编号
     *
     * @param code
     */
    @ApiOperation("获取单据编号")
    @GetMapping(value = "/getOrder")
    public BaseResult getOrder(@RequestParam(name = "code", required = true) String code) {

        String order = bNoRuleService.getOrder(code, new Date());
        return BaseResult.ok(order);
    }
    /**
     * 获取单据编号
     *
     * @param code
     */
    @ApiOperation("获取单据编号")
    @PostMapping(value = "/api/getOrderFeign")
    public BaseResult getOrderFeign(@RequestParam(name = "code", required = true) String code, @RequestParam(name = "date", required = true) Date date) {
        String order = bNoRuleService.getOrder(code, date);
        BNoRule noRulesBySheetCode = bNoRuleService.getNoRulesBySheetCode(code);
        Integer checkLength = noRulesBySheetCode.getCheckLength();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("order",order);
        jsonObject.put("fLevel",checkLength);
        jsonObject.put("fStep",noRulesBySheetCode.getCheckStep());
        jsonObject.put("checkStateFlag",noRulesBySheetCode.getCheckFlag());
        return BaseResult.ok(jsonObject);
    }


}
