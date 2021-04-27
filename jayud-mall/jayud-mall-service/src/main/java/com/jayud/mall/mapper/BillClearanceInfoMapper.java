package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.bo.SaveSystemUserForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.SystemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * <p>
 * 提单对应清关
 * </p>
 *
 * @author Larry
 * @since 2020-12-30
 */
@Mapper
@Component
public interface BillClearanceInfoMapper extends BaseMapper<BillClearanceInfo> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<BillClearanceInfoVO> findBillClearanceInfoByPage(Page<BillClearanceInfo> page, @Param("form") BillClearanceInfoForm form);


    /**
     * 增加
     * @param billClearanceInfo
     */
    void insertBillClearanceInfo(@Param("billClearanceInfo") BillClearanceInfoForm billClearanceInfo);

    /**
     * 修改
     * @param billClearanceInfo
     */
    void updateBillClearanceInfo(@Param("billClearanceInfo") BillClearanceInfoForm billClearanceInfo);

    /**
     * 删除
     * @param id
     */
    void deleteBillClearanceInfo(@Param("id") Long id);


}
