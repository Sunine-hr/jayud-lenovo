package com.jayud.tools.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.jayud.tools.common.AjaxResult;
import com.jayud.tools.dto.CameraDto;
import com.jayud.tools.entity.Camera;
import com.jayud.tools.mapper.CameraMapper;
import com.jayud.tools.service.MediaService;
import com.jayud.tools.vo.CameraVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * api管理接口
 * 后续可能改为使用数据库
 * @author ZJ
 *
 */
@RestController
@RequestMapping("/camera")
@Api(tags = "摄像头，监视器接口")
public class StreamController {
	
	@Autowired
	private MediaService mediaService;
	@Autowired
	private CameraMapper cameraMapper;
	
	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("page")
	public AjaxResult page(Long pageNum, Long pageSize) {
		if(null == pageNum || pageNum <= 1) {
			pageNum = 1L;
		}
		if(null == pageSize || pageSize <= 10) {
			pageSize = 10L;
		}
		Page<Camera> page = new Page<>();
		page.setCurrent(pageNum);
		page.setSize(pageSize);
		page.addOrder(OrderItem.desc("id"));
		
		QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
		Page<Camera> data = cameraMapper.selectPage(page, queryWrapper);
		return AjaxResult.success(data);
	}
	
	/**
	 * 查询所有流
	 * @return
	 */
	@PostMapping("list")
	public AjaxResult list() {
		List<Camera> data = cameraMapper.selectList(null);
		List<CameraVo> rData = new ArrayList<>();
		for (Camera camera : data) {
			CameraVo cameraVo = new CameraVo();
			cameraVo.setId(String.valueOf(camera.getId()));
			cameraVo.setEnabledFlv(camera.getFlv() == 1 ? true : false);
			cameraVo.setEnabledHls(camera.getHls() == 1 ? true : false);
			cameraVo.setMode(camera.getFfmpeg() == 1 ? "ffmpeg" : "javacv");
			cameraVo.setRemark(camera.getRemark());
			cameraVo.setUrl(camera.getUrl());
			rData.add(cameraVo);
		}
		return AjaxResult.success(rData);
	}

	/**
	 * 新增流
	 * @param cameraVo
	 * @return
	 */
	@RequestMapping("add")
	public AjaxResult add(CameraVo cameraVo) {
		String digestHex = MD5.create().digestHex(cameraVo.getUrl());
		
		Camera camera = new Camera();
		camera.setMediaKey(digestHex);
		
		QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("media_key", digestHex);
		Integer selectCount = cameraMapper.selectCount(queryWrapper);
		if(selectCount > 0) {
			return AjaxResult.error("数据库里已有此地址");
		}
		
		camera.setUrl(cameraVo.getUrl());
		camera.setRemark(cameraVo.getRemark());
		int save = cameraMapper.insert(camera);
		return save == 1 ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
	}
	
	/**
	 * 编辑流
	 * @param cameraDto
	 * @return
	 */
	@RequestMapping("edit")
	public AjaxResult edit(CameraVo cameraVo) {
		String digestHex = MD5.create().digestHex(cameraVo.getUrl());
		QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("media_key", digestHex);
		Camera selectOne = cameraMapper.selectOne(queryWrapper);
		if(null != selectOne) {
			int res = cameraMapper.update(selectOne, queryWrapper);
		}
		
		return AjaxResult.success("编辑成功");
	}
	
	/**
	 * 删除流（会停止推流）
	 * @param cameraDto
	 * @return
	 */
	@RequestMapping("del")
	public AjaxResult del(CameraVo cameraVo) {
		String digestHex = MD5.create().digestHex(cameraVo.getUrl());
		Camera camera = new Camera();
		camera.setMediaKey(digestHex);
		
		QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("media_key", digestHex);
		cameraMapper.delete(queryWrapper);
		
		CameraDto cameraDto = new CameraDto();
		cameraDto.setUrl(cameraVo.getUrl());
		cameraDto.setMediaKey(digestHex);
		mediaService.closeForApi(cameraDto);
		return AjaxResult.success("删除成功");
	}
	
	/**
	 * 停止推流
	 * @param cameraDto
	 * @return
	 */
	@RequestMapping("stop")
	public AjaxResult stop(CameraVo cameraVo) {
		String digestHex = MD5.create().digestHex(cameraVo.getUrl());
		CameraDto cameraDto = new CameraDto();
		
		Camera camera = new Camera();
		camera.setFlv(0);
		QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("media_key", digestHex);
		int res = cameraMapper.update(camera, queryWrapper);
		
		cameraDto.setUrl(cameraVo.getUrl());
		cameraDto.setMediaKey(digestHex);
		mediaService.closeForApi(cameraDto);
		return AjaxResult.success("停止推流成功");
	}
	
	/**
	 * 开始推流
	 * @param cameraDto
	 * @return
	 */
	@RequestMapping("start")
	public AjaxResult start(CameraVo cameraVo) {
		String digestHex = MD5.create().digestHex(cameraVo.getUrl());
		CameraDto cameraDto = new CameraDto();
		cameraDto.setUrl(cameraVo.getUrl());
		cameraDto.setMediaKey(digestHex);
		cameraDto.setAutoClose(false);
		boolean playForApi = mediaService.playForApi(cameraDto);
		
		if(playForApi) {
			Camera camera = new Camera();
			QueryWrapper<Camera> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("media_key", digestHex);
			camera.setFlv(1);
			int res = cameraMapper.update(camera, queryWrapper);
		}
		
		return playForApi ? AjaxResult.success("开启推流成功") : AjaxResult.error("开启失败");
	}
	
}
