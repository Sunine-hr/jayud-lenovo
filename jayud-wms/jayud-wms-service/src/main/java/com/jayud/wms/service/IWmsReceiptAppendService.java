package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.wms.model.po.WmsReceiptAppend;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货单附加服务表 服务类
 *
 * @author jayud
 * @since 2022-03-31
 */
public interface IWmsReceiptAppendService extends IService<WmsReceiptAppend> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-31
     * @param: wmsReceiptAppend
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsReceiptAppend>
     **/
    IPage<WmsReceiptAppend> selectPage(WmsReceiptAppend wmsReceiptAppend,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-31
     * @param: wmsReceiptAppend
     * @param: req
     * @return: java.util.List<com.jayud.wms.model.po.WmsReceiptAppend>
     **/
    List<WmsReceiptAppend> selectList(WmsReceiptAppend wmsReceiptAppend);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-03-31
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);



    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-31
     * @param: queryReceiptForm
     * @param: req
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsReceiptAppendForExcel(Map<String, Object> paramMap);


}
