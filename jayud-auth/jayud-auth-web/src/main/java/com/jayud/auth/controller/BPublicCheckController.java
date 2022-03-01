package com.jayud.auth.controller;

import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.service.IPublicCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.model.po.BPublicCheck;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 审核记录表 控制类
 *
 * @author jayud
 * @since 2022-02-28
 */
@Slf4j
@Api(tags = "审核记录表")
@RestController
@RequestMapping("/bPublicCheck")
public class BPublicCheckController {

    @Autowired
    public IPublicCheckService bPublicCheckService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-28
     * @param: bPublicCheck
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BPublicCheck>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<BPublicCheck>> selectPage(BPublicCheck bPublicCheck,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(bPublicCheckService.selectPage(bPublicCheck,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-28
    * @param: bPublicCheck
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.BPublicCheck>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<BPublicCheck>> selectList(BPublicCheck bPublicCheck,
                                                HttpServletRequest req) {
        return BaseResult.ok(bPublicCheckService.selectList(bPublicCheck));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-28
    * @param: bPublicCheck
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody BPublicCheck bPublicCheck ){
        bPublicCheckService.save(bPublicCheck);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-02-28
     * @param: bPublicCheck
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody BPublicCheck bPublicCheck ){
        bPublicCheckService.updateById(bPublicCheck);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        bPublicCheckService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        bPublicCheckService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.BPublicCheck>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<BPublicCheck> queryById(@RequestParam(name="id",required=true) int id) {
        BPublicCheck bPublicCheck = bPublicCheckService.getById(id);
        return BaseResult.ok(bPublicCheck);
    }

    /**
     * 审核
     */
    @ApiOperation("审核")
    @PostMapping(value = "/check")
    public BaseResult check(@RequestBody CheckForm checkForm){

        return bPublicCheckService.check(checkForm);
    }


    /**
     * 反核
     */
    @ApiOperation("反核")
    @PostMapping(value = "/unCheck")
    public BaseResult unCheck(@RequestBody CheckForm checkForm){

        return bPublicCheckService.unCheck(checkForm);
    }


}
