package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.NoticeSnMaterialForm;
import com.jayud.wms.model.po.NoticeSnMaterial;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知单物料sn信息 服务类
 *
 * @author jyd
 * @since 2021-12-18
 */
public interface INoticeSnMaterialService extends IService<NoticeSnMaterial> {

    /**
    *  分页查询
    * @param noticeSnMaterial
    * @param req
    * @return
    */
    IPage<NoticeSnMaterial> selectPage(NoticeSnMaterial noticeSnMaterial,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param noticeSnMaterial
    * @return
    */
    List<NoticeSnMaterial> selectList(NoticeSnMaterial noticeSnMaterial);

    /**
     * 保存(新增+编辑)
     * @param noticeSnMaterial
     */
//    NoticeSnMaterial saveOrUpdateNoticeSnMaterial(NoticeSnMaterial noticeSnMaterial);

    /**
     * 逻辑删除
     * @param id
     */
    void delNoticeSnMaterial(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryNoticeSnMaterialForExcel(Map<String, Object> paramMap);


    void createOrder(Long id, String receiptNoticeNum, List<NoticeSnMaterialForm> noticeSnMaterialForms);

    List<NoticeSnMaterial> getByCondition(NoticeSnMaterial condition);
}
