package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.QuerySeaOrderForm;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.po.SeaReplenishment;
import com.jayud.oceanship.mapper.SeaReplenishmentMapper;
import com.jayud.oceanship.service.ISeaReplenishmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.vo.SeaOrderFormVO;
import com.jayud.oceanship.vo.SeaReplenishmentFormVO;
import com.jayud.oceanship.vo.SeaReplenishmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 海运补料表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-18
 */
@Service
public class SeaReplenishmentServiceImpl extends ServiceImpl<SeaReplenishmentMapper, SeaReplenishment> implements ISeaReplenishmentService {

    @Autowired
    private OauthClient oauthClient;

    /**
     * 分页获取草稿提单数据
     *
     * @return
     */
    @Override
    public IPage<SeaReplenishmentFormVO> findBillByPage(QuerySeaOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<SeaOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findBillByPage(page, form, legalIds);
    }
}
