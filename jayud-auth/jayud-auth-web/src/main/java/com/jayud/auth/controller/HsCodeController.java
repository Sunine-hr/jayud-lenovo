package com.jayud.auth.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddHsCodeForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.enums.CorrespondEnum;
import com.jayud.auth.model.vo.HsCodeFormVO;
import com.jayud.auth.model.vo.HsCodeVO;
import com.jayud.auth.service.IHsCodeService;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 海关编码表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/hsCode")
@Api(tags = "海关编码管理")
public class HsCodeController {

    @Autowired
    private IHsCodeService hsCodeService;

    @ApiOperation(value = "海关编码列表")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@Valid @RequestBody QueryForm form) {

        if(form.getKey() != null && CorrespondEnum.getName(form.getKey()) == null){
            return CommonResult.error(444,"该条件无法搜索");
        }
        form.setKey(CorrespondEnum.getName(form.getKey()));

        List list = new ArrayList();
        //获取表头信息
        Class<HsCodeFormVO> seaOrderFormVOClass = HsCodeFormVO.class;
        Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }
        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<HsCodeFormVO> page = this.hsCodeService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
        }else{
            CommonPageResult<HsCodeFormVO> pageVO = new CommonPageResult(page);
            map1.put("pageInfo", pageVO);
        }
        return CommonResult.success(map1);
    }

    @ApiOperation(value = "增加或修改信息")
    @PostMapping(value = "/saveOrUpdateHsCode")
    public CommonResult saveOrUpdateHsCode(@Valid @RequestBody AddHsCodeForm form) {
        boolean result = hsCodeService.saveOrUpdateHsCode(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"添加或修改海关编码失败");

    }

    @ApiOperation(value = "根据编码id获取编码详情")
    @PostMapping(value = "/getHsCodeDetail")
    public CommonResult getHsCodeDetail(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        HsCodeVO hsCodeVO = hsCodeService.getHsCodeDetail(id);
        return CommonResult.success(hsCodeVO);
    }

    @ApiOperation(value = "根据海关编码获取编码详情")
    @PostMapping(value = "/getHsCodeDetailByCodeNo")
    public CommonResult getHsCodeDetailByCodeNo(@RequestBody Map<String,Object> map) {
        String codeNo = MapUtil.getStr(map, "codeNo");
        HsCodeVO hsCodeVO = hsCodeService.getHsCodeDetailByCodeNo(codeNo);
        return CommonResult.success(hsCodeVO);
    }

}

