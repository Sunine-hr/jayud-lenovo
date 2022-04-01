package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.auth.mapper.HsCodeMapper;
import com.jayud.auth.model.bo.AddHsCodeElementsForm;
import com.jayud.auth.model.bo.AddHsCodeForm;
import com.jayud.auth.model.bo.DeleteForm;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.HsCode;
import com.jayud.auth.model.po.HsCodeElements;
import com.jayud.auth.model.vo.HsCodeElementsVO;
import com.jayud.auth.model.vo.HsCodeFormVO;
import com.jayud.auth.model.vo.HsCodeVO;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.service.IHsCodeElementsService;
import com.jayud.auth.service.IHsCodeService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海关编码表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class HsCodeServiceImpl extends ServiceImpl<HsCodeMapper, HsCode> implements IHsCodeService {

    @Autowired
    private IHsCodeElementsService hsCodeElementsService;

    @Autowired
    private ISysUserService systemUserService;

    @Override
    public IPage<HsCodeFormVO> findByPage(QueryForm form) {
        Page<HsCodeFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        SysUserVO systemUserByName = systemUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        List<HsCode> hsCodes = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
            HsCode hsCode = new HsCode();
            hsCode.setId(id.intValue());
            hsCode.setVoided(1);
            hsCode.setVoidedBy(systemUserByName.getId().intValue());
            hsCode.setVoidedByDtm(LocalDateTime.now());
            hsCode.setVoidedByName(systemUserByName.getName());
            hsCodes.add(hsCode);
        }
        boolean b = this.updateBatchById(hsCodes);
        if(b){
            log.warn("海关编码删除成功："+hsCodes);
        }
        return b;
    }

    /**
     * 添加或修改海关编码
     * @param form
     * @return
     */
    @Override
    public boolean saveOrUpdateHsCode(AddHsCodeForm form) {
        HsCode hsCode = ConvertUtil.convert(form, HsCode.class);
        SysUserVO systemUserByName = systemUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        StringBuffer stringBuffer = new StringBuffer();

        if(CollectionUtils.isNotEmpty(form.getHsCodeElementsVOS())){
            for (AddHsCodeElementsForm hsCodeElementsForm : form.getHsCodeElementsVOS()) {
                if(hsCodeElementsForm.getElementsName().equals("型号")){
                    stringBuffer.append(hsCodeElementsForm.getDefaultValue() == null?" ":(hsCodeElementsForm.getDefaultValue()+"型")).append("|");
                }
                stringBuffer.append(hsCodeElementsForm.getDefaultValue() == null?" ":hsCodeElementsForm.getDefaultValue()).append("|");
            }
        }

        if(form.getId() != null){ //修改
            HsCode byId = this.getById(form.getId());

            List<HsCodeElementsVO> codeElementsByCodeNo = hsCodeElementsService.getCodeElementsByCodeNo(byId.getCodeNo());
            if(CollectionUtils.isNotEmpty(codeElementsByCodeNo)){
                boolean result = hsCodeElementsService.deleteCodeElementsByCodeNo(byId.getCodeNo());
                if(!result){
                    log.warn("要素信息删除失败"+byId.getCodeNo());
                    return false;
                }
            }
            //删除修改前的要素

            hsCode.setMdyBy(systemUserByName.getId().intValue());
            hsCode.setMdyByDtm(LocalDateTime.now());
            hsCode.setMdyByName(systemUserByName.getUserName());
        }else{
            hsCode.setCrtBy(systemUserByName.getId().intValue());
            hsCode.setCrtByDtm(LocalDateTime.now());
            hsCode.setCrtByName(systemUserByName.getUserName());
        }
        hsCode.setElements(stringBuffer.length()>0?stringBuffer.toString().substring(0,stringBuffer.length()-1):null);
        boolean update = this.saveOrUpdate(hsCode);
        if(!update){
            log.warn("海关编码添加或修改失败"+hsCode.getCodeNo());
            return false;
        }
        log.warn("海关编码添加或修改成功"+hsCode.getCodeNo());

        //添加要素
        if(CollectionUtils.isNotEmpty(form.getHsCodeElementsVOS())){
            List<HsCodeElements> hsCodeElements = ConvertUtil.convertList(form.getHsCodeElementsVOS(), HsCodeElements.class);
            for (HsCodeElements hsCodeElement : hsCodeElements) {
                hsCodeElement.setId(null);
                hsCodeElement.setCrtBy(systemUserByName.getId().intValue());
                hsCodeElement.setCrtByDtm(LocalDateTime.now());
                hsCodeElement.setCrtByName(systemUserByName.getUserName());
                hsCodeElement.setHsCodeNo(hsCode.getCodeNo());
            }
            boolean b = hsCodeElementsService.saveBatch(hsCodeElements);
            if(!b){
                log.warn("海关编码要素添加或修改失败"+hsCode.getCodeNo());
                return false;
            }
            log.warn("海关编码要素添加或修改成功"+hsCode.getCodeNo());

        }

        return true;

    }

    /**
     * 获取海关编码详情
     * @param id
     * @return
     */
    @Override
    public HsCodeVO getHsCodeDetail(Integer id) {
        HsCode hsCode = this.getById(id);
        HsCodeVO hsCodeVO = ConvertUtil.convert(hsCode, HsCodeVO.class);

        //获取要素
        List<HsCodeElementsVO> hsCodeElementsVO = hsCodeElementsService.getCodeElementsByCodeNo(hsCodeVO.getCodeNo());
        hsCodeVO.setHsCodeElementsVOS(hsCodeElementsVO);

        return hsCodeVO;
    }

    @Override
    public List<String> findHsCode(String codeNo) {
        return this.baseMapper.findHsCode( codeNo);
    }

    @Override
    public HsCodeVO getHsCodeByCodeNo(String hsCodeNo) {
        QueryWrapper<HsCode> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HsCode::getCodeNo,hsCodeNo);
        queryWrapper.lambda().eq(HsCode::getVoided,0);
        HsCode hsCode = this.getOne(queryWrapper);
        return ConvertUtil.convert(hsCode,HsCodeVO.class);
    }

    @Override
    public HsCodeVO getHsCodeDetailByCodeNo(String codeNo) {
        QueryWrapper<HsCode> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HsCode::getCodeNo,codeNo);
        HsCode hsCode = this.getOne(queryWrapper);
        HsCodeVO hsCodeVO = ConvertUtil.convert(hsCode, HsCodeVO.class);

        //获取要素
        List<HsCodeElementsVO> hsCodeElementsVO = hsCodeElementsService.getCodeElementsByCodeNo(hsCodeVO.getCodeNo());
        hsCodeVO.setHsCodeElementsVOS(hsCodeElementsVO);

        return hsCodeVO;
    }

    @Override
    public CommonResult deleteByIds(DeleteForm form) {
        SysUserVO systemUserByName = systemUserService.getSystemUserByName(CurrentUserUtil.getUsername());

        List<HsCode> hsCodes = new ArrayList<>();
        for (Long id : form.getIds()) {
            HsCode hsCode = new HsCode();
            hsCode.setId(id.intValue());
            hsCode.setVoided(1);
            hsCode.setVoidedBy(systemUserByName.getId().intValue());
            hsCode.setVoidedByDtm(LocalDateTime.now());
            hsCode.setVoidedByName(systemUserByName.getName());
            hsCodes.add(hsCode);
        }
        boolean b = this.updateBatchById(hsCodes);
        if(b){
            log.warn("海关编码删除成功："+hsCodes);
            return CommonResult.success();
        }
        return CommonResult.error(444,"海关编码删除失败");
    }

    @Override
    public HsCode getByCodeNo(String codeNo) {
        QueryWrapper<HsCode> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HsCode::getCodeNo,codeNo);
        queryWrapper.lambda().eq(HsCode::getVoided,0);
        return this.getOne(queryWrapper);
    }
}
