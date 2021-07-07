package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.BusinessLogMapper;
import com.jayud.mall.model.bo.QueryBusinessLogForm;
import com.jayud.mall.model.po.BusinessLog;
import com.jayud.mall.model.vo.BusinessLogVO;
import com.jayud.mall.service.IBusinessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务日志表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
@Service
public class BusinessLogServiceImpl extends ServiceImpl<BusinessLogMapper, BusinessLog> implements IBusinessLogService {

    @Autowired
    BusinessLogMapper businessLogMapper;

    @Override
    public IPage<BusinessLogVO> findBusinessLogByPage(QueryBusinessLogForm form) {
        //定义分页参数
        Page<BusinessLogVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.operation_time"));
        IPage<BusinessLogVO> pageInfo = businessLogMapper.findBusinessLogByPage(page, form);
        return pageInfo;
    }
}
