package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.InventoryCheckForm;
import com.jayud.wms.model.bo.WorkbenchCheckAffirmForm;
import com.jayud.wms.model.po.InventoryCheck;
import com.jayud.wms.model.po.InventoryCheckDetail;
import com.jayud.wms.model.vo.InventoryCheckAppVO;
import com.jayud.wms.model.vo.InventoryCheckVO;
import com.jayud.wms.model.vo.WorkbenchCheckVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点表 服务类
 *
 * @author jyd
 * @since 2021-12-27
 */
public interface IInventoryCheckService extends IService<InventoryCheck> {

    /**
    *  分页查询
    * @param inventoryCheck
    * @param req
    * @return
    */
    IPage<InventoryCheck> selectPage(InventoryCheck inventoryCheck,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param inventoryCheck
    * @return
    */
    List<InventoryCheck> selectList(InventoryCheck inventoryCheck);

    /**
     * 保存(新增+编辑)
     * @param inventoryCheck
     */
    InventoryCheck saveOrUpdateInventoryCheck(InventoryCheck inventoryCheck);

    /**
     * 逻辑删除
     * @param id
     */
    void delInventoryCheck(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryInventoryCheckForExcel(Map<String, Object> paramMap);

    /**
     * 生成盘点任务
     * @param bo
     * @return
     */
    InventoryCheckVO generateInventoryCheck(InventoryCheckForm bo);

    /**
     * 查看盘点报告
     * @param id
     * @return
     */
    InventoryCheckVO lookInventoryCheck(int id);

    /**
     * 确认完成盘点
     * @param id
     * @return
     */
    boolean confirmCompleteInventoryCheck(int id);

    /**
     * 分页查询数据Feign
     */
    IPage<InventoryCheckAppVO> selectPageByFeign(InventoryCheck inventoryCheck,
                                                 Integer pageNo,
                                                 Integer pageSize,
                                                 HttpServletRequest req);

    /**
     * 取盘点(盘点详情)
     * @param checkCode
     * @return
     */
    InventoryCheckAppVO queryInventoryCheckByCheckCode(String checkCode);

    /**
     * 盘点确认
     * @param inventoryCheckAppVO
     * @return
     */
    InventoryCheckAppVO inventoryCheckCompletedByApp(InventoryCheckAppVO inventoryCheckAppVO);

    /**
     * 工作台盘点确认，货架+物料+数量(盘点数量)+库位编号
     * @param form
     */
    void workbenchCheckAffirm(WorkbenchCheckAffirmForm form);

    /**
     * 工作台盘点，查询货架号对应的示意图和盘点任务
     * @param form
     * @return
     */
    WorkbenchCheckVO workbenchCheckQueryByShelfCode(WorkbenchCheckAffirmForm form);

    /**
     * 工作台盘点，撤销盘点
     * @param inventoryCheckDetails
     */
    void workbenchCheckRevocation(List<InventoryCheckDetail> inventoryCheckDetails);

    /**
     * 工作台盘点，货架回库
     * @param form
     */
    void workbenchCheckBackLibrary(WorkbenchCheckAffirmForm form);
}
