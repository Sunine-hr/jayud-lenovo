package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.HeXiaoConfirmForm;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.mapper.CancelAfterVerificationMapper;
import com.jayud.finance.service.ICancelAfterVerificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.finance.vo.HeXiaoListVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class CancelAfterVerificationServiceImpl extends ServiceImpl<CancelAfterVerificationMapper, CancelAfterVerification> implements ICancelAfterVerificationService {

    @Override
    public List<HeXiaoListVO> heXiaoList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<CancelAfterVerification> heXiaoList = list(queryWrapper);
        return ConvertUtil.convertList(heXiaoList,HeXiaoListVO.class);
    }

    @Override
    public Boolean heXiaoConfirm(List<HeXiaoConfirmForm> forms) {
        List<HeXiaoConfirmForm> addList = new ArrayList<>();
        for (HeXiaoConfirmForm form : forms) {
            if(form.getId() == null){
                addList.add(form);
            }
        }
        //计算本币金额,提供给前台累加
        List<CancelAfterVerification> list = ConvertUtil.convertList(addList,CancelAfterVerification.class);
        return saveBatch(list);
    }
}
