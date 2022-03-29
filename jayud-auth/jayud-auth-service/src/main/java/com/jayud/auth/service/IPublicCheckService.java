package com.jayud.auth.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.CheckForm;
import com.jayud.auth.model.po.BPublicCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.auth.model.vo.BPublicCheckVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 审核记录表 服务类
 *
 * @author jayud
 * @since 2022-02-28
 */
public interface IPublicCheckService extends IService<BPublicCheck> {


    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-02-28
     * @param: bPublicCheck
     * @param: currentPage
     * @param: pageSize
     * @param: req
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.BPublicCheck>
     **/
    IPage<BPublicCheckVO> selectPage(BPublicCheck bPublicCheck,
                                     Integer currentPage,
                                     Integer pageSize,
                                     HttpServletRequest req);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-02-28
     * @param: bPublicCheck
     * @param: req
     * @return: java.util.List<com.jayud.auth.model.po.BPublicCheck>
     **/
    List<BPublicCheckVO> selectList(BPublicCheck bPublicCheck);



    /**
     * @description 物理删除
     * @author  jayud
     * @date   2022-02-28
     * @param: id
     * @return: void
     **/
    void phyDelById(Long id);


    /**
    * @description 逻辑删除
    * @author  jayud
    * @date   2022-02-28
    * @param: id
    * @return: com.jyd.component.commons.result.Result
    **/
    void logicDel(Long id);

    /**
     * 审核
     * @param checkForm
     * @return
     */
    BaseResult check(CheckForm checkForm);

    /**
     * 反审
     * @param checkForm
     * @return
     */
    BaseResult unCheck(CheckForm checkForm);

    /**
     * 获取审核信息
     * @param checkForm
     * @return
     */
    List<BPublicCheckVO> getList(CheckForm checkForm);
}
