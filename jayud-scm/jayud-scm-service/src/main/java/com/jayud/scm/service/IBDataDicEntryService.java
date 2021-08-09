package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddBDataDicEntryForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BDataDic;
import com.jayud.scm.model.po.BDataDicEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BDataDicEntryVO;

import java.util.List;

/**
 * <p>
 * 数据字典明细表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface IBDataDicEntryService extends IService<BDataDicEntry> {

    List<BDataDicEntryVO> getDropDownList(String dicCode);

    IPage<BDataDicEntryVO> findByPage(QueryCommonForm form);

    BDataDicEntry getBDataDicEntryByDicCode(String dicCode, String dataValue);

    boolean saveOrUpdateBDataDicEntry(AddBDataDicEntryForm form);

    BDataDicEntryVO getBDataDicEntryId(Integer id);

    boolean delete(DeleteForm deleteForm);
}
