package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.scm.feign.FileClient;
import com.jayud.scm.model.bo.AddBPublicFilesForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.enums.SignEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.mapper.BPublicFilesMapper;
import com.jayud.scm.model.vo.BPublicFilesVO;
import com.jayud.scm.service.IBPublicFilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICommodityFollowService;
import com.jayud.scm.service.ICustomerFollowService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 上传附件表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class BPublicFilesServiceImpl extends ServiceImpl<BPublicFilesMapper, BPublicFiles> implements IBPublicFilesService {

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICommodityFollowService commodityFollowService;

    @Autowired
    private ICustomerFollowService customerFollowService;

    @Override
    public List<BPublicFilesVO> getPublicFileList(Integer fileModel, Integer businessId) {

        //获取根路径
        String path = (String)fileClient.getBaseUrl().getData();

        QueryWrapper<BPublicFiles> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BPublicFiles::getFileModel,fileModel);
        queryWrapper.lambda().eq(BPublicFiles::getBusinessId,businessId);
        queryWrapper.lambda().eq(BPublicFiles::getVoided,0);
        List<BPublicFiles> list = this.list(queryWrapper);
        List<BPublicFilesVO> bPublicFilesVOS = ConvertUtil.convertList(list, BPublicFilesVO.class);
        for (BPublicFilesVO bPublicFilesVO : bPublicFilesVOS) {
            bPublicFilesVO.setFileView(StringUtils.getFileViews(bPublicFilesVO.getSFilePath(),bPublicFilesVO.getSFileName(),path));
        }
        return bPublicFilesVOS;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
//        List<BPublicFiles> bPublicFiles = new ArrayList<>();
        List<CommodityFollow> commodityFollows = new ArrayList<>();
        for (Long id : deleteForm.getIds()) {
//            BPublicFiles publicFiles = new BPublicFiles();
//            publicFiles.setId(id.intValue());
//            publicFiles.setVoided(1);
//            publicFiles.setVoidedBy(deleteForm.getId().intValue());
//            publicFiles.setVoidedByDtm(deleteForm.getDeleteTime());
//            publicFiles.setVoidedByName(deleteForm.getName());
//            bPublicFiles.add(publicFiles);

            CommodityFollow commodityFollow = new CommodityFollow();
            commodityFollow.setCommodityId(id.intValue());
            commodityFollow.setSType(OperationEnum.DELETE.getCode());
            commodityFollow.setFollowContext("附件信息" + OperationEnum.DELETE.getDesc()+id);
            commodityFollow.setCrtBy(deleteForm.getId().intValue());
            commodityFollow.setCrtByDtm(deleteForm.getDeleteTime());
            commodityFollow.setCrtByName(deleteForm.getName());
            commodityFollows.add(commodityFollow);
        }
//        boolean b = this.updateBatchById(bPublicFiles);
//        if(b){
//            log.warn("附件删除成功："+bPublicFiles);
//            boolean b1 = commodityFollowService.saveBatch(commodityFollows);
//            if(!b1){
//                log.warn("操作记录表添加失败"+commodityFollows);
//            }
//        }
        boolean b1 = commodityFollowService.saveBatch(commodityFollows);
        if(!b1){
            log.warn("操作记录表添加失败"+commodityFollows);
        }
        return b1;
    }

    @Override
    public boolean AddPublicFile(AddBPublicFilesForm filesForm) {

        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        for (FileView fileView : filesForm.getFileView()) {
            BPublicFiles files = ConvertUtil.convert(filesForm, BPublicFiles.class);
            files.setFileType(filesForm.getFileModelCopy());
            files.setSFileName(StringUtils.getFileNameStr(Collections.singletonList(fileView)));
            files.setSFilePath(StringUtils.getFileStr(Collections.singletonList(fileView)));
            files.setCrtBy(systemUser.getId().intValue());
            files.setCrtByDtm(LocalDateTime.now());
            files.setCrtByName(systemUser.getUserName());
            boolean save = this.save(files);
            if(!save){
                log.warn("附件上传失败，id为"+filesForm.getBusinessId());
                return false;
            }
            if(filesForm.getFileModel().equals(1)){
                CommodityFollow commodityFollow = new CommodityFollow();
                commodityFollow.setCrtBy(systemUser.getId().intValue());
                commodityFollow.setCrtByDtm(LocalDateTime.now());
                commodityFollow.setCrtByName(systemUser.getUserName());
                commodityFollow.setCommodityId(filesForm.getBusinessId());
                commodityFollow.setSType(OperationEnum.INSERT.getCode());
                commodityFollow.setFollowContext("附件上传失败，商品id为"+filesForm.getBusinessId());
                boolean save1 = commodityFollowService.save(commodityFollow);
                if(!save1){
                    log.warn("商品操作日志，附件上传失败，id为"+filesForm.getBusinessId());
                }

            }
            if(filesForm.getFileModel().equals(2)){
                CustomerFollow customerFollow = new CustomerFollow();
                customerFollow.setCrtBy(systemUser.getId().intValue());
                customerFollow.setCrtByDtm(LocalDateTime.now());
                customerFollow.setCrtByName(systemUser.getUserName());
                customerFollow.setCustomerId(filesForm.getBusinessId());
                customerFollow.setSType(OperationEnum.INSERT.getCode());
                customerFollow.setFollowContext("附件上传失败，商品id为"+filesForm.getBusinessId());
                boolean save1 = customerFollowService.save(customerFollow);
                if(!save1){
                    log.warn("客户操作日志，附件上传失败，id为"+filesForm.getBusinessId());
                }

            }
        }


        return true;
    }
}
