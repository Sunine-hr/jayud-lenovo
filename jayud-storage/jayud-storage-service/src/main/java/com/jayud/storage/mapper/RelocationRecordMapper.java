package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.storage.model.bo.QueryRelocationRecordForm;
import com.jayud.storage.model.po.RelocationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.RelocationGoodsOperationRecordFormVO;
import com.jayud.storage.model.vo.RelocationRecordVO;
import com.jayud.storage.model.vo.StockVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 移库信息表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
public interface RelocationRecordMapper extends BaseMapper<RelocationRecord> {

    List<RelocationGoodsOperationRecordFormVO> getListBySkuAndLocationCode(@Param("sku") String sku, @Param("locationCode")String locationCode);

    IPage<RelocationRecordVO> findByPage(@Param("page")Page<StockVO> page,@Param("form")QueryRelocationRecordForm form);
}
