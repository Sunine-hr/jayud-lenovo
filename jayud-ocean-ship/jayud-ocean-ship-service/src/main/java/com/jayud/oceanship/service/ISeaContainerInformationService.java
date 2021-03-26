package com.jayud.oceanship.service;

import com.jayud.oceanship.po.SeaContainerInformation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 货柜信息表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-25
 */
public interface ISeaContainerInformationService extends IService<SeaContainerInformation> {

    List<SeaContainerInformation> getList(Long id);
}
