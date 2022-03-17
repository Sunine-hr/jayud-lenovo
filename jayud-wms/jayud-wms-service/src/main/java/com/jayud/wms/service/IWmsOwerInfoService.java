package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.WmsOwerInfoForm;
import com.jayud.wms.model.po.WmsOwerInfo;
import com.jayud.wms.model.vo.WmsOwerInfoVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 货主信息 服务类
 *
 * @author jyd
 * @since 2021-12-13
 */
public interface IWmsOwerInfoService extends IService<WmsOwerInfo> {

        /**
         *  分页查询
         * @param wmsOwerInfo
         * @param pageNo
         * @param pageSize
         * @param req
         * @return
         */
        IPage<WmsOwerInfo> selectPage(WmsOwerInfo wmsOwerInfo,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

        /**
         *  查询列表
         * @param wmsOwerInfo
         * @return
         */
        List<WmsOwerInfo> selectList(WmsOwerInfo wmsOwerInfo);

        /**\
         * 新增或者修改
         * @param wmsOwerInfoForm
         * @return
         */
        boolean saveOrUpdateWmsCustomerInfo(WmsOwerInfoForm wmsOwerInfoForm);


        WmsOwerInfo  getWmsCustomerInfoCodeName(@Param("owerCode")String owerCode,@Param("owerName") String owerName);

        WmsOwerInfoVO getWmsCustomerInfoVOCodeName(@Param("id")Long id);

        /**
         *  根据仓库ID查询 货主信息
         * @param wmsOwerInfoForm
         * @return
         */
        List<WmsOwerInfo> selectWmsOwerInfoWarehouseIdList(WmsOwerInfoForm wmsOwerInfoForm);


        // 导出
        List<LinkedHashMap<String, Object>> queryWmsOwerInfoForExcel(WmsOwerInfo wmsOwerInfo,
                                                                     Integer pageNo,
                                                                     Integer pageSize,
                                                                     HttpServletRequest req);
        /**
         * @description 根据货主信息查询
         * @author  ciro
         * @date   2022/1/17 17:56
         * @param: owerId
         * @param: owerCode
         * @return: com.jayud.model.po.WmsOwerInfo
         **/
        WmsOwerInfo selectByOwerMsg(Long owerId,String owerCode);
}
