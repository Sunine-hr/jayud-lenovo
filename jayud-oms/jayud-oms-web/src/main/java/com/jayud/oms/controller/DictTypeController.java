package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.AddDictTypeForm;
import com.jayud.oms.model.bo.QueryDictTypeForm;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.model.po.DictType;
import com.jayud.oms.model.po.PortInfo;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.service.IDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典类型 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@RestController
@RequestMapping("/dictType")
@Api(tags = "类型名称")
public class DictTypeController {

    @Autowired
    private IDictTypeService dictTypeService;

    @ApiOperation(value = "分页查询字典类型")
    @PostMapping(value = "/findDictTypeByPage")
    public CommonResult<CommonPageResult<DictType>> findDictTypeByPage(@RequestBody QueryDictTypeForm form) {
        IPage<DictType> page = this.dictTypeService.findByPage(form);

        return CommonResult.success(new CommonPageResult<>(page));
    }

    @ApiOperation(value = "新增编辑")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult saveOrUpdate(@Valid @RequestBody AddDictTypeForm form) {
        DictType dictType = new DictType().setId(form.getId())
                .setCode(form.getCode()).setName(form.getName());
        if (this.dictTypeService.checkUnique(dictType)) {
            return CommonResult.error(400, "名称或代码已经存在");
        }
        DictType tmp = ConvertUtil.convert(form, DictType.class);
        if (tmp.getId() == null) {
            tmp.setCreateTime(LocalDateTime.now());
            tmp.setCreateUser(UserOperator.getToken());
        }
        if (this.dictTypeService.saveOrUpdate(tmp)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "获取下拉类型名称")
    @PostMapping(value = "/initDictType")
    public CommonResult<List<InitComboxStrVO>> initDictType() {
        List<DictType> list = dictTypeService.list();
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (DictType dictType : list) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(dictType.getCode());
            comboxStrVO.setName(dictType.getName());
            comboxStrVO.setId(Long.valueOf(dictType.getId()));
            comboxStrVOS.add(comboxStrVO);
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "根据主键查询字典类型")
    @PostMapping(value = "/getById")
    public CommonResult<DictType> getById(@RequestBody Map<String, Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        DictType dictType = this.dictTypeService.getById(id);
        return CommonResult.success(dictType);
    }

}

