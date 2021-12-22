package com.jayud.tools.service;

import com.jayud.tools.model.bo.AddFbaOrderTrackForm;
import com.jayud.tools.model.po.FbaOrderTrack;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * fba订单轨迹表 服务类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
public interface IFbaOrderTrackService extends IService<FbaOrderTrack> {

    List<FbaOrderTrack> getFbaOrderTrackByOrderId(Integer id);

    void deleteById(List<Long> ids);

    void saveOrUpdateFbaOrderTrack(AddFbaOrderTrackForm addFbaOrderTrackForm);
}
