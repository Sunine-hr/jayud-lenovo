package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomsClearanceMapper;
import com.jayud.mall.model.bo.AuditCustomsClearanceForm;
import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.po.CustomsClearance;
import com.jayud.mall.model.vo.CustomsClearanceVO;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICustomsClearanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 清关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsClearanceServiceImpl extends ServiceImpl<CustomsClearanceMapper, CustomsClearance> implements ICustomsClearanceService {

    //文件服务访问前缀
    @Value("${file.absolute.path:}")
    private String fileAbsolutePath;

    @Autowired
    CustomsClearanceMapper customsClearanceMapper;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<CustomsClearanceVO> findCustomsClearanceByPage(QueryCustomsClearanceForm form) {
        //定义分页参数
        Page<CustomsClearanceVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<CustomsClearanceVO> pageInfo = customsClearanceMapper.findCustomsClearanceByPage(page, form);
        List<CustomsClearanceVO> records = pageInfo.getRecords();
        records.forEach(customsClearanceVO -> {
            String picUrl = customsClearanceVO.getPicUrl();
            if(StrUtil.isNotEmpty(picUrl)){
                customsClearanceVO.setPicUrl(fileAbsolutePath+picUrl);
            }
        });
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveCustomsData(CustomsClearanceForm form) {
        CustomsClearance customsClearance = ConvertUtil.convert(form, CustomsClearance.class);
        Long id = form.getId();
        String idCode = form.getIdCode();
        if(id == null){
            //新增
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
            AuthUser user = baseService.getUser();
            customsClearance.setSuttleUnit("KG");
            customsClearance.setGrossUnit("KG");
            customsClearance.setStatus("1");
            customsClearance.setUserId(user.getId().intValue());
            customsClearance.setUserName(user.getName());
            customsClearance.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_code", idCode);
            queryWrapper.ne("id", id);
            int count = this.count(queryWrapper);
            if(count > 0){
                return CommonResult.error(-1, "idCode,已存在");
            }
        }

        TemplateUrlVO picUrls = form.getPicUrls();
        if(ObjectUtil.isNotEmpty(picUrls)){
            String relativePath = picUrls.getRelativePath();
            customsClearance.setPicUrl(relativePath);
        }

        this.saveOrUpdate(customsClearance);
        return CommonResult.success("保存清关，成功！");
    }

    @Override
    public List<CustomsClearanceVO> findCustomsClearance() {
        QueryWrapper<CustomsClearance> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("","");
        List<CustomsClearance> list = this.list(queryWrapper);
        List<CustomsClearanceVO> customsClearanceVOS = ConvertUtil.convertList(list, CustomsClearanceVO.class);
        return customsClearanceVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditCustomsClearance(AuditCustomsClearanceForm form) {
        AuthUser user = baseService.getUser();
        Long id = form.getId();
        CustomsClearanceVO customsClearanceVO = customsClearanceMapper.findCustomsClearanceById(id);
        if(ObjectUtil.isEmpty(customsClearanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "清关商品不存在");
        }
        CustomsClearance customsClearance = ConvertUtil.convert(customsClearanceVO, CustomsClearance.class);
        Integer auditStatus = form.getAuditStatus();//审核状态(0待审核 1已审核 2已取消)
        customsClearance.setAuditStatus(auditStatus);
        customsClearance.setAuditUserId(user.getId().intValue());
        customsClearance.setAuditUserName(user.getName());
        this.saveOrUpdate(customsClearance);
    }
}
