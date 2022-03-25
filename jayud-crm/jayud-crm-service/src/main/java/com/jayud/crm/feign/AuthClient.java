package com.jayud.crm.feign;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.bo.SysLogForm;
import com.jayud.auth.model.dto.SysUserDTO;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.model.vo.SysLogVO;
import com.jayud.common.BaseResult;
import com.jayud.common.dto.QuerySysLogForm;
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

    /**
     * @description 根据租户获取角色
     * @author  ciro
     * @date   2022/3/5 10:20
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<java.util.List<com.jayud.auth.model.po.SysRole>>
     **/
    @PostMapping(value = "/sysRole/getRoleByTenantCode")
    public BaseResult<List<SysRole>>  getRoleByTenantCode(@RequestParam("tenantCode") String tenantCode);

    /**
     * @description 根据登录名称获取用户
     * @author  ciro
     * @date   2022/3/5 10:21
     * @param: tenantCode
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.dto.SysUserDTO>
     **/
    @PostMapping(value = "/sysUser/selectByUsername")
    public BaseResult<SysUserDTO>  selectByUsername(@RequestParam("username") String username);

    /**
     * @description 根据用户id查询用户信息
     * @author  ciro
     * @date   2022/3/7 14:01
     * @param: userId
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUser>
     **/
    @PostMapping(value = "/sysUser/selectByUserId")
    public BaseResult<SysUser>  selectByUserId(@RequestParam("userId") Long userId);


    @ApiOperation("公共方法新增日志")
    @PostMapping("/sysLog/api/addSysLogFeign")
    public BaseResult addSysLogFeign(@RequestParam("logContent") String logContent,@RequestParam("businessId") Long businessId);


    @ApiOperation("外部调用分页查询数据")
    @PostMapping("/sysLog/api/selectSysLogPageFeign")
    public BaseResult selectSysLogPageFeign(@RequestBody QuerySysLogForm QuerySysLogForm);

}
