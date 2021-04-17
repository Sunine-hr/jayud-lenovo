package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 根据应收对账单id，查询应收账单
     * @param accountReceivableId 应收对账单id
     * @return
     */
    List<ReceivableBillMasterVO> findReceivableBillMasterByAccountReceivableId(@Param("accountReceivableId") Long accountReceivableId);

    /**
     * 当前账单日期，验证，应收账单是否可以月结
     * @param firstday  账单日期(开始)
     * @param lastDay   账单日期(结束)
     * @return
     */
    List<ReceivableBillMaster> verifyReceivableBillMasterByCreateTime(@Param("firstday") LocalDateTime firstday, @Param("lastDay") LocalDateTime lastDay);

    /**
     * 根据账单日期，查询可以月结的应收账单，准备生成应收单对账账单
     * @param firstday  账单日期(开始)
     * @param lastDay   账单日期(结束)
     * @return
     */
    List<ReceivableBillMaster> findReceivableBillMasterByCreateTime(@Param("firstday") LocalDateTime firstday, @Param("lastDay") LocalDateTime lastDay);

    /**
     * 验证 应收对账单下的应收账单，是否全部核销
     * 查询应收账单，所有未付款的记录，存在未付款的记录
     * @param accountReceivableId 应收对账单id
     * @return
     */
    List<ReceivableBillMasterVO> verifyReceivableBillMasterByAccountReceivableId(@Param("accountReceivableId") Long accountReceivableId);
}
