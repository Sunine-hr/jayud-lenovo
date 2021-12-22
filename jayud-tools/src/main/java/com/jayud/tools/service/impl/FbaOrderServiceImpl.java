package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.tools.model.bo.AddFbaOrderForm;
import com.jayud.tools.model.bo.QueryFbaOrderForm;
import com.jayud.tools.model.po.FbaOrder;
import com.jayud.tools.mapper.FbaOrderMapper;
import com.jayud.tools.model.vo.FbaOrderVO;
import com.jayud.tools.service.IFbaOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * FBA订单 服务实现类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Service
public class FbaOrderServiceImpl extends ServiceImpl<FbaOrderMapper, FbaOrder> implements IFbaOrderService
{

    @Override
    public IPage selectPage(QueryFbaOrderForm queryFbaOrderForm) {
        Page<FbaOrderVO> page = new Page<>(queryFbaOrderForm.getPageNum(), queryFbaOrderForm.getPageSize());
        return this.baseMapper.findByPage(queryFbaOrderForm,page);
    }

    @Override
    public List<FbaOrderVO> selectList(QueryFbaOrderForm queryFbaOrderForm) {
        return this.baseMapper.findList(queryFbaOrderForm);
    }

    @Override
    public void saveOrUpdateFbaOrder(AddFbaOrderForm addFbaOrderForm) {

    }

    @Override
    public void deleteById(List<Long> ids) {

    }
}
