package eroica.util.jxl;

import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;

/**
 * The fonts recommended in this project.
 * 
 * @author Minhua HUANG
 *
 */
public class RecommendedFonts {
	private static final FontName SONG = WritableFont.createFont("宋体");
	private static final FontName YAHEI = WritableFont.createFont("微软雅黑");
	private static final int TITLE_FONT_SIZE = 16;
	private static final int HEAD_FONT_SIZE = 12;
	private static final int DATA_FONT_SIZE = 12;

	public static WritableFont arialTitle() {
		return new WritableFont(WritableFont.ARIAL, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont timesTitle() {
		return new WritableFont(WritableFont.TIMES, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont courierTitle() {
		return new WritableFont(WritableFont.COURIER, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont tahomaTitle() {
		return new WritableFont(WritableFont.TAHOMA, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont songTitle() {
		return new WritableFont(SONG, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont yaheiTitle() {
		return new WritableFont(YAHEI, TITLE_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont arialHead() {
		return new WritableFont(WritableFont.ARIAL, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont timesHead() {
		return new WritableFont(WritableFont.TIMES, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont courierHead() {
		return new WritableFont(WritableFont.COURIER, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont tahomaHead() {
		return new WritableFont(WritableFont.TAHOMA, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont songHead() {
		return new WritableFont(SONG, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont yaheiHead() {
		return new WritableFont(YAHEI, HEAD_FONT_SIZE, WritableFont.BOLD);
	}

	public static WritableFont arialBody() {
		return new WritableFont(WritableFont.ARIAL, DATA_FONT_SIZE, WritableFont.NO_BOLD);
	}

	public static WritableFont timesBody() {
		return new WritableFont(WritableFont.TIMES, DATA_FONT_SIZE, WritableFont.NO_BOLD);
	}

	public static WritableFont courierBody() {
		return new WritableFont(WritableFont.COURIER, DATA_FONT_SIZE, WritableFont.NO_BOLD);
	}

	public static WritableFont tahomaBody() {
		return new WritableFont(WritableFont.TAHOMA, DATA_FONT_SIZE, WritableFont.NO_BOLD);
	}

	public static WritableFont songBody() {
		return new WritableFont(SONG, 12, WritableFont.NO_BOLD);
	}

	public static WritableFont taheiBody() {
		return new WritableFont(YAHEI, 12, WritableFont.NO_BOLD);
	}

	public static WritableFont defaultTitle() {
		return timesTitle();
	}

	public static WritableFont defaultHead() {
		return timesHead();
	}

	public static WritableFont defaultBody() {
		return timesBody();
	}
}
