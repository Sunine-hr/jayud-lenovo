package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.LegalPerson;
import com.jayud.mall.mapper.LegalPersonMapper;
import com.jayud.mall.service.ILegalPersonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 法人表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Service
public class LegalPersonServiceImpl extends ServiceImpl<LegalPersonMapper, LegalPerson> implements ILegalPersonService {

}
