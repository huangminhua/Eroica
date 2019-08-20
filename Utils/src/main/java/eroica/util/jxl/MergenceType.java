package eroica.util.jxl;

import java.util.UUID;

import eroica.util.enumeration.GetId;

/**
 * These are the commands of how to merge cells. Used by ExcelGenerator.
 * 
 * @author Minhua HUANG
 *
 */
public enum MergenceType {
	// merged with up cell
	MERGE_TO_UP("MERGE_TO_UP_" + UUID.randomUUID()),
	// merged with down cell
	MERGE_TO_DOWN("MERGE_TO_DOWN_" + UUID.randomUUID()),
	// merged with left cell
	MERGE_TO_LEFT("MERGE_TO_LEFT_" + UUID.randomUUID()),
	// merged with right cell
	MERGE_TO_RIGHT("MERGE_TO_RIGHT_" + UUID.randomUUID()),
	// merged with up-left cell
	MERGE_TO_UP_LEFT("MERGE_TO_UP_LEFT_" + UUID.randomUUID()),
	// merged with up-right cell
	MERGE_TO_UP_RIGHT("MERGE_TO_UP_RIGHT_" + UUID.randomUUID()),
	// merged with down-left cell
	MERGE_TO_DOWN_LEFT("MERGE_TO_DOWN_LEFT_" + UUID.randomUUID()),
	// merged with down-right cell
	MERGE_TO_DOWN_RIGHT("MERGE_TO_DOWN_RIGHT_" + UUID.randomUUID());
	private String typeId;

	private MergenceType(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * Get the id of this MergenceType element, which can be used as a placeholder
	 * in a table, with which an xls file is to be generated.
	 * 
	 * @return id of this MergenceType element
	 */
	@GetId
	public String getTypeId() {
		return typeId;
	}

	/**
	 * Same as getTypeId.Get the id of this MergenceType element, which can be used
	 * as a placeholder in a table, with which an xls file is to be generated.
	 * 
	 * @return id of this MergenceType element
	 */
	@Override
	public String toString() {
		return typeId;
	}
}
