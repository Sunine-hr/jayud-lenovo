package com.jayud.tools.thread;

import lombok.Getter;
import lombok.Setter;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.io.InputStream;

/**
 * 提供管道流接入
 * @author ZJ
 *
 */
public class MediaStreamReader {

	@Getter@Setter
	private InputStream in;

	public MediaStreamReader(InputStream in) {
		super();
		this.in = in;
	}
	
	public void init() {
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(in);
		
		/**
		 * 待完善
		 */
	}
}
