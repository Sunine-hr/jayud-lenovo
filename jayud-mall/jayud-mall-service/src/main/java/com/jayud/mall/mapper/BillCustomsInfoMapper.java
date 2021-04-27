package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.bo.BillCustomsInfoForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 提单对应报关
 * </p>
 *
 * @author Larry
 * @since 2020-12-30
 */
@Mapper
@Component
public interface BillCustomsInfoMapper extends BaseMapper<BillClearanceInfo> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<BillCustomsInfoVO> findBillCustomsInfoByPage(@Param("form") BillCustomsInfoForm form);


    /**
     * 增加
     * @param billCustomsInfo
     */
    void insertBillCustomsInfo(@Param("billCustomsInfo") BillCustomsInfoForm billCustomsInfo);

    /**
     * 修改
     * @param billCustomsInfo
     */
    void updateBillCustomsInfo(@Param("billCustomsInfo") BillCustomsInfoForm billCustomsInfo);

    /**
     * 删除
     * @param id
     */
    void deleteBillCustomsInfo(@Param("id") Long id);


}
