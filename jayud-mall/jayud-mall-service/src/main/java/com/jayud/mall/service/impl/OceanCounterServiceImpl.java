package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 提单对应货柜信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanCounterServiceImpl extends ServiceImpl<OceanCounterMapper, OceanCounter> implements IOceanCounterService {

}
