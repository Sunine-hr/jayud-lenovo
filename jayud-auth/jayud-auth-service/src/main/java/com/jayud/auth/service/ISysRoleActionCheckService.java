package com.jayud.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.bo.SysRoleActionCheckForm;
import com.jayud.auth.model.po.SysRoleActionCheck;
import com.jayud.auth.model.vo.SysRoleActionCheckVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色审核级别权限 服务类
 *
 * @author jayud
 * @since 2022-03-01
 */
public interface ISysRoleActionCheckService extends IService<SysRoleActionCheck> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-01
     * @param: sysRoleActionCheck
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.SysRoleActionCheck>
     **/
    IPage<SysRoleActionCheckVO> selectPage(SysRoleActionCheck sysRoleActionCheck,
                                           Integer currentPage,
                                           Integer pageSize,
                                           HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-01
     * @param: sysRoleActionCheck
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.SysRoleActionCheck>
     **/
    List<SysRoleActionCheck> selectList(SysRoleActionCheck sysRoleActionCheck);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-01
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-01
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-01
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> querySysRoleActionCheckForExcel(Map<String, Object> paramMap);

    /**
     * 新增审核权限
     * @param sysRoleActionCheck
     */
    void saveSysRoleActionCheck(SysRoleActionCheckForm sysRoleActionCheck);

    /**
     * 获取按钮有几个审核级别
     * @param checkForm
     * @return
     */
    List<SysRoleActionCheckVO> getList(CheckForm checkForm);

    /**
     * 获取该级审核有哪些角色
     * @param checkLevel
     * @param menuCode
     * @return
     */
    List<SysRoleActionCheckVO> getListByCheckLevelAndMenuCode(Integer checkLevel, String menuCode);
}
