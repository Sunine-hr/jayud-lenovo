package com.jayud.crm.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.BaseResult;
import com.jayud.crm.model.bo.QueryCrmFile;
import com.jayud.crm.model.po.CrmFile;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_文件(crm_file) 服务类
 *
 * @author jayud
 * @since 2022-03-02
 */
public interface ICrmFileService extends IService<CrmFile> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-02
     * @param: crmFile
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.crm.model.po.CrmFile>
     **/
    IPage<CrmFile> selectPage(CrmFile crmFile,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-02
     * @param: crmFile
     * @param: req
     * @return: java.util.List<com.jayud.crm.model.po.CrmFile>
     **/
    List<CrmFile> selectList(CrmFile crmFile);


    BaseResult saveOrUpdateCrmFile(QueryCrmFile queryCrmFile);
    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-02
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-02
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(List<Long> ids);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-02
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryCrmFileForExcel(Map<String, Object> paramMap);

    /**
     * 文件处理
     * @param files
     * @param id
     * @param code
     */
    void doFileProcessing(List<CrmFile> files, Long businessId, String code);

    List<CrmFile> getFiles(Long id, String code);
}
