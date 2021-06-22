package com.jayud.mall.mapper;

import com.jayud.mall.model.po.BillCustomsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * (提单)报关信息表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Mapper
public interface BillCustomsInfoMapper extends BaseMapper<BillCustomsInfo> {

    /**
     * 根据报关信息id，查询提单对应报关箱号信息
     * @param b_id
     * @return
     */
    List<CustomsInfoCaseVO> findCustomsInfoCase(@Param("b_id") Long b_id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    BillCustomsInfoVO findBillCustomsInfoById(@Param("id") Long id);

    /**
     * 统计箱号
     * @param b_id
     * @return
     */
    Integer findCustomsInfoCaseTotalBybid(@Param("b_id") Long b_id);

    /**
     * 导出清单-报关箱子
     * @param b_id
     * @return
     */
    List<CustomsInfoCaseExcelVO> findCustomsInfoCaseBybid(@Param("b_id") Long b_id);

    /**
     * 导出报关清单文件
     * @param id
     * @return
     */
    CustomsListExcelVO findCustomsListExcelById(@Param("id") Long id);

    /**
     * 查询导出数据
     * 1.根据报关清单id，查询关联的箱子
     * 2.根据箱子，查询关联的商品
     * 3.根据商品id，查询关联的报关资料（商品的报关资料，要做合并）
     * @param billCustomsInfoId     (提单)报关信息表id(bill_customs_info id)
     * @return
     */
    List<CustomsGoodsExcelVO> findCustomsGoodsExcelByBillCustomsInfoId(@Param("billCustomsInfoId") Long billCustomsInfoId);

    /**
     * 根据参数，查询统计值
     * 箱数、总数量、毛重、立方
     * @param paraMap
     * @return
     */
    Map<String, Object> findCustomsGoodsExcelTotalByParaMap(@Param("paraMap") Map<String, Object> paraMap);

    /**
     * 根据参数，查询统计数据
     * 箱数、毛重、立方
     * @param paraMap
     * @return
     */
    Map<String, Object> findCustomsListExcelTotalByParaMap(@Param("paraMap") Map<String, Object> paraMap);
}
