package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.BreakoutWorkbenchForm;
import com.jayud.wms.model.po.BreakoutWorkbench;
import com.jayud.wms.model.vo.WorkbenchVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台类型信息 服务类
 *
 * @author jyd
 * @since 2021-12-17
 */
public interface IBreakoutWorkbenchService extends IService<BreakoutWorkbench> {

    /**
    *  分页查询
    * @param breakoutWorkbench
    * @param req
    * @return
    */
    IPage<BreakoutWorkbench> selectPage(BreakoutWorkbench breakoutWorkbench,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param breakoutWorkbench
    * @return
    */
    List<BreakoutWorkbench> selectList(BreakoutWorkbench breakoutWorkbench);

    /**
     * 保存(新增+编辑)
     * @param breakoutWorkbench
     */
    BreakoutWorkbench saveOrUpdateBreakoutWorkbench(BreakoutWorkbench breakoutWorkbench);

    /**
     * 逻辑删除
     * @param id
     */
    void delBreakoutWorkbench(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryBreakoutWorkbenchForExcel(Map<String, Object> paramMap);

    /**
     * 根据工作台id，获取工作台业务信息
     * @param workbenchId
     * @return
     */
    WorkbenchVO queryByWorkbenchId(Integer workbenchId);

    /**
     * 保存工作台业务信息
     * @param bo
     */
    void saveBreakoutWorkbench(WorkbenchVO bo);

    /**
     * 根据工作台id，添加播种墙编号
     * @param bo
     * @return
     */
    BreakoutWorkbench addBreakoutWorkbench(BreakoutWorkbenchForm bo);
}
