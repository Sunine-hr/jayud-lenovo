package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.PrintReportManage;
import com.jayud.oms.mapper.PrintReportManageMapper;
import com.jayud.oms.service.IPrintReportManageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 打印报表管理 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-08
 */
@Service
public class PrintReportManageServiceImpl extends ServiceImpl<PrintReportManageMapper, PrintReportManage> implements IPrintReportManageService {

    @Override
    public List<PrintReportManage> getByCondition(PrintReportManage printReportManage) {
        return this.baseMapper.selectList(new QueryWrapper<>(printReportManage));
    }
}
