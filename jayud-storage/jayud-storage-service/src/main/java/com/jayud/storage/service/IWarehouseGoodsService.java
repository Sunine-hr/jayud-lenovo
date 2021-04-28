package com.jayud.storage.service;

import com.jayud.storage.model.po.WarehouseGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.storage.model.vo.WarehouseGoodsVO;

import java.util.List;

/**
 * <p>
 * 仓储商品信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-19
 */
public interface IWarehouseGoodsService extends IService<WarehouseGoods> {

    List<WarehouseGoodsVO> getList(Long id, String orderNo);

    /**
     * 删除商品信息
     * @param orderId
     * @param orderNo
     * @return
     */
    void deleteWarehouseGoodsFormsByOrder(Long orderId, String orderNo);
}
