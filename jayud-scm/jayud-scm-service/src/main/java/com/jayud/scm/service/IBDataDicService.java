package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddBDataDicForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BDataDic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.BDataDicVO;

/**
 * <p>
 * 数据字典主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface IBDataDicService extends IService<BDataDic> {

    IPage<BDataDicVO> findByPage(QueryForm form);

    BDataDicVO getBDataDicById(Integer id);

    BDataDic getBDataDicByDicCode(String dicCode);

    boolean saveOrUpdateBDataDic(AddBDataDicForm form);
}
