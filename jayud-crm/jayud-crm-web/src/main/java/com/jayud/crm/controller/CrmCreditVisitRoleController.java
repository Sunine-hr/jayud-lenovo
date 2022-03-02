package com.jayud.crm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditVisitRoleService;
import com.jayud.crm.model.po.CrmCreditVisitRole;

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
 * 客户走访记录-拜访人员(员工表)关联表 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "客户走访记录-拜访人员(员工表)关联表")
@RestController
@RequestMapping("/crmCreditVisitRole")
public class CrmCreditVisitRoleController {



    @Autowired
    public ICrmCreditVisitRoleService crmCreditVisitRoleService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisitRole
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisitRole>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCreditVisitRole>> selectPage(CrmCreditVisitRole crmCreditVisitRole,
                                                   @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitRoleService.selectPage(crmCreditVisitRole,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCreditVisitRole
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCreditVisitRole>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditVisitRole>> selectList(CrmCreditVisitRole crmCreditVisitRole,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitRoleService.selectList(crmCreditVisitRole));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCreditVisitRole
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCreditVisitRole crmCreditVisitRole ){
        crmCreditVisitRoleService.save(crmCreditVisitRole);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisitRole
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCreditVisitRole crmCreditVisitRole ){
        crmCreditVisitRoleService.updateById(crmCreditVisitRole);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("物理删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/phyDel")
    public BaseResult phyDel(@RequestParam Long id){
        crmCreditVisitRoleService.phyDelById(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    /**
     * @description 逻辑删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
    @GetMapping("/logicDel")
    public BaseResult logicDel(@RequestParam Long id){
        crmCreditVisitRoleService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCreditVisitRole>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCreditVisitRole> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCreditVisitRole crmCreditVisitRole = crmCreditVisitRoleService.getById(id);
        return BaseResult.ok(crmCreditVisitRole);
    }


}
