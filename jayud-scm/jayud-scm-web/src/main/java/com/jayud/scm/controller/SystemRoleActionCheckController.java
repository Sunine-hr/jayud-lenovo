package com.jayud.scm.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddSystemRoleActionCheckForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.enums.CorrespondEnum;
import com.jayud.scm.model.vo.QueryMenuStructureVO;
import com.jayud.scm.model.vo.SystemRoleActionCheckVO;
import com.jayud.scm.model.vo.SystemRoleActionVO;
import com.jayud.scm.service.ISystemMenuService;
import com.jayud.scm.service.ISystemRoleActionCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色审核级别权限 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/systemRoleActionCheck")
@Api("角色审核级别权限管理")
public class SystemRoleActionCheckController {

    @Autowired
    private ISystemRoleActionCheckService systemRoleActionCheckService;

    @ApiOperation(value = "根据条件分页查询角色审核权限")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryForm form) {
        if(form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        IPage<SystemRoleActionCheckVO> page = this.systemRoleActionCheckService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success( new CommonPageResult(page));
        }else {
            CommonPageResult<SystemRoleActionCheckVO> pageVO = new CommonPageResult(page);
            return CommonResult.success(pageVO);
        }
    }

    @ApiOperation(value = "添加角色审核权限")
    @PostMapping(value = "/addSystemRoleActionCheck")
    public CommonResult addSystemRoleAction(@RequestBody AddSystemRoleActionCheckForm form) {

        if(form == null){
            return CommonResult.success();
        }

        boolean result = systemRoleActionCheckService.addSystemRoleAction(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"添加角色审核权限失败");
    }

}

