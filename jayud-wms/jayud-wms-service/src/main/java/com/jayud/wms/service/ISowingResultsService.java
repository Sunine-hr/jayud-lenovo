package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QuerySowingResultsForm;
import com.jayud.wms.model.po.SowingResults;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SowingResultsVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 播种结果 服务类
 *
 * @author jyd
 * @since 2021-12-23
 */
public interface ISowingResultsService extends IService<SowingResults> {

    /**
    *  分页查询
    * @param querySowingResultsForm
    * @param req
    * @return
    */
    IPage<SowingResultsVO> selectPage(QuerySowingResultsForm querySowingResultsForm,
                                      Integer pageNo,
                                      Integer pageSize,
                                      HttpServletRequest req);

    /**
    *  查询列表
    * @param querySowingResultsForm
    * @return
    */
    List<SowingResultsVO> selectList(QuerySowingResultsForm querySowingResultsForm);

    /**
     * 保存(新增+编辑)
     * @param sowingResults
     */
    SowingResults saveOrUpdateSowingResults(SowingResults sowingResults);

    /**
     * 逻辑删除
     * @param id
     */
    void delSowingResults(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> querySowingResultsForExcel(Map<String, Object> paramMap);


    List<SowingResults> getByCondition(SowingResults condition);

    List<SeedingWallLayoutTwoVo> sketchMap();

    void cancel(Long id);
}
