package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddBPublicFilesForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.po.BPublicFiles;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BPublicFilesVO;

import java.util.List;

/**
 * <p>
 * 上传附件表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IBPublicFilesService extends IService<BPublicFiles> {

    List<BPublicFilesVO> getPublicFileList(Integer fileModel, Integer businessId);

    boolean delete(DeleteForm deleteForm);

    boolean AddPublicFile(AddBPublicFilesForm filesForm);
}
