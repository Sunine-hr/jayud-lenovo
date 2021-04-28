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
import com.jayud.oauth.model.vo.TrainingManagementVO;
import com.jayud.oauth.service.ITrainingManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
        for (TrainingManagementVO record : list.getRecords()) {
            record.setFileViewList(StringUtils.getFileViews(record.getFilePath(),
                    record.getFileName(), url.toString()));
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

