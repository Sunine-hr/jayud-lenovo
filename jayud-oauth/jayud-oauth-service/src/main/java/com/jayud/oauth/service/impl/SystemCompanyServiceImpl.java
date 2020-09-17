package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oauth.model.po.Company;
import com.jayud.oauth.model.vo.CompanyVO;
import com.jayud.oauth.mapper.SystemCompanyMapper;
import com.jayud.oauth.service.ISystemCompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台公司表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
@Service
public class SystemCompanyServiceImpl extends ServiceImpl<SystemCompanyMapper, Company> implements ISystemCompanyService {


    @Override
    public List<CompanyVO> findCompany() {
        return ConvertUtil.convertList(baseMapper.selectList(null),CompanyVO.class);
    }


}
