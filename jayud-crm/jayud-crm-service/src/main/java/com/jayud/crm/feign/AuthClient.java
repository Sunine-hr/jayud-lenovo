package com.jayud.crm.feign;


import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.dto.SysUserDTO;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysRole;
import com.jayud.common.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * customs模块消费file模块的接口
 */
@Component
//@FeignClient(name = "jayud-auth-web" ,path = "/jayudAuth/sysUser")
@FeignClient(name = "jayud-auth-web")
public interface AuthClient {


    @PostMapping("/sysUser/api/selectListFeign")
    public BaseResult selectListFeign();
    /**
     * 字段表查询
     */
    @PostMapping(value = "/sysDictItem/api/selectItemByDictCode")
    public BaseResult selectItemByDictCodeFeign(@RequestParam("dictCode") String dictCode);


    /**
     * 自定义单号
     */
    @PostMapping(value = "/bNoRule/api/getOrderFeign")
    public BaseResult getOrderFeign(@RequestParam(name = "code") String code, @RequestParam(name = "date") Date date);

    /**
     * 查询法人主体
     * @description 查询法人主体
     **/
    @ApiOperation("查询法人主体")
    @RequestMapping("/api/getLegalEntity")
    public BaseResult<List<SysDepart>> getLegalEntity();




    /**
     * @description 根据角色编码查询用户
     * @author  ciro
     * @date   2022/3/4 9:37
     * @param: roleCode 角色编码
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.dto.SysUserDTO>>
     **/
    @PostMapping(value = "/sysUser/selectUserByRoleCode")
    public BaseResult<List<SysUserDTO>>  selectUserByRoleCode(@RequestParam(name = "roleCode") String roleCode);

    /**
     * 省市级联
     */
    @PostMapping("/sysArea/api/selectListSysAreaFeign")
    public BaseResult selectListSysAreaFeign(@RequestParam(name = "level", required = false) Integer level, @RequestParam(name = "parentCode", required = false) Long parentCode);

    /**
     * 审核
     * @param checkForm
     * @return
     */
    @PostMapping("/bPublicCheck/check")
    public BaseResult check(@RequestBody CheckForm checkForm);

    @PostMapping(value = "/sysRole/getRoleByTenantCode")
    public BaseResult<List<SysRole>>  getRoleByTenantCode(@RequestParam("tenantCode") String tenantCode);


}
