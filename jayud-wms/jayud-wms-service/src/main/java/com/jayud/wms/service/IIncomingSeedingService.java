package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.IncomingSeedingForm;
import com.jayud.wms.model.bo.QueryIncomingSeedingForm;
import com.jayud.wms.model.po.IncomingSeeding;
import com.jayud.wms.model.vo.IncomingSeedingVO;
import com.jayud.wms.model.vo.ReceiptVO;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库播种 服务类
 *
 * @author jyd
 * @since 2021-12-23
 */
public interface IIncomingSeedingService extends IService<IncomingSeeding> {

    /**
    *  分页查询
    * @param incomingSeeding
    * @param req
    * @return
    */
    IPage<IncomingSeeding> selectPage(IncomingSeeding incomingSeeding,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param incomingSeeding
    * @return
    */
    List<IncomingSeeding> selectList(IncomingSeeding incomingSeeding);

    /**
     * 保存(新增+编辑)
     * @param incomingSeeding
     */
    IncomingSeeding saveOrUpdateIncomingSeeding(IncomingSeeding incomingSeeding);

    /**
     * 逻辑删除
     * @param id
     */
    void delIncomingSeeding(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryIncomingSeedingForExcel(Map<String, Object> paramMap);


    void doWarehousingSeeding(ReceiptVO receiptDetails);

    IncomingSeedingVO getDetails(QueryIncomingSeedingForm form);

    void confirmReplacement(IncomingSeedingForm form);

    void confirmShelf();

    /**
     * 容器更换已经存在的必须更换到相应的库位
     * @param form
     */
    void verifyingContainerReplacement(IncomingSeedingForm form);
}
