package com.jayud.oceanship.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.po.SeaReplenishment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oceanship.vo.SeaReplenishmentFormVO;
import com.jayud.oceanship.vo.SeaReplenishmentVO;

import java.util.List;

/**
 * <p>
 * 海运补料表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-18
 */
public interface ISeaReplenishmentService extends IService<SeaReplenishment> {

    /**
     * 分页获取草稿提单数据
     * @return
     */
    IPage<SeaReplenishmentFormVO> findBillByPage(QuerySeaOrderForm form);


    /**
     * 获取补料单详情
     * @return
     */
    SeaReplenishmentVO getSeaRepOrderDetails(Long orderId);


    void deleteSeaReplenishment(String orderNo);

    List<SeaReplenishment> getList(String orderNo);
}
