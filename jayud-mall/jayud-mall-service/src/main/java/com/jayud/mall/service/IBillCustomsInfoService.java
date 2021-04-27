package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BillCustomsInfoForm;
import com.jayud.mall.model.po.BillCustomsInfo;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * (提单)清关信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IBillCustomsInfoService extends IService<BillCustomsInfo> {
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
