package com.jayud.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.SysPostForm;
import com.jayud.auth.model.vo.SysPostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysPostService;
import com.jayud.auth.model.po.SysPost;

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
 * 岗位表 控制类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Slf4j
@Api(tags = "岗位表")
@RestController
@RequestMapping("/sysPost")
public class SysPostController {



    @Autowired
    public ISysPostService sysPostService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-22
     * @param: sysPost
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysPost>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<SysPost>> selectPage(SysPost sysPost,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysPostService.selectPage(sysPost,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-02-22
    * @param: sysPost
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysPost>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<SysPost>> selectList(SysPost sysPost,
                                                HttpServletRequest req) {
        return BaseResult.ok(sysPostService.selectList(sysPost));
    }


    /**
     * @description 递归查询数据列表查询数据
     **/
    @ApiOperation("递归查询数据列表查询数据")
    @GetMapping("/selectSysPostLists")
    public BaseResult<List<SysPostVO>> selectSysPostLists(SysPostForm sysPostForm,
                                                          HttpServletRequest req) {
        return BaseResult.ok(sysPostService.selectSysPostLists(sysPostForm));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-02-22
    * @param: sysPost
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@RequestBody SysPostForm sysPostForm ){

        if(sysPostForm==null){
            return  BaseResult.error("参数不能为空！");
        }
        sysPostService.saveOrUpdateSysPost(sysPostForm);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }

//
//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-02-22
//     * @param: sysPost
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody SysPost sysPost ){
//        sysPostService.updateById(sysPost);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }



//    /**
//     * @description 物理删除
//     * @author  jayud
//     * @date   2022-02-22
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        sysPostService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm ids){

//        sysPostService.deleteSysPost(ids.getIds());
        return sysPostService.deleteSysPost(ids.getIds());
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-02-22
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysPost>
     **/
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<SysPost> queryById(@RequestParam(name="id",required=true) Long id) {
        SysPost sysPost = sysPostService.getById(id);
        return BaseResult.ok(sysPost);
    }


}
