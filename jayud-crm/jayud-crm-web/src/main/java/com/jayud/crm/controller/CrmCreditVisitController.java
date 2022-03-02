package com.jayud.crm.controller;

import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.vo.CrmCreditVisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditVisitService;
import com.jayud.crm.model.po.CrmCreditVisit;

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
 * 基本档案_客户_客户走访记录 控制类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Api(tags = "基本档案_客户_客户走访记录")
@RestController
@RequestMapping("/crmCreditVisit")
public class CrmCreditVisitController {



    @Autowired
    public ICrmCreditVisitService crmCreditVisitService;


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmCreditVisit>>
     **/
    @ApiOperation("分页查询数据")
    @GetMapping("/selectPage")
    public BaseResult<IPage<CrmCreditVisitVO>> selectPage(CrmCreditVisitForm crmCreditVisitForm,
                                                          @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                          HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitService.selectPage(crmCreditVisitForm,currentPage,pageSize,req));
    }


    /**
    * @description 列表查询数据
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCreditVisit
    * @param: req
    * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.crm.model.po.CrmCreditVisit>>
    **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditVisit>> selectList(CrmCreditVisit crmCreditVisit,
                                                HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitService.selectList(crmCreditVisit));
    }


    /**
    * @description 新增
    * @author  jayud
    * @date   2022-03-02
    * @param: crmCreditVisit
    * @return: com.jayud.common.BaseResult
    **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCreditVisit crmCreditVisit ){
        crmCreditVisitService.save(crmCreditVisit);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }


    /**
     * @description 编辑
     * @author  jayud
     * @date   2022-03-02
     * @param: crmCreditVisit
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("编辑")
    @PostMapping("/edit")
    public BaseResult edit(@Valid @RequestBody CrmCreditVisit crmCreditVisit ){
        crmCreditVisitService.updateById(crmCreditVisit);
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
        crmCreditVisitService.phyDelById(id);
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
        crmCreditVisitService.logicDel(id);
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    @ApiOperation("根据id查询")
    @ApiImplicitParam(name = "id",value = "主键id",dataType = "int",required = true)
    @GetMapping(value = "/queryById")
    public BaseResult<CrmCreditVisit> queryById(@RequestParam(name="id",required=true) int id) {
        CrmCreditVisit crmCreditVisit = crmCreditVisitService.getById(id);
        return BaseResult.ok(crmCreditVisit);
    }


}
