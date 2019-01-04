package eroica.util.jxl;

/**
 * Border thickness template determined by cell position.
 * 
 * @author Minhua HUANG
 *
 */
public enum BorderThicknessTemplate {
	/* top line: left, middle or right */
	TOP_LEFT(2, 1, 2, 1), TOP_MIDDLE(2, 1, 1, 1), TOP_RIGHT(2, 1, 1, 2),
	/* body line: left, middle or right */
	BODY_LEFT(1, 1, 2, 1), BODY_MIDDLE(1, 1, 1, 1), BODY_RIGHT(1, 1, 1, 2),
	/* bottom line: left, middle or right */
	BOTTOM_LEFT(1, 2, 2, 1), BOTTOM_MIDDLE(1, 2, 1, 1), BOTTOM_RIGHT(1, 2, 1, 2),
	/* when a whole line is merged: the line is at: top, body or bottom */
	TOP_MERGED(2, 1, 2, 2), BODY_MERGED(1, 1, 2, 2), BOTTOM_MERGED(1, 2, 2, 2),
	/* when a whole column is merged: the column is at: left, middle or right */
	LEFT_MERGED(2, 2, 2, 1), MIDDLE_MERGED(2, 2, 1, 1), RIGHT_MERGED(2, 2, 1, 2),
	/* when the whole table has only one cell */
	ALL_MERGED(2, 2, 2, 2),
	/* when the cell is out of the table such as the title */
	NONE(0, 0, 0, 0);

	private int topThickness;
	private int bottomThickness;
	private int leftThickness;
	private int rightThickness;

	private BorderThicknessTemplate(int topThickness, int bottomThickness, int leftThickness, int rightThickness) {
		this.topThickness = topThickness;
		this.bottomThickness = bottomThickness;
		this.leftThickness = leftThickness;
		this.rightThickness = rightThickness;
	}

	/**
	 * Get the recommended template of a cell by its position.
	 * 
	 * @param isAtTop    whether the cell is at top
	 * @param isAtBottom whether the cell is at bottom
	 * @param isAtLeft   whether the cell is at left
	 * @param isAtRight  whether the cell is at right
	 * @return the recommended template of a cell
	 */
	public static BorderThicknessTemplate getRecommendedTemplate(boolean isAtTop, boolean isAtBottom, boolean isAtLeft,
			boolean isAtRight) {
		for (BorderThicknessTemplate btt : values())
			if ((btt.topThickness == 2) == isAtTop && (btt.bottomThickness == 2) == isAtBottom
					&& (btt.leftThickness == 2) == isAtLeft && (btt.rightThickness == 2) == isAtRight)
				return btt;
		return NONE;
	}

	/**
	 * Get the recommended template of a cell by its position.
	 * 
	 * @param column      the column of a cell
	 * @param row         the row of a cell
	 * @param columnCount the whole column count of a table
	 * @param rowCount    the whole row count of a table
	 * @return the recommended template of a cell
	 */
	public static BorderThicknessTemplate getRecommendedTemplate(int column, int row, int columnCount, int rowCount) {
		return getRecommendedTemplate(row == 0, row == rowCount - 1, column == 0, column == columnCount - 1);
	}

	/**
	 * Get the recommended template of a merged cell by its position.
	 * 
	 * @param column      the column of a cell
	 * @param row         the row of a cell
	 * @param columnCount the whole column count of a table
	 * @param rowCount    the whole row count of a table
	 * @return the recommended template of a merged cell
	 */
	public static BorderThicknessTemplate getRecommendedTemplate(MergedUnit mergedUnit, int columnCount, int rowCount) {
		return getRecommendedTemplate(mergedUnit.getTop() == 0, mergedUnit.getBottom() == rowCount - 1,
				mergedUnit.getLeft() == 0, mergedUnit.getRight() == columnCount - 1);
	}

	public int getTopThickness() {
		return topThickness;
	}

	public int getBottomThickness() {
		return bottomThickness;
	}

	public int getLeftThickness() {
		return leftThickness;
	}

	public int getRightThickness() {
		return rightThickness;
	}
}
