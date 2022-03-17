package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsCustomerDevelopmentSetting;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户开发设置表 服务类
 *
 * @author jyd
 * @since 2022-02-14
 */
public interface IWmsCustomerDevelopmentSettingService extends IService<WmsCustomerDevelopmentSetting> {

    /**
    *  分页查询
    * @param wmsCustomerDevelopmentSetting
    * @param req
    * @return
    */
    IPage<WmsCustomerDevelopmentSetting> selectPage(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsCustomerDevelopmentSetting
    * @return
    */
    List<WmsCustomerDevelopmentSetting> selectList(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting);

    /**
     * 保存(新增+编辑)
     * @param wmsCustomerDevelopmentSetting
     */
    WmsCustomerDevelopmentSetting saveOrUpdateWmsCustomerDevelopmentSetting(WmsCustomerDevelopmentSetting wmsCustomerDevelopmentSetting);

    /**
     * 逻辑删除
     * @param id
     */
    void delWmsCustomerDevelopmentSetting(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsCustomerDevelopmentSettingForExcel(Map<String, Object> paramMap);


}
