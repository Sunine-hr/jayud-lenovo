package com.jayud.oauth.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oauth.feign.FileClient;
import com.jayud.oauth.model.bo.AddTrainingManagementForm;
import com.jayud.oauth.model.bo.QueryTrainingManagementFrom;
import com.jayud.oauth.model.po.TrainingManagement;
import com.jayud.oauth.model.vo.DepartmentVO;
import com.jayud.oauth.model.vo.TrainingManagementVO;
import com.jayud.oauth.service.ISystemDepartmentService;
import com.jayud.oauth.service.ISystemUserService;
import com.jayud.oauth.service.ITrainingManagementService;
import com.jayud.oauth.service.impl.SystemDepartmentServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 培训管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-04-27
 */
@RestController
@RequestMapping("/trainingManagement")
public class TrainingManagementController {

    @Autowired
    private ITrainingManagementService trainingManagementService;
    @Autowired
    private ISystemDepartmentService departmentService;
    @Autowired
    private FileClient fileClient;

    @ApiOperation("创建/编辑")
    @PostMapping(value = "/addOrUpdate")
    public CommonResult addOrUpdate(@RequestBody AddTrainingManagementForm form) {
        form.checkAdd();
        this.trainingManagementService.addOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation("分页查询")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<TrainingManagementVO>> findByPage(@RequestBody QueryTrainingManagementFrom form) {
        IPage<TrainingManagementVO> list = this.trainingManagementService.findByPage(form);
        Object url = fileClient.getBaseUrl().getData();
        //培训对象
        List<DepartmentVO> departments = departmentService.findDepartment(null);
        Map<Long, String> departmentMap = departments.stream().collect(Collectors.toMap(DepartmentVO::getId, DepartmentVO::getName));
        for (TrainingManagementVO record : list.getRecords()) {
            record.setFileViewList(StringUtils.getFileViews(record.getFilePath(),
                    record.getFileName(), url.toString()));
            record.assembleTraineesDesc(departmentMap);
        }

        return CommonResult.success(new CommonPageResult<>(list));
    }


    @ApiOperation("获取详情")
    @PostMapping(value = "/getById")
    public CommonResult<TrainingManagementVO> getById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        TrainingManagement trainingManagement = this.trainingManagementService.getById(id);

        TrainingManagementVO trainingManagementVO = ConvertUtil.convert(trainingManagement, TrainingManagementVO.class);

        Object url = fileClient.getBaseUrl().getData();
        trainingManagementVO.assembleTrainees(trainingManagement.getTrainees()).setFileViewList(StringUtils.getFileViews(trainingManagement.getFilePath(),
                trainingManagement.getFileName(), url.toString()));

        return CommonResult.success(trainingManagementVO);
    }


}

