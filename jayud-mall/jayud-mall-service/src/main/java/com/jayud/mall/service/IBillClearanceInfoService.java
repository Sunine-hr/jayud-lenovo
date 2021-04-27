package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * (提单)清关信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IBillClearanceInfoService extends IService<BillClearanceInfo> {
    /**
     * 集合数据
     * @param
     * @return
     */
    IPage<BillClearanceInfoVO> findBillClearanceInfoByPage(@Param("form") BillClearanceInfoForm form);


    /**
     * 增加
     * @param billClearanceInfo
     */
    void insertBillClearanceInfo(@Param("billClearanceInfo") BillClearanceInfoForm billClearanceInfo);

    /**
     * 修改用户
     * @param billClearanceInfo
     */
    void updateBillClearanceInfo(@Param("billClearanceInfo") BillClearanceInfoForm billClearanceInfo);

    /**
     * 删除
     * @param id
     */
    void deleteBillClearanceInfo(@Param("id") Long id);
}
