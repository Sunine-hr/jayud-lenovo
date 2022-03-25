package com.jayud.crm.controller;

import com.jayud.common.aop.annotations.SysLog;
import com.jayud.common.enums.SysLogTypeEnum;
import com.jayud.common.service.BaseCommonService;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.model.bo.CrmCreditVisitForm;
import com.jayud.crm.model.bo.DeleteForm;
import com.jayud.crm.model.vo.CrmCreditVisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.crm.service.ICrmCreditVisitService;
import com.jayud.crm.model.po.CrmCreditVisit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Autowired
    private AuthClient authClient;

    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-03-02
     * @param: crmCreditVisit
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.crm.model.po.CrmCreditVisit>>
     **/
    @SysLog("查询了走访记录")
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<CrmCreditVisitVO>> selectPage(@RequestBody CrmCreditVisitForm crmCreditVisitForm,
                                                          HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitService.selectPage(crmCreditVisitForm, crmCreditVisitForm.getCurrentPage(), crmCreditVisitForm.getPageSize(), req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-03-02
     * @param: crmCreditVisit
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.crm.model.po.CrmCreditVisit>>
     **/
    @ApiOperation("列表查询数据")
    @GetMapping("/selectList")
    public BaseResult<List<CrmCreditVisit>> selectList(CrmCreditVisit crmCreditVisit,
                                                       HttpServletRequest req) {
        return BaseResult.ok(crmCreditVisitService.selectList(crmCreditVisit));
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-03-02
     * @param: crmCreditVisit
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/add")
    public BaseResult add(@Valid @RequestBody CrmCreditVisitForm crmCreditVisitForm) {

        return crmCreditVisitService.saveOrUpdateCrmCreditVisit(crmCreditVisitForm);
    }


//    /**
//     * @description 编辑
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: crmCreditVisit
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody CrmCreditVisit crmCreditVisit ){
//        crmCreditVisitService.updateById(crmCreditVisit);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }


//
//    /**
//     * @description 物理删除
//     * @author  jayud
//     * @date   2022-03-02
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id",value = "主键id",dataType = "Long",required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id){
//        crmCreditVisitService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @PostMapping("/logicDel")
    public BaseResult logicDel(@RequestBody DeleteForm ids) {
        crmCreditVisitService.logicDel(ids.getIds());
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-03-02
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.crm.model.po.CrmCreditVisit>
     **/
    @ApiOperation("根据id查询详情")
    @GetMapping(value = "/findCrmCreditVisitIdOne")
    public BaseResult<CrmCreditVisitVO> findCrmCreditVisitIdOne(@RequestParam(name = "id", required = true) Long id) {
        CrmCreditVisitVO crmCreditVisitIdOne = null;
        crmCreditVisitIdOne = crmCreditVisitService.findCrmCreditVisitIdOne(id);
        if (crmCreditVisitIdOne == null) {
            return BaseResult.error("用户不存在！");
        }
        //拿到的员工的集合
        List<Long> listId = new ArrayList<>();
        String userIds = crmCreditVisitIdOne.getUserIds();
        String[] aa = userIds.split(",");
        for (int i = 0; i < aa.length; i++) {
            long l = Long.parseLong(aa[i]);
            listId.add(l);
        }
        crmCreditVisitIdOne.setVisitNameList(listId);

        //拿到创建时间
        List<String> visitDateTime = new ArrayList<>();

        visitDateTime.add(dateString(crmCreditVisitIdOne.getVisitDate()));
        visitDateTime.add(dateString(crmCreditVisitIdOne.getEndDate()));

        crmCreditVisitIdOne.setCreationVisitTime(visitDateTime);

        crmCreditVisitIdOne.setVisitDate(null);
        crmCreditVisitIdOne.setEndDate(null);
        return BaseResult.ok(crmCreditVisitIdOne);
    }

    @ApiOperation("获得员工列表下拉")
    @GetMapping("/selectListSysUserFeign")
    public BaseResult selectListSysUserFeign() {
        BaseResult baseResult = authClient.selectListFeign();
        return BaseResult.ok(baseResult);
    }


    public String dateString(LocalDateTime dateOne) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(dateOne);
        return localTime;
    }
}
