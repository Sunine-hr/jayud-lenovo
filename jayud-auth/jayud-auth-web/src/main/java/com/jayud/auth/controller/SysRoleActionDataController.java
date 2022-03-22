package com.jayud.auth.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddSysRoleActionDataForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.enums.CorrespondEnum;
import com.jayud.auth.model.vo.SysRoleActionDataVO;
import com.jayud.auth.service.ISysRoleActionDataService;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色数据权限 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/systemRoleActionData")
@Api(tags = "权限数据管理")
public class SysRoleActionDataController {

    @Autowired
    private ISysRoleActionDataService systemRoleActionDataService;

    @ApiOperation(value = "根据条件分页查询角色数据权限")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryForm form) {
        if (form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null) {
            return CommonResult.error(444, "该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        IPage<SysRoleActionDataVO> page = this.systemRoleActionDataService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult(page));
        } else {
            CommonPageResult<SysRoleActionDataVO> pageVO = new CommonPageResult(page);
            return CommonResult.success(pageVO);
        }
    }

    @ApiOperation(value = "添加角色数据权限")
    @PostMapping(value = "/addSystemRoleActionData")
    public CommonResult addSystemRoleActionData(@RequestBody AddSysRoleActionDataForm form) {

        if (form == null) {
            return CommonResult.success();
        }

        boolean result = systemRoleActionDataService.addSystemRoleActionData(form);
        if (result) {
            return CommonResult.success();
        }
        return CommonResult.error(444, "添加角色审核权限失败");
    }

}

