package com.jayud.customs.service;

import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关归档记录 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
public interface ICustomsDeclFilingRecordService extends IService<CustomsDeclFilingRecord> {

    List<CustomsDeclFilingRecord> getByNums(List<String> nums);
}
