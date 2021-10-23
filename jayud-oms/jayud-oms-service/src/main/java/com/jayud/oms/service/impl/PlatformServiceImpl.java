package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.StorageClient;
import com.jayud.oms.mapper.PlatformMapper;
import com.jayud.oms.model.bo.QueryPlatformForm;
import com.jayud.oms.model.po.Platform;
import com.jayud.oms.model.vo.PlatformDetailsVO;
import com.jayud.oms.model.vo.PlatformVO;
import com.jayud.oms.service.IPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 月台管理 服务实现类
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
@Service
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform> implements IPlatformService {

    @Autowired
    private StorageClient storageClient;

    /**
     * 分页查询月台
     * @param form
     * @return
     */
    @Override
    public IPage<PlatformVO> findPlatformByPage(QueryPlatformForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findPlatformByPage(page, form);
    }

    /**
     * 校验唯一性
     * @param platform
     * @return
     */
    @Override
    public boolean checkUniqueByName(Platform platform) {
        QueryWrapper<Platform> condition = new QueryWrapper<>();
        if (platform.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(Platform::getId, platform.getId())
                    .eq(Platform::getPlatformName, platform.getPlatformName()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己名称,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(Platform::getPlatformName, platform.getPlatformName());
        return this.count(condition) > 0;
    }

    /**
     * 校验唯一性
     * @param platform
     * @return
     */
    @Override
    public boolean checkUniqueByCode(Platform platform) {
        QueryWrapper<Platform> condition = new QueryWrapper<>();
        if (platform.getId() != null) {
            condition.lambda().and(tmp -> tmp.eq(Platform::getId, platform.getId())
                    .eq(Platform::getPlatformCode, platform.getPlatformCode()));
            int count = this.count(condition);
            if (count > 0) {
                //匹配到自己编码,不进行唯一校验
                return false;
            }
        }
        condition = new QueryWrapper<>();
        condition.lambda().eq(Platform::getPlatformCode, platform.getPlatformCode());
        return this.count(condition) > 0;
    }

    /**
     * 获取月台编号
     * @return
     */
    @Override
    public String autoGenerateNum() {
        StringBuilder orderNo = new StringBuilder();
        String curDate = DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd");
        Map<String, Object> platform = this.baseMapper.getLastCodeByCreateTime(curDate);
        int index = 1;
        if (platform != null) {
            String sn = MapUtil.getStr(platform, "code").substring(10);
            index = Integer.parseInt(sn) + 1;
        }
        orderNo.append("YT").append(curDate).append(StringUtils.zeroComplement(4, index));
        return orderNo.toString();
    }

    /**
     * 新增编辑月台
     * @param platform
     * @return
     */
    @Override
    public boolean saveOrUpdatePlatform(Platform platform) {
        // 仓库
        if (!Objects.isNull(platform.getWarehouseId())) {
            CommonResult result = storageClient.findWarehouseNameById(platform.getWarehouseId());
            if (!Objects.isNull(result.getData())) {
                JSONObject jsonObject = new JSONObject(result.getData());
                platform.setWarehouseName(jsonObject.getStr("name"));
            }
        }

        if (Objects.isNull(platform.getId())) {
            platform.setCreateTime(LocalDateTime.now())
                    .setCreatedUser(UserOperator.getToken());
            return this.save(platform);
        } else {
            platform.setPlatformCode(null);
            platform
                    .setUpTime(LocalDateTime.now())
                    .setUpUser(UserOperator.getToken());
            return this.updateById(platform);
        }
    }

    /**
     * 检查月台是否存在
     * @param id
     * @return
     */
    @Override
    public boolean checkExists(Long id) {
        return this.count(new QueryWrapper<Platform>().lambda().eq(Platform::getId, id)) > 0;
    }

    /**
     * 删除月台
     * @param id
     */
    @Override
    public void delPlatform(Long id) {
        this.remove(new QueryWrapper<Platform>().lambda().eq(Platform::getId, id));
    }

    /**
     * 查看月台详情
     * @param id
     * @return
     */
    @Override
    public PlatformDetailsVO getPlatformDetails(Long id) {
        return this.baseMapper.getPlatformDetails(id);
    }
}
