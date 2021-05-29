package com.jayud.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.po.RelocationRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.RelocationGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.RelocationRecordVO;

import java.util.List;

/**
 * <p>
 * 移库信息表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
public interface IRelocationRecordService extends IService<RelocationRecord> {

    List<RelocationGoodsOperationRecordFormVO> getListBySkuAndLocationCode(String sku, String locationCode);

    IPage<RelocationRecordVO> findByPage(QueryRelocationRecordForm form);

    List<RelocationRecordVO> getList(String orderNo, String warehouseName, String areaName, String shelvesName, String sku, String startTime, String endTime);
}
