package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddHsCodeForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.HsCode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HsCodeFormVO;
import com.jayud.scm.model.vo.HsCodeVO;

import java.util.List;

/**
 * <p>
 * 海关编码表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
public interface IHsCodeService extends IService<HsCode> {

    IPage<HsCodeFormVO> findByPage(QueryForm form);

    boolean delete(DeleteForm deleteForm);

    boolean saveOrUpdateHsCode(AddHsCodeForm form);

    HsCodeVO getHsCodeDetail(Integer id);

    List<String> findHsCode(String codeNo);

    HsCodeVO getHsCodeByCodeNo(String hsCodeNo);

    HsCodeVO getHsCodeDetailByCodeNo(String codeNo);
}
