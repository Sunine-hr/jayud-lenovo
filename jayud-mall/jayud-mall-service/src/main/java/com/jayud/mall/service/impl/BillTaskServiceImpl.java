package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.BillTaskMapper;
import com.jayud.mall.model.po.BillTask;
import com.jayud.mall.model.vo.BillTaskVO;
import com.jayud.mall.service.IBillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提单任务列表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Service
public class BillTaskServiceImpl extends ServiceImpl<BillTaskMapper, BillTask> implements IBillTaskService {

    @Autowired
    BillTaskMapper billTaskMapper;

    @Override
    public List<BillTaskVO> findbillTaskByObId(Long obId) {
        List<BillTaskVO> list = billTaskMapper.findbillTaskByObId(obId);
        return list;
    }
}
