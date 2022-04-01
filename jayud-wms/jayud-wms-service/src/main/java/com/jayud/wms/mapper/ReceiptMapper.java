package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.QueryReceiptForm;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.vo.ReceiptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 收货单 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-20
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {
    /**
    *   分页查询
    */
    IPage<ReceiptVO> pageList(@Param("page") Page<QueryReceiptForm> page, @Param("receipt") QueryReceiptForm receipt);

    /**
     *   外部app分页查询
     */
    IPage<ReceiptVO> pageListFeign(@Param("page") Page<QueryReceiptForm> page, @Param("receipt") QueryReceiptForm receipt);
    /**
    *   列表查询
    */
    List<Receipt> list(@Param("receipt") Receipt receipt);

    /**
     * 查询导出
     * @param receipt
     * @return
     */
    List<LinkedHashMap<String, Object>> queryReceiptForExcel(@Param("receipt") QueryReceiptForm receipt);

    /**
     * @description 删除质检单编码
     * @author  ciro
     * @date   2022/4/1 13:43
     * @param: orderNumberList
     * @param: username
     * @return: int
     **/
    int delQcno(@Param("orderNumberList") List<String> orderNumberList,@Param("username") String username);
}
