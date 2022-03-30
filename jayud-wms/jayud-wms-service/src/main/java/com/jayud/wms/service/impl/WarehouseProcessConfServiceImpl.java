package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.model.po.SysDictItem;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.WarehouseProcessConfMapper;
import com.jayud.wms.model.po.WarehouseProcessConf;
import com.jayud.wms.service.IWarehouseProcessConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仓库流程配置 服务实现类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Service
public class WarehouseProcessConfServiceImpl extends ServiceImpl<WarehouseProcessConfMapper, WarehouseProcessConf> implements IWarehouseProcessConfService {


    @Autowired
    private WarehouseProcessConfMapper warehouseProcessConfMapper;
    @Autowired
    private AuthClient authClient;

    @Override
    public IPage<WarehouseProcessConf> selectPage(WarehouseProcessConf warehouseProcessConf,
                                                  Integer pageNo,
                                                  Integer pageSize,
                                                  HttpServletRequest req) {

        Page<WarehouseProcessConf> page = new Page<WarehouseProcessConf>(pageNo, pageSize);
        IPage<WarehouseProcessConf> pageList = warehouseProcessConfMapper.pageList(page, warehouseProcessConf);
        return pageList;
    }

    @Override
    public List<WarehouseProcessConf> selectList(WarehouseProcessConf warehouseProcessConf) {
        return warehouseProcessConfMapper.list(warehouseProcessConf);
    }

    @Override
    public List<WarehouseProcessConf> getByCondition(WarehouseProcessConf warehouseProcessConf) {
        QueryWrapper<WarehouseProcessConf> condition = new QueryWrapper<>(warehouseProcessConf);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public void initConfig(Long warehouseId) {
        List<SysDictItem> inProConfs = authClient.selectItemByDictCode("wms_inProConf").getResult();
        List<SysDictItem> outProConfs =  authClient.selectItemByDictCode("wms_inProConf").getResult();
        if (CollectionUtil.isEmpty(inProConfs)) {
            throw new ServiceException("初始化流程失败,请在字典配置流程模板");
        }
        List<WarehouseProcessConf> list = new ArrayList<>();
        for (SysDictItem inProConf : inProConfs) {
            WarehouseProcessConf tmp = new WarehouseProcessConf();
            tmp.setCode(inProConf.getItemValue());
            tmp.setName(inProConf.getItemText());
            tmp.setIsExecute(true);
            tmp.setType(1);
            tmp.setWarehouseId(warehouseId);
            tmp.setCreateBy(CurrentUserUtil.getUsername());
            tmp.setCreateTime(new Date());
            list.add(tmp);
        }
        for (SysDictItem outProConf : outProConfs) {
            WarehouseProcessConf tmp = new WarehouseProcessConf();
            tmp.setCode(outProConf.getItemValue());
            tmp.setName(outProConf.getItemText());
            tmp.setIsExecute(true);
            tmp.setType(2);
            tmp.setWarehouseId(warehouseId);
            tmp.setCreateBy(CurrentUserUtil.getUsername());
            tmp.setCreateTime(new Date());
            list.add(tmp);
        }
        this.saveOrUpdateBatch(list);
    }

}
