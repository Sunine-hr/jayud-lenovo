package com.jayud.tools.thread;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegLogCallback;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 媒体转换者
 * @author ZJ
 *
 */
public abstract class MediaTransfer {
	
	static {
		avutil.av_log_set_level(avutil.AV_LOG_ERROR);
		FFmpegLogCallback.set();
	}

	/**
	 * ws客户端
	 */
	public ConcurrentHashMap<String, ChannelHandlerContext> wsClients = new ConcurrentHashMap<>();
	
	/**
	 * http客户端
	 */
	public ConcurrentHashMap<String, ChannelHandlerContext> httpClients = new ConcurrentHashMap<>();
	
	/**
	 * 当前在线人数
	 */
	public int hcSize, wcSize = 0;

	/**
	 * 用于没有客户端时候的计时
	 */
	public int noClient = 0;
	
	/**
	 * flv header
	 */
	public byte[] header = null;
	
	/**
	 * 输出流，视频最终会输出到此
	 */
	public ByteArrayOutputStream bos = new ByteArrayOutputStream();
	
	/**
	 * 转码回调
	 */
	@Getter@Setter
	public TransferCallback transferCallback;
	
//	public void addClient() {
//		
//	}
}
