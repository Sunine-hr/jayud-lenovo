package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsPackingOffshelfTaskVO;
import com.jayud.wms.model.vo.WmsPackingOffshelfVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拣货下架任务 服务类
 *
 * @author jyd
 * @since 2021-12-24
 */
public interface IWmsPackingOffshelfTaskService extends IService<WmsPackingOffshelfTask> {

    /**
    *  分页查询
    * @param wmsPackingOffshelfTask
    * @param req
    * @return
    */
    IPage<WmsPackingOffshelfTask> selectPage(WmsPackingOffshelfTask wmsPackingOffshelfTask,
                                Integer currentPage,
                                Integer pageSize,
                                HttpServletRequest req);

    /**
    *  查询列表
    * @param wmsPackingOffshelfTask
    * @return
    */
    List<WmsPackingOffshelfTask> selectList(WmsPackingOffshelfTask wmsPackingOffshelfTask);



    /**
     * 物理删除
     * @param id
     */
    void phyDelById(int id);


    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfTaskForExcel(Map<String, Object> paramMap);


    /**
     * @description 生成拣货下架单
     * @author  ciro
     * @date   2021/12/29 9:22
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult createPackingOffShelf(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 撤销拣货下架
     * @author  ciro
     * @date   2021/12/27 15:14
     * @param: outboundOrderNumberVO
     * @return: void
     **/
    BaseResult cancelPackingOffShelf(OutboundOrderNumberVO outboundOrderNumberVO);

    /**
     * @description 接收拣货下架单
     * @author  ciro
     * @date   2021/12/29 10:26
     * @param: task
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult receivePackingOffShelf(WmsPackingOffshelfTask task);

    /**
     * @description 完成拣货下架
     * @author  ciro
     * @date   2021/12/29 10:00
     * @param: outboundOrderNumberVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult finishPackingOffShelf(WmsPackingOffshelfTask wmsPackingOffshelfTask);

    /**
     * @description 获取下架任务
     * @author  ciro
     * @date   2022/1/6 10:14
     * @param:  wmsPackingOffshelfVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult  getPackingTask(WmsPackingOffshelfVO wmsPackingOffshelfVO);

    /**
     * @description 查询下架任务详情详情
     * @author  ciro
     * @date   2022/1/6 10:49
     * @param: wmsPackingOffshelfVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult getPackingTaskDetail(WmsPackingOffshelfVO wmsPackingOffshelfVO);

    /**
     * @description
     * @author  ciro
     * @date   2022/1/13 11:05
     * @param: packingOffshelfNumber
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult skipTaskDetail(String packingOffshelfNumber);

    /**
     * 出库报表
     * @param queryShelfOrderTaskForm
     * @return
     */
    BaseResult selectDeliveryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm);


    /**
     * @description 查询库位信息、拣货下架数据
     * @author  ciro
     * @date   2022/3/10 14:06
     * @param: wmsPackingOffshelfTaskVO
     * @return: com.jyd.component.commons.result.Result<com.jayud.model.vo.WmsPackingOffshelfTaskVO>
     **/
    BaseResult<WmsPackingOffshelfTaskVO> selectTaskAndLocationByMsg(WmsPackingOffshelfTaskVO wmsPackingOffshelfTaskVO);


    /**
     * @description 完成并结束拣货下架
     * @author  ciro
     * @date   2022/3/10 15:25
     * @param: wmsPackingOffshelfTaskVO
     * @return: com.jyd.component.commons.result.Result
     **/
    BaseResult finishTaskAndEndOrder(WmsPackingOffshelfTaskVO wmsPackingOffshelfTaskVO);

}
