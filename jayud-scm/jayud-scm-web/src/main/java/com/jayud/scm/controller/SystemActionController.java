package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddSystemActionForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.CorrespondEnum;
import com.jayud.scm.model.po.SystemAction;
import com.jayud.scm.model.po.SystemMenu;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.ISystemActionService;
import com.jayud.scm.service.ISystemMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 按钮权限设置表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/systemAction")
@Api("按钮权限设置管理")
public class SystemActionController {

    @Autowired
    private ISystemActionService systemActionService;

    @Autowired
    private ISystemMenuService systemMenuService;

    @ApiOperation(value = "根据条件分页查询所有按钮的关系")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryForm form) {
        if(form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        IPage<SystemActionVO> page = this.systemActionService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success( new CommonPageResult(page));
        }else {
            CommonPageResult<SystemActionVO> pageVO = new CommonPageResult(page);
            return CommonResult.success(pageVO);
        }
    }

    @ApiOperation(value = "根据id查询按钮的信息")
    @PostMapping(value = "/getSystemActionById")
    public CommonResult getSystemActionById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        SystemActionOutVO systemActionById = systemActionService.getSystemActionById(id);
        return CommonResult.success(systemActionById);
    }

    @ApiOperation(value = "增加或修改按钮或者菜单")
    @PostMapping(value = "/saveOrUpdateSystemAction")
    public CommonResult saveOrUpdateSystemAction(@RequestBody AddSystemActionForm form) {
        if(null == form.getId()){
            SystemAction systemAction = systemActionService.getSystemActionByActionCode(form.getActionCode());
            if(systemAction != null ){
                return CommonResult.error(444,"权限code已存在");
            }
        }

        boolean result = systemActionService.saveOrUpdateSystemAction(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"按钮权限添加失败");
    }

    @ApiOperation(value = "获取角色审核权限的下拉菜单")
    @PostMapping(value = "/findAllMenuNode")
    public CommonResult<List<QueryMenuStructureVO>> findAllMenuNode() {
        List<QueryMenuStructureVO> menuStructureVOS = systemMenuService.findAllMenuNode();
        for (QueryMenuStructureVO menuStructureVO : menuStructureVOS) {
            //获取菜单下按钮
            for (QueryMenuStructureVO child : menuStructureVO.getChildren()) {
                //获取菜单下按钮
                List<SystemAction> systemActions = systemActionService.getSystemActionByIsAudit(child.getId());

                List<QueryMenuStructureVO> menuStructureVOS1 = new ArrayList<>();
                for (SystemAction systemAction : systemActions) {
                    QueryMenuStructureVO menuStructureVO1 = new QueryMenuStructureVO();
                    menuStructureVO1.setLabel(systemAction.getActionName());
                    menuStructureVO1.setFId(systemAction.getParentId().longValue());
                    menuStructureVO1.setId(systemAction.getId().longValue());
                    menuStructureVO1.setActionCode(systemAction.getActionCode());
                    menuStructureVO1.setChildren(new ArrayList<>());
                    menuStructureVOS1.add(menuStructureVO1);
                }
                child.setChildren(menuStructureVOS1);
            }
        }
        return CommonResult.success(menuStructureVOS);
    }

    @ApiOperation(value = "获取权限管理的下拉菜单")
    @PostMapping(value = "/findAllMenuNode2")
    public CommonResult<List<QueryMenuStructureVO>> findAllMenuNode2() {
        List<QueryMenuStructureVO> menuStructureVOS = systemMenuService.findAllMenuNode();
        return CommonResult.success(menuStructureVOS);
    }

}

