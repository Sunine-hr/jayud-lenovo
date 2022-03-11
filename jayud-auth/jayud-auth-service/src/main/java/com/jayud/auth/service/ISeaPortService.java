package com.jayud.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.auth.model.bo.AddSeaPortForm;
import com.jayud.auth.model.bo.QuerySeaPortForm;
import com.jayud.auth.model.po.SeaPort;
import com.jayud.auth.model.vo.SeaPortVO;
import com.jayud.common.entity.InitComboxStrVO;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 * 船港口地址表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
public interface ISeaPortService extends IService<SeaPort> {

    /**
     * 获取港口列表
     * @return
     */
    List<InitComboxStrVO> initSeaPort();

    String getPortName(String portDepartureCode);

    IPage<SeaPortVO> findByPage(QuerySeaPortForm form);

    SeaPort isCodeExistence(String code);

    SeaPort isNameExistence(String name);

    boolean saveOrUpdateSeaPort(AddSeaPortForm form);

    boolean deleteSeaPort(Long id);

    boolean saveOrUpdateBatchSeaPort(List<AddSeaPortForm> list);
}
