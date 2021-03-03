package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 应收账单主单 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Mapper
@Component
public interface ReceivableBillMasterMapper extends BaseMapper<ReceivableBillMaster> {

    /**
     * 应收账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<ReceivableBillMasterVO> findReceivableBillMasterByPage(Page<ReceivableBillMasterVO> page, @Param("form") QueryReceivableBillMasterForm form);

    /**
     * 根据id，查询应收账单主单
     * @param id
     * @return
     */
    ReceivableBillMasterVO findReceivableBillById(@Param("id") Long id);
}
