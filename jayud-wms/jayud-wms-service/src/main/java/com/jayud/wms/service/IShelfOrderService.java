package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryShelfOrderForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.ShelfOrder;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.ShelfOrderVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 上架单 服务类
 *
 * @author jyd
 * @since 2021-12-23
 */
public interface IShelfOrderService extends IService<ShelfOrder> {

    /**
    *  分页查询
    * @param queryShelfOrderForm
    * @param req
    * @return
    */
    IPage<ShelfOrderVO> selectPage(QueryShelfOrderForm queryShelfOrderForm,
                                   Integer pageNo,
                                   Integer pageSize,
                                   HttpServletRequest req);

    /**
    *  查询列表
    * @param queryShelfOrderForm
    * @return
    */
    List<ShelfOrderVO> selectList(QueryShelfOrderForm queryShelfOrderForm);

    /**
     * 保存(新增+编辑)
     * @param shelfOrder
     */
    ShelfOrder saveOrUpdateShelfOrder(ShelfOrder shelfOrder);

    /**
     * 逻辑删除
     * @param id
     */
    void delShelfOrder(Long id);

    /**
     * 查询导出
     * @param queryShelfOrderForm
     * @return
     */
    List<LinkedHashMap<String, Object>> queryShelfOrderForExcel(QueryShelfOrderForm queryShelfOrderForm,
                                                                HttpServletRequest req);


    void shelfOperation(List<ShelfOrder> shelfOrder);

    /**
     * 生成上架任务
     * @param details
     */
    void generateShelfOrder(ReceiptVO details);

    /**
     * 查询入库报表
     * @param queryShelfOrderTaskForm
     * @return
     */
    BaseResult selectWarehousingReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);

}
