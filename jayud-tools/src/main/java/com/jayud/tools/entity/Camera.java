package com.jayud.tools.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;

/**
 * camera相机，摄影机 实体类
 * @author ZJ
 *
 */
@Getter
@Setter
//@TableName("camera")
//@Entity
//@Table(name="camera")	//jpa自动创建表
public class Camera implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5575352151805386129L;
	/**
	 * id
	 */
	//@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	/**
	 * url
	 */
	//@Column
	private String url;
	/**
	 * 备注
	 */
	//@Column
	private String remark;
	/**
	 * FlashVideo
	 */
	//@Column
	private int flv;
	/**
	 * hls流媒体
	 */
	//@Column
	private int hls;
	/**
	 * FFmpeg
	 */
	//@Column
	private int ffmpeg;
	/**
	 * 自动关闭
	 */
	//@Column
	private int autoClose;
	/**
	 * 类型
	 */
	//@Column
	private int type = 0;
	/**
	 * 媒体key，标识url的唯一性
	 */
	//@Column
	private String mediaKey;
}
