package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryReceiptNoticeForm;
import com.jayud.wms.model.po.ReceiptNotice;
import com.jayud.wms.model.vo.ReceiptNoticeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 收货通知单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface ReceiptNoticeMapper extends BaseMapper<ReceiptNotice> {
    /**
    *   分页查询
    */
    IPage<ReceiptNoticeVO> pageList(@Param("page") Page<QueryReceiptNoticeForm> page, @Param("receiptNotice") QueryReceiptNoticeForm receiptNotice);

    /**
    *   列表查询
    */
    List<ReceiptNotice> list(@Param("receiptNotice") ReceiptNotice receiptNotice);


    //导出
    List<LinkedHashMap<String, Object>> queryReceiptNoticeForExcel(@Param("receiptNotice") QueryReceiptNoticeForm receiptNotice);


}
