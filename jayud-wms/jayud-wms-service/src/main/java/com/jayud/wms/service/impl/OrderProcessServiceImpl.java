package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.wms.mapper.OrderProcessMapper;
import com.jayud.wms.model.po.OrderProcess;
import com.jayud.wms.model.po.WarehouseProcessConf;
import com.jayud.wms.service.IOrderProcessService;
import com.jayud.wms.service.IWarehouseProcessConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单流程 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class OrderProcessServiceImpl extends ServiceImpl<OrderProcessMapper, OrderProcess> implements IOrderProcessService {


    @Autowired
    private OrderProcessMapper orderProcessMapper;
    @Autowired
    private IWarehouseProcessConfService warehouseProcessConfService;

    @Override
    public IPage<OrderProcess> selectPage(OrderProcess orderProcess,
                                          Integer pageNo,
                                          Integer pageSize,
                                          HttpServletRequest req) {

        Page<OrderProcess> page = new Page<OrderProcess>(pageNo, pageSize);
        IPage<OrderProcess> pageList = orderProcessMapper.pageList(page, orderProcess);
        return pageList;
    }

    @Override
    public List<OrderProcess> selectList(OrderProcess orderProcess) {
        return orderProcessMapper.list(orderProcess);
    }

    @Override
    public void generationProcess(String orderNo, Long warehouseId, Integer type) {
        List<WarehouseProcessConf> warehouseProcessConfs = warehouseProcessConfService.getByCondition(new WarehouseProcessConf().setWarehouseId(warehouseId).setType(type));
        if (CollectionUtil.isEmpty(warehouseProcessConfs)) {throw new ServiceException("请在系统管理配置流程");}
        warehouseProcessConfs = warehouseProcessConfs.stream().filter(e -> e.getIsExecute() != null && e.getIsExecute()).collect(Collectors.toList());
        String parenId = "0";
        List<OrderProcess> list = new ArrayList<>();
        for (int i = 0; i < warehouseProcessConfs.size(); i++) {
            WarehouseProcessConf warehouseProcessConf = warehouseProcessConfs.get(i);
            OrderProcess orderProcess = new OrderProcess().setOrderNo(orderNo).setStatus(warehouseProcessConf.getCode())
                    .setStatusName(warehouseProcessConf.getName()).setFStatus(parenId).setType(type);
            parenId = warehouseProcessConf.getCode();
            list.add(orderProcess);
        }
        this.saveBatch(list);
    }

    @Override
    public String getNextNode(String orderNo, String fStatus) {
        List<OrderProcess> orderProcesses = this.baseMapper.selectList(new QueryWrapper<>(new OrderProcess().setOrderNo(orderNo).setFStatus(fStatus)));
        return CollectionUtil.isEmpty(orderProcesses) ? "" : orderProcesses.get(0).getStatus();
    }

}
