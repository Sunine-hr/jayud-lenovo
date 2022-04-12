package com.jayud.auth.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.SysUserForm;
import com.jayud.auth.model.dto.SysUserDTO;
import com.jayud.auth.model.po.SysUserToWarehouse;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.ISysUserToWarehouseService;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.utils.Captcha;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import com.jayud.auth.service.ISysUserService;
import com.jayud.auth.model.po.SysUser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 后台用户表 控制类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Slf4j
@Api(tags = "后台用户表")
@RestController
@RequestMapping("/sysUser")
public class SysUserController {


    @Autowired
    public ISysUserService sysUserService;
    @Autowired
    private ISysUserToWarehouseService sysUserToWarehouseService;


    /**
     * @description 分页查询
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.jayud.common.BaseResult<com.baomidou.mybatisplus.core.metadata.IPage < com.jayud.auth.model.po.SysUser>>
     **/
    @ApiOperation("分页查询数据")
    @PostMapping("/selectPage")
    public BaseResult<IPage<SysUserVO>> selectPage(@RequestBody SysUserForm sysUserForm, HttpServletRequest req) {

        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            sysUserForm.setCode(null);
        } else {
            sysUserForm.setCode(CurrentUserUtil.getUserTenantCode());
        }
        return BaseResult.ok(sysUserService.selectPage(sysUserForm, sysUserForm.getCurrentPage(), sysUserForm.getPageSize(), req));
    }


    /**
     * @description 列表查询数据
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @param: req
     * @return: com.jayud.common.BaseResult<java.util.List < com.jayud.auth.model.po.SysUser>>
     **/
    @ApiOperation("列表查询数据")
    @PostMapping("/selectList")
    public BaseResult<List<SysUserVO>> selectList(@RequestBody SysUser sysUser,
                                                  HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectList(sysUser));
    }

    @ApiOperation("列表查询数据")
    @PostMapping("/api/selectListFeign")
    public BaseResult selectListFeign() {
        SysUser sysUser = new SysUser();
        if (CurrentUserUtil.hasRole(CommonConstant.SUPER_TENANT)) {
            sysUser.setCode(null);
        } else {
            sysUser.setCode(CurrentUserUtil.getUserTenantCode());
        }
        List<SysUserVO> sysUserVOS = sysUserService.selectList(sysUser);
        System.out.println("远程调用查询到的数据：" + sysUserVOS);
        return BaseResult.ok(sysUserVOS);
    }

    /**
     * @description 根据ids查询数据  没用到
     **/
    @ApiOperation("根据ids查询数据")
    @PostMapping("/selectIdsList")
    public BaseResult<List<SysUserVO>> selectIdsList(@RequestBody DeleteForm ids) {
        //需要写一个根据id集合查询列表信息
        SysUserForm sysUserForm = new SysUserForm();
        sysUserForm.setRoleIds(ids.getIds());
        List<SysUserVO> sysUserVOS = sysUserService.selectIdsList(sysUserForm);
        return BaseResult.ok(sysUserVOS);
    }


    /**
     * @description 新增
     * @author jayud
     * @date 2022-02-21
     * @param: sysUser
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("新增")
    @PostMapping("/saveOrUpdate")
    public BaseResult add(@RequestBody SysUserForm sysUserForm) {
        if (sysUserForm == null) {
            return BaseResult.error("数据不能为空！");
        }
        if (sysUserForm.getId() == null) {
            SysUser sysUserNameOne = sysUserService.findSysUserNameOne(sysUserForm);
            if (sysUserNameOne != null) {
                return BaseResult.error("用户名已存在！");
            }
        }
        if (sysUserForm.getId() != null) {
            //校验非当前的用户名称
            LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysUser::getName, sysUserForm.getName());
            lambdaQueryWrapper.eq(SysUser::getIsDeleted, false);
            lambdaQueryWrapper.ne(SysUser::getId, sysUserForm.getId());
            SysUser one = sysUserService.getOne(lambdaQueryWrapper);
            if (one != null) {
                return BaseResult.error("用户名已存在！");
            }
        }
        boolean b = sysUserService.saveOrUpdateSysUser(sysUserForm);
        if (b) {
            return BaseResult.ok();
        }
        return BaseResult.error(SysTips.ADD_SUCCESS);
    }


//    /**
//     * @description 编辑
//     * @author jayud
//     * @date 2022-02-21
//     * @param: sysUser
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("编辑")
//    @PostMapping("/edit")
//    public BaseResult edit(@Valid @RequestBody SysUser sysUser) {
//        sysUserService.updateById(sysUser);
//        return BaseResult.ok(SysTips.EDIT_SUCCESS);
//    }


//    /**
//     * @description 物理删除
//     * @author jayud
//     * @date 2022-02-21
//     * @param: id
//     * @return: com.jayud.common.BaseResult
//     **/
//    @ApiOperation("物理删除")
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true)
//    @GetMapping("/phyDel")
//    public BaseResult phyDel(@RequestParam Long id) {
//        sysUserService.phyDelById(id);
//        return BaseResult.ok(SysTips.DEL_SUCCESS);
//    }

    /**
     * @description 逻辑删除
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult
     **/
    @ApiOperation("逻辑删除")
    @PostMapping("/delSysUser")
    public BaseResult logicDel(@RequestBody DeleteForm ids) {
        if (ids.getIds().size() == 0) {
            return BaseResult.error("id不为空");
        }

        return sysUserService.deleteSysUser(ids.getIds());
    }


    /**
     * @description 根据id查询
     * @author jayud
     * @date 2022-02-21
     * @param: id
     * @return: com.jayud.common.BaseResult<com.jayud.auth.model.po.SysUser>
     **/
    @ApiOperation("根据id查询")
    @GetMapping(value = "/queryById")
    public BaseResult<SysUserVO> queryById(@RequestParam(name = "id", required = true) Long id) {
        SysUserVO sysUserIdOne = null;
        SysUserForm sysUserForm = new SysUserForm();
        sysUserForm.setId(id);
        //关联查询用户信息 关联表 行合并  成列
        sysUserIdOne = sysUserService.findSysUserIdOne(sysUserForm);
        if (sysUserIdOne == null) {
            return BaseResult.error("用户不存在！");
        }

        //拿到的部门的集合
        List<Long> listId = new ArrayList<>();
        if (sysUserIdOne.getDepartmentList() != null) {
            String departmentList = sysUserIdOne.getDepartmentList();
            String[] aa = departmentList.split(",");
            for (int i = 0; i < aa.length; i++) {
                long l = Long.parseLong(aa[i]);
                listId.add(l);
            }
        }
        sysUserIdOne.setDepartIdLists(listId);
        //拿到角色的集合
        List<Long> list = new ArrayList<>();
        String s = sysUserIdOne.getRoleListIdString();
        String[] a = s.split(",");
        for (int i = 0; i < a.length; i++) {
            list.add(Long.parseLong(a[i]));
        }
        sysUserIdOne.setRoleIds(list);
        SysUserToWarehouse warehouse = new SysUserToWarehouse();
        warehouse.setUserId(sysUserIdOne.getId());
        List<SysUserToWarehouse> warehouseList = sysUserToWarehouseService.selectList(warehouse);
        if (CollUtil.isNotEmpty(warehouseList)) {
            sysUserIdOne.setWarehouseList(warehouseList);
        } else {
            sysUserIdOne.setWarehouseList(new ArrayList<>());
        }
        return BaseResult.ok(sysUserIdOne);
    }


    @ApiOperation("修改密码")
    @PostMapping("/updateUserPassword")
    public BaseResult updateUserPassword(@RequestBody SysUserForm sysUserForm) {
        sysUserService.findUpdateUserPassword(sysUserForm);
        return BaseResult.ok();
    }

    @ApiOperation("根据角色编码查询用户")
    @ApiImplicitParam(name = "roleCode", value = "角色编码", dataType = "String", required = true)
    @PostMapping("/selectUserByRoleCode")
    public BaseResult<List<SysUserDTO>> selectUserByRoleCode(@RequestParam String roleCode,
                                                             HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectUserByRoleCode(roleCode));
    }

    @ApiOperation("根据登录名称查询用户")
    @ApiImplicitParam(name = "username", value = "登录名称", dataType = "String", required = true)
    @PostMapping("/selectByUsername")
    public BaseResult<SysUserDTO> selectByUsername(@RequestParam String username,
                                                   HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectByUsername(username));
    }

    @ApiOperation("根据用户id称查询用户")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    @PostMapping("/selectByUserId")
    public BaseResult<SysUser> selectByUserId(@RequestParam Long userId,
                                              HttpServletRequest req) {
        return BaseResult.ok(sysUserService.selectByUserId(userId));
    }

    @ApiOperation("根据仓库查询用户")
    @GetMapping("/getUserByWarehouse")
    public BaseResult<List<SysUser>> getUserByWarehouse(SysUserToWarehouse sysUserToWarehouse) {
        return BaseResult.ok(sysUserService.getUserByWarehouse(sysUserToWarehouse));
    }

    /**
     * 调取获得验证码  获取验证码不重复 传时间戳
     * http://localhost:8001/jayudAuth/sysUser/captcha?date=12312
     * @param request
     * @param response
     * @param username
     * @return
     * @throws Exception
     */
    @ApiOperation("调取获得验证码")
    @GetMapping(value = "/captcha")
    public BaseResult imagecode(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String username) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        OutputStream os = response.getOutputStream();
        //返回验证码和图片的map
        Map<String, Object> map = Captcha.getImageCode(86, 37, os);
        String simpleCaptcha = "simpleCaptcha";
        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        request.getSession().setAttribute("codeTime", System.currentTimeMillis());

        try {
            ImageIO.write((BufferedImage) map.get("image"), "jpg", os);
        } catch (IOException e) {
            return BaseResult.error();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
        return null;
    }

    /**
     * @param checkCode 前端用户输入返回的验证码
     *                  参数若需要，自行添加
     */
    @ApiOperation("前端用户输入返回的验证码")
    @GetMapping(value = "/verify")
    public BaseResult checkcode(HttpServletRequest request,
                                HttpSession session,
                                @RequestParam String checkCode) throws Exception {

        // 获得验证码对象
        Object cko = session.getAttribute("simpleCaptcha");
        if (cko == null) {
            request.setAttribute("errorMsg", "请输入验证码！");
            return BaseResult.ok("请输入验证码！");
        }
        String captcha = cko.toString();
        // 判断验证码输入是否正确
        //验证码创建时间
        String codeTime1 = session.getAttribute("codeTime") + "";
        System.out.println("时间戳：" + codeTime1);
        Long aLong = Long.valueOf(codeTime1);
        //当前时间
        long l = System.currentTimeMillis();
        System.out.println("当前时间戳：" + l);
        long l1 = l - aLong;
        // 验证码有效时长为1分钟
        if (l1 / 1000 / 60 > 0.2) {

            System.out.println("进入到方法！超时时间");
            request.setAttribute("errorMsg", "验证码已失效，请重新输入！");
            return BaseResult.error("验证码已失效，请重新输入！");

        } else {
            if (StringUtils.isEmpty(checkCode) || captcha == null || !(checkCode.equalsIgnoreCase(captcha))) {
                request.setAttribute("errorMsg", "验证码错误！");
                System.out.println("进入到方法！验证码是否错误！");
                return BaseResult.error("验证码错误，请重新输入！");

            } else {
                // 在这里可以处理自己需要的事务，比如验证登陆等

                return BaseResult.ok("验证通过！");
            }
        }
    }
}
