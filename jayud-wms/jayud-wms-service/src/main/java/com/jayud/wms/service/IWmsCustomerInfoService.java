package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.WmsCustomerInfoForm;
import com.jayud.wms.model.dto.WmsCustomerInfoDTO;
import com.jayud.wms.model.po.WmsCustomerInfo;
import com.jayud.wms.model.vo.WmsCustomerInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * <p>
 * 客户信息
 * </p>
 *
 */
public interface IWmsCustomerInfoService extends IService<WmsCustomerInfo> {

    /**
     *  分页查询
     * @param wmsCustomerInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<WmsCustomerInfoVO> selectPage(WmsCustomerInfo wmsCustomerInfo,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req);
    /**
     *  查询列表
     * @param wmsCustomerInfo
     * @return
     */
    List<WmsCustomerInfoVO> selectList(WmsCustomerInfo wmsCustomerInfo);

    /**\
     * 新增或者修改
     * @param wmsCustomerInfoForm
     * @return
     */
    void saveOrUpdateWmsCustomerInfo(WmsCustomerInfoForm wmsCustomerInfoForm);

    /**
     * 根据名字查询
     * @param customerCode
     * @param customerName
     * @return
     */
    WmsCustomerInfo getWmsCustomerInfoByName(String customerCode, String customerName);


    /**
     * 校验编码和  名称
     * @param customerCode
     * @param customerName
     * @return
     */
    WmsCustomerInfo getWmsCustomerInfoByCode(String customerCode, String customerName);


    //导出
    List<LinkedHashMap<String, Object>> queryWmsCustomerInfoForExcel(WmsCustomerInfo wmsCustomerInfo,HttpServletRequest req);


    /**
     * 获取仓库DTO
     * @param appId
     * @return
     */
    WmsCustomerInfoDTO getCustomerInfoDTOByAppId(String appId);
}
