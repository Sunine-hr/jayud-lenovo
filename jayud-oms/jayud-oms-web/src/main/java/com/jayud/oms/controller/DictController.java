package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddDictForm;
import com.jayud.oms.model.bo.QueryDictForm;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.model.vo.DictVO;
import com.jayud.oms.service.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@RestController
@RequestMapping("/dict")
@Api(tags = "字典管理")
public class DictController {

    @Autowired
    private IDictService dictService;

    @ApiOperation(value = "分页查询字典")
    @PostMapping(value = "/findDictByPage")
    public CommonResult<CommonPageResult<DictVO>> findDictTypeByPage(@RequestBody QueryDictForm form) {
        IPage<DictVO> page = this.dictService.findByPage(form);
        return CommonResult.success(new CommonPageResult<>(page));
    }

    @ApiOperation(value = "新增编辑")
    @PostMapping(value = "/saveOrUpdate")
    public CommonResult saveOrUpdate(@Valid @RequestBody AddDictForm form) {
        Dict dictType = new Dict().setId(form.getId())
                .setCode(form.getCode()).setValue(form.getValue());
        if (this.dictService.checkUnique(dictType)) {
            return CommonResult.error(400, "名称或代码已经存在");
        }
        Dict tmp = ConvertUtil.convert(form, Dict.class);
        if (tmp.getId() == null) {
            tmp.setCreateTime(LocalDateTime.now());
            tmp.setCreateUser(UserOperator.getToken());
        } else {
            tmp.setCode(null);
        }
        if (this.dictService.saveOrUpdate(tmp)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "启用/禁用,id是主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        Integer id = MapUtil.getInt(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        if (this.dictService.enableOrDisable(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }

    @ApiOperation(value = "根据主键查询字典")
    @PostMapping(value = "/getById")
    public CommonResult<Dict> getById(@RequestBody Map<String, Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Dict dict = this.dictService.getById(id);
        return CommonResult.success(dict);
    }

    @ApiOperation(value = "查询字典")
    @PostMapping(value = "/findDict")
    public CommonResult<List<Dict>> findDictType(@RequestParam("dictTypeCode") String dictTypeCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dict::getDictTypeCode, dictTypeCode);
        List<Dict> dictList = this.dictService.list(queryWrapper);
        return CommonResult.success(dictList);
    }
}

