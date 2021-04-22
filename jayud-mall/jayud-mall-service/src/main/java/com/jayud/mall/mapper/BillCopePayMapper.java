package com.jayud.mall.mapper;

import com.jayud.mall.model.po.BillCopePay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.BillCopePayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 提单对应应付费用信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-27
 */
@Mapper
@Component
public interface BillCopePayMapper extends BaseMapper<BillCopePay> {

    /**
     * 根据提单id，查询提单费用（提单应收费用）
     * @param billId 提单id
     * @return
     */
    List<BillCopePayVO> findBillCopePayByBillId(@Param("billId") Long billId);
}
