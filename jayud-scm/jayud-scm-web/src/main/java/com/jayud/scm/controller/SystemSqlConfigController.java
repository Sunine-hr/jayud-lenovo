package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.scm.model.bo.QueryCommonConfigForm;
import com.jayud.scm.model.bo.QuerySystemSqlConfigForm;
import com.jayud.scm.model.bo.SystemSqlConfigForm;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.SystemSqlConfigVO;
import com.jayud.scm.model.vo.TableColumnVO;
import com.jayud.scm.service.ISystemSqlConfigService;
import com.jayud.scm.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SQL数据源配置 前端控制器
 * </p>
 *
 * @author mfc
 * @since 2021-08-09
 */
@RestController
@Api(tags = "SQL数据源配置")
@RequestMapping("/systemSqlConfig")
public class SystemSqlConfigController {

    @Autowired
    private ISystemSqlConfigService systemSqlConfigService;
    @Autowired
    private ISystemUserService systemUserService;

    @ApiOperation(value = "根据条件分页查询,SQL数据源配置")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<SystemSqlConfigVO>> findByPage(@Valid @RequestBody QuerySystemSqlConfigForm form){
        IPage<SystemSqlConfigVO> page = systemSqlConfigService.findByPage(form);
        CommonPageResult<SystemSqlConfigVO> pageVO = new CommonPageResult(page);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "根据id查询，SQL数据源配置详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/getSystemSqlConfigById")
    public CommonResult<SystemSqlConfigVO> getSystemSqlConfigById(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigService.getSystemSqlConfigById(id);
        return CommonResult.success(systemSqlConfigVO);
    }

    @ApiOperation(value = "保存（新增和修改）,SQL数据源配置")
    @PostMapping(value = "/saveSystemSqlConfig")
    public CommonResult saveSystemSqlConfig(@Valid @RequestBody SystemSqlConfigForm form){
        systemSqlConfigService.saveSystemSqlConfig(form);
        return CommonResult.success("保存成功!");
    }

    @ApiOperation(value = "根据id删除,SQL数据源配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", dataType = "Integer", value = "自动ID", required = true)
    })
    @PostMapping(value = "/delSystemSqlConfig")
    public CommonResult delSystemSqlConfig(@RequestBody Map<String,Object> map){
        Integer id = MapUtil.getInt(map, "id");
        if(ObjectUtil.isEmpty(id)){
            return CommonResult.error(-1,"id不能为空");
        }
        systemSqlConfigService.delSystemSqlConfig(id);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "根据SQL代码,获取数据源查询展示列集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sqlCode", dataType = "string", value = "SQL代码", required = true)
    })
    @RequestMapping(value = "/getTableColumnCookies",method = RequestMethod.POST)
    public CommonResult getTableColumnCookies(HttpServletResponse response, @RequestBody Map<String,Object> map){
        String sqlCode = MapUtil.getStr(map, "sqlCode");
        if(ObjectUtil.isEmpty(sqlCode)){
            return CommonResult.error(-1,"SQL代码，不能为空");
        }
        List<TableColumnVO> tableColumnList = systemSqlConfigService.getTableColumn(sqlCode);

        //JSON数据
        //list -> json
        String json = JSONObject.toJSONString(tableColumnList);
        //json -> list
        List<TableColumnVO> lsit = JSON.parseObject(json, new TypeReference<List<TableColumnVO>>() {});

        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录!");
        }
        Long userId = systemUser.getId();
        //HttpServerletRequest  装请求信息的类
        //HttpServerletResponse  装相应信息的类
        Cookie cookie = null;
        try {
            //TableColumn + sqlCode + userId
            cookie = new Cookie("TableColumn_"+sqlCode+"_"+userId, URLEncoder.encode(json, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addCookie(cookie);

        return CommonResult.success(json);
    }

    /*
        根据sql代码，查询数据
        查询条件
            1.普通查询参数
            2.数据权限查询参数
        数据结果
            1.list(明细记录)
            2.count(汇总)
            3.无数据的返回消息
    */
    @ApiOperation(value = "根据SQL代码和通用条件分页查询,返回泛型map")
    @PostMapping(value = "/findCommonByPage")
    public CommonResult<CommonPageResult<Map<String, Object>>> findCommonByPage(@Valid @RequestBody QueryCommonConfigForm form){
        CommonPageResult<Map<String, Object>> pageVO = systemSqlConfigService.findCommonByPage(form);
        return CommonResult.success(pageVO);
    }



}

