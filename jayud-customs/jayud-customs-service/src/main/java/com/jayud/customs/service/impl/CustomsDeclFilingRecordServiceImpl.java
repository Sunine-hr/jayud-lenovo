package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import com.jayud.customs.mapper.CustomsDeclFilingRecordMapper;
import com.jayud.customs.service.ICustomsDeclFilingRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报关归档记录 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
@Service
public class CustomsDeclFilingRecordServiceImpl extends ServiceImpl<CustomsDeclFilingRecordMapper, CustomsDeclFilingRecord> implements ICustomsDeclFilingRecordService {

    @Override
    public List<CustomsDeclFilingRecord> getByNums(Long customsDeclFilingId, List<String> nums) {
        QueryWrapper<CustomsDeclFilingRecord> condition = new QueryWrapper<>();
        condition.lambda().in(CustomsDeclFilingRecord::getNum, nums);
        if (customsDeclFilingId != null) {
            condition.lambda().ne(CustomsDeclFilingRecord::getCustomsDeclFilingId, customsDeclFilingId);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<CustomsDeclFilingRecord> getByDeclFilingId(Long declFilingId) {
        QueryWrapper<CustomsDeclFilingRecord> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomsDeclFilingRecord::getCustomsDeclFilingId, declFilingId);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public int removeByCondition(CustomsDeclFilingRecord customsDeclFilingRecord) {
        return this.baseMapper.delete(new QueryWrapper<>(customsDeclFilingRecord));
    }
}
