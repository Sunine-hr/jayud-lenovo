package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.MaterialSn;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料sn信息 服务类
 *
 * @author jyd
 * @since 2021-12-21
 */
public interface IMaterialSnService extends IService<MaterialSn> {

    /**
    *  分页查询
    * @param materialSn
    * @param req
    * @return
    */
    IPage<MaterialSn> selectPage(MaterialSn materialSn,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param materialSn
    * @return
    */
    List<MaterialSn> selectList(MaterialSn materialSn);

    /**
     * 保存(新增+编辑)
     * @param materialSn
     */
    MaterialSn saveOrUpdateMaterialSn(MaterialSn materialSn);

    /**
     * 逻辑删除
     * @param id
     */
    void delMaterialSn(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryMaterialSnForExcel(Map<String, Object> paramMap);


    void updateByCondition(MaterialSn materialSn, MaterialSn condition);

    List<MaterialSn> getByCondition(MaterialSn condition);
}
