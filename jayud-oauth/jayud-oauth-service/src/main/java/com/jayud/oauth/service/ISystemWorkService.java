package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.po.Work;
import com.jayud.oauth.model.vo.WorkVO;

import java.util.List;

/**
 * 岗位表
 */
public interface ISystemWorkService extends IService<Work> {

    /**
     * 获取部门下的岗位
     * @return
     */
    List<WorkVO> findWork(Long departmentId);



}
