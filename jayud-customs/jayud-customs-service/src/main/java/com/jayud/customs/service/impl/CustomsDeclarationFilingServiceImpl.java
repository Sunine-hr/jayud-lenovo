package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.customs.model.bo.AddCustomsDeclarationFilingForm;
import com.jayud.customs.model.bo.QueryCustomsDeclarationFiling;
import com.jayud.customs.model.po.CustomsDeclFilingRecord;
import com.jayud.customs.model.po.CustomsDeclarationFiling;
import com.jayud.customs.mapper.CustomsDeclarationFilingMapper;
import com.jayud.customs.model.vo.CustomsDeclarationFilingVO;
import com.jayud.customs.service.ICustomsDeclFilingRecordService;
import com.jayud.customs.service.ICustomsDeclarationFilingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 报关归档 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
@Service
public class CustomsDeclarationFilingServiceImpl extends ServiceImpl<CustomsDeclarationFilingMapper, CustomsDeclarationFiling> implements ICustomsDeclarationFilingService {

    //业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆)
    final List<String> prefix = Arrays.asList("-", "ZG", "KY", "HY", "KD", "NL");
    @Autowired
    private ICustomsDeclFilingRecordService customsDeclFilingRecordService;

    @Override
    public boolean exitBoxNum(CustomsDeclarationFiling customsDeclarationFiling) {
        return this.count(new QueryWrapper<>(customsDeclarationFiling)) > 0;
    }

    @Override
    @Transactional
    public void saveOrUpdate(AddCustomsDeclarationFilingForm form) {
        CustomsDeclarationFiling convert = ConvertUtil.convert(form, CustomsDeclarationFiling.class);
        List<CustomsDeclFilingRecord> nums = form.getNums();
        if (convert.getId() == null) {
            convert.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
            this.saveOrUpdate(convert);
            form.getNums().forEach(e -> e.setCustomsDeclFilingId(convert.getId()));
            customsDeclFilingRecordService.saveBatch(nums);
        } else {
            convert.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
            this.updateById(convert);
            this.customsDeclFilingRecordService.updateBatchById(nums);
        }

    }

    @Override
    public Integer getSerialNumber(String filingDate, Integer bizModel, Integer imAndExType) {
        return this.baseMapper.getSerialNumber(filingDate, bizModel, imAndExType);
    }

    @Override
    public String generateBoxNum(String filingDate, Integer bizModel, Integer imAndExType) {
        //获取流水号
        Integer num = this.getSerialNumber(filingDate, bizModel, imAndExType);
        String prefix = this.prefix.get(bizModel);
        String type = imAndExType == 1 ? "JK" : "CK";
        String[] split = filingDate.split("-");
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(type).append(split[0]).append(split[1]).append(StringUtils.zeroComplement(3, num + 1));
        return sb.toString();
    }

    @Override
    public IPage<CustomsDeclarationFilingVO> findByPage(QueryCustomsDeclarationFiling form) {
        Page<CustomsDeclarationFilingVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        IPage<CustomsDeclarationFilingVO> iPage = this.baseMapper.findByPage(page, form);
        iPage.getRecords().forEach(CustomsDeclarationFilingVO::handleNums);
        return iPage;
    }

    @Override
    public CustomsDeclarationFilingVO getDetails(Long id) {
        CustomsDeclarationFiling declarationFiling = this.getById(id);
        List<CustomsDeclFilingRecord> list = this.customsDeclFilingRecordService.getByDeclFilingId(declarationFiling.getId());
        CustomsDeclarationFilingVO convert = ConvertUtil.convert(declarationFiling, CustomsDeclarationFilingVO.class);
        convert.setNums(list);
        convert.handleNums();
        return convert;
    }
}
