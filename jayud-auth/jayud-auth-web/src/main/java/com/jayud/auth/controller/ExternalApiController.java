package com.jayud.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.service.ISysDepartService;
import com.jayud.common.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@Api(tags = "auth对外接口")
@RequestMapping("/api")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ISysDepartService departService;

    /**
     * @description 查询法人主体
     **/
    @ApiOperation("查询法人主体")
    @PostMapping("/getLegalEntity")
    public BaseResult<List<SysDepart>> getLegalEntity() {
        List<SysDepart> sysDeparts = departService.getByOrgCategory(2);
        return BaseResult.ok(sysDeparts);
    }

}