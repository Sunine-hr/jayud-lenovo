package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.AddMessagePushTemplateForm;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.jayud.oms.mapper.MessagePushTemplateMapper;
import com.jayud.oms.service.IBindingMsgTemplateService;
import com.jayud.oms.service.IMessagePushTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.IMsgPushListService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息推送模板 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Service
public class MessagePushTemplateServiceImpl extends ServiceImpl<MessagePushTemplateMapper, MessagePushTemplate> implements IMessagePushTemplateService {

    @Autowired
    private IMsgPushListService msgPushListService;
    @Autowired
    private IBindingMsgTemplateService bindingMsgTemplateService;
    @Autowired
    private OauthClient oauthClient;


    @Override
    @Transactional
    public void saveOrUpdate(AddMessagePushTemplateForm form) {
        MessagePushTemplate convert = ConvertUtil.convert(form, MessagePushTemplate.class);
        convert.setNum(null);
        if (convert.getId() == null) {
            String prefix = form.getType() == 2 ? "CMT" : "OMT";
            //订单规则
            Integer count = this.baseMapper.selectCount(new QueryWrapper(new MessagePushTemplate().setType(form.getType())));
            convert.setNum(prefix + StringUtils.zeroComplement(4, count + 1));
            convert.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            convert.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(convert);

        //操作人类型,根据岗位获取所有人,并且创建消息
        if (form.getType() == 1 && form.getId() == null && !StringUtils.isEmpty(form.getPost())) {
            //根据岗位查询消息列表,根据岗位追加模板

            //相同状态不进行操作
        }
    }

    @Override
    public Map<String, Object> executeTemplateSQL(String sqlSelect) {
        try {
            return this.baseMapper.executeTemplateSQL(sqlSelect);
        } catch (Exception e) {
            throw new JayudBizException("sql语法错误 错误信息:" + e.getMessage());
        }
    }

    @Override
    public List<MessagePushTemplate> getByCondition(MessagePushTemplate messagePushTemplate) {
        return this.baseMapper.selectList(new QueryWrapper<>(messagePushTemplate));
    }

    @Override
    public boolean checkUnique(AddMessagePushTemplateForm form) {
        List<MessagePushTemplate> list = this.getByCondition(new MessagePushTemplate()
                .setMsgName(form.getMsgName()).setType(form.getType()));
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        MessagePushTemplate messagePushTemplate = list.get(0);
        return messagePushTemplate.getId().equals(form.getId());
    }
}
