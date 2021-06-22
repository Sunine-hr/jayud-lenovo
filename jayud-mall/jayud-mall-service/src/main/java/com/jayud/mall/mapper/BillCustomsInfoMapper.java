package com.jayud.mall.mapper;

import com.jayud.mall.model.po.BillCustomsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.model.vo.CustomsInfoCaseExcelVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import com.jayud.mall.model.vo.CustomsListExcelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
