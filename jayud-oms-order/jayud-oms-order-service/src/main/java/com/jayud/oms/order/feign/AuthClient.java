package com.jayud.oms.order.feign;


import com.jayud.common.BaseResult;
import com.jayud.oms.order.model.bo.CheckForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * oms-order模块消费auth模块的接口
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
//    @ApiOperation("查询法人主体")
//    @RequestMapping("/api/getLegalEntity")
//    public BaseResult<List<SysDepart>> getLegalEntity();




    /**
     * @description 根据角色编码查询用户
     * @author  ciro
     * @date   2022/3/4 9:37
     * @param: roleCode 角色编码
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.dto.SysUserDTO>>
     **/
//    @PostMapping(value = "/sysUser/selectUserByRoleCode")
//    public BaseResult<List<SysUserDTO>>  selectUserByRoleCode(@RequestParam(name = "roleCode") String roleCode);

    /**
     * 省市级联
     */
//    @PostMapping("/sysArea/api/selectListSysAreaFeign")
//    public BaseResult selectListSysAreaFeign(@RequestParam(name = "level", required = false) Integer level, @RequestParam(name = "parentCode", required = false) Long parentCode);

    /**
     * 审核
     * @param checkForm
     * @return
     */
    @PostMapping("/bPublicCheck/check")
    public BaseResult check(@RequestBody CheckForm checkForm);

    /**
     * 反审核
     * @param checkForm
     * @return
     */
    @PostMapping("/bPublicCheck/unCheck")
    public BaseResult unCheck(@RequestBody CheckForm checkForm);

    /**
     * @description 根据租户获取角色
     * @author  ciro
     * @date   2022/3/5 10:20
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysRole>>
     **/
//    @PostMapping(value = "/sysRole/getRoleByTenantCode")
//    public BaseResult<List<SysRole>>  getRoleByTenantCode(@RequestParam("tenantCode") String tenantCode);

    /**
     * @description 根据登录名称获取用户
     * @author  ciro
     * @date   2022/3/5 10:21
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.dto.SysUserDTO>
     **/
//    @PostMapping(value = "/sysUser/selectByUsername")
//    public BaseResult<SysUserDTO>  selectByUsername(@RequestParam("username") String username);

    /**
     * @description 根据用户id查询用户信息
     * @author  ciro
     * @date   2022/3/7 14:01
     * @param: userId
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUser>
     **/
//    @PostMapping(value = "/sysUser/selectByUserId")
//    public BaseResult<SysUser>  selectByUserId(@RequestParam("userId") Long userId);



}
