package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryReceiptNoticeForm;
import com.jayud.wms.model.bo.ReceiptNoticeForm;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.po.ReceiptNotice;
import com.jayud.wms.model.vo.ReceiptNoticeVO;
import com.jayud.common.dto.QueryClientReceiptNoticeForm;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货通知单 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface IReceiptNoticeService extends IService<ReceiptNotice> {

    /**
     * 分页查询
     *
     * @param queryReceiptNoticeForm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<ReceiptNoticeVO> selectPage(QueryReceiptNoticeForm queryReceiptNoticeForm,
                                      Integer pageNo,
                                      Integer pageSize,
                                      HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param receiptNotice
     * @return
     */
    List<ReceiptNotice> selectList(ReceiptNotice receiptNotice);

    /**
     * 创建收货通知单
     * @param form
     */
    ReceiptNotice createOrder(ReceiptNoticeForm form);

    /**
     * 获取收货通知单
     * @param id
     * @return
     */
    ReceiptNoticeVO getDetails(Long id);

    /**
     * 终端增加物料操作
     * @param map
     */
    void addMaterialToTerminal(Map<String, Object> map);

    /**
     * 收货通知单  转收货   ->  收货单
     * @param id
     */
    Receipt transferReceipt(Long id);



    //导出
    List<LinkedHashMap<String, Object>> queryReceiptNoticeForExcel(QueryReceiptNoticeForm queryReceiptNoticeForm,
                                                                   HttpServletRequest req);

    void updateByCondition(ReceiptNotice receiptNotice, ReceiptNotice condition);


    //关联删除信息物料表和物料sn表
    boolean deletedReceiptNotice(Long id);

    /**
     * 创建收货通知单
     * @param queryClientReceiptNoticeForm
     */
    BaseResult addOrUpdateFeign(QueryClientReceiptNoticeForm queryClientReceiptNoticeForm);

}
