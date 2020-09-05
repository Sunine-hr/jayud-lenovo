package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.Work;
import com.jayud.model.vo.WorkVO;

import java.util.List;

/**
 * 岗位表
 */
public interface ISystemWorkService extends IService<Work> {

    /**
     * 获取用户角色
     * @return
     */
    List<WorkVO> findWork();



}
