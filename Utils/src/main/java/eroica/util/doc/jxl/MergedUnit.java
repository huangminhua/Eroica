package eroica.util.doc.jxl;

/**
 * This class describe the properties of a merged unit.
 * 
 * @author Minhua HUANG
 *
 */
public class MergedUnit {
	private int left;// left column
	private int right;// right column
	private int top;// top row
	private int bottom;// bottom row

	public MergedUnit(int top, int bottom, int left, int right) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}
}
