package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryReceiptForm;
import com.jayud.wms.model.bo.ReceiptForm;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.common.dto.QueryClientQualityMaterialForm;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 收货单 服务类
 *
 * @author jyd
 * @since 2021-12-20
 */
public interface IReceiptService extends IService<Receipt> {

    /**
     * 分页查询
     *
     * @param queryReceiptForm
     * @param req
     * @return
     */
    IPage<ReceiptVO> selectPage(QueryReceiptForm queryReceiptForm,
                                Integer pageNo,
                                Integer pageSize,
                                HttpServletRequest req);


    /**
     * 分页查询
     *
     * @param queryReceiptForm
     * @param req
     * @return
     */
    IPage<ReceiptVO> selectPageFeign(QueryReceiptForm queryReceiptForm,
                                     Integer pageNo,
                                     Integer pageSize,
                                     HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param receipt
     * @return
     */
    List<Receipt> selectList(Receipt receipt);

    /**
     * 保存(新增+编辑)
     *
     * @param receipt
     */
    Receipt saveOrUpdateReceipt(Receipt receipt);

    /**
     * 逻辑删除
     *
     * @param id
     */
    boolean delReceipt(Long id);

    /**
     * 查询导出
     *
     * @param queryReceiptForm
     * @return
     */
    List<LinkedHashMap<String, Object>> queryReceiptForExcel(QueryReceiptForm queryReceiptForm,
                                                             HttpServletRequest req);


    void cancel(Long id);

    /**
     * 获取收货单详情信息
     * @param id
     * @return
     */
    ReceiptVO getDetails(Long id);

    /**
     * 编辑 是否直接上架 -> 填写容器号 -> 保存
     * @param receiptForm
     */
    void createOrder(ReceiptForm receiptForm);

    /**
     * 确认收货
     * @param id
     */
    void receiptConfirmation(Long id);

    List<Receipt> getByReceiptNums(List<String> orderNums);

    //APP物料超收比例计算
    void appMaterialFigureUp(QueryClientQualityMaterialForm form);


    //根据物料类型入库
    BaseResult createRecordCopyMaterial(QueryClientQualityMaterialForm form);

    //根据SN序列号入库
    BaseResult createRecordCopyMaterialSN(QueryClientQualityMaterialForm form);

    //test
     void updateMaterial(QueryClientQualityMaterialForm form);

}
