package com.jayud.customs.service.impl;

import com.jayud.customs.model.po.CustomsHandbookRecognizeLog;
import com.jayud.customs.mapper.CustomsHandbookRecognizeLogMapper;
import com.jayud.customs.service.ICustomsHandbookRecognizeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 手册黑名单识别请求记录表，用于记录上传的识别请求 服务实现类
 * </p>
 *
 * @author william.chen
 * @since 2020-10-28
 */
@Service
public class CustomsHandbookRecognizeLogServiceImpl extends ServiceImpl<CustomsHandbookRecognizeLogMapper, CustomsHandbookRecognizeLog> implements ICustomsHandbookRecognizeLogService {

}
