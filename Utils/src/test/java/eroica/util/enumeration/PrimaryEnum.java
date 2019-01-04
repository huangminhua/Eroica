package eroica.util.enumeration;

public enum PrimaryEnum {
	FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6);
	private int index;

	private PrimaryEnum(int index) {
		this.index = index;
	}

	public int getId() {
		return index;
	}

	public static enum CompositeEnum {
		GROUP_1(1, FIRST, SECOND), GROUP_2(2, THIRD, FOURTH, FIFTH), GROUP_3(3, SIXTH);
		private int index;
		private PrimaryEnum[] primitives;

		private CompositeEnum(int index, PrimaryEnum... primitives) {
			this.index = index;
			this.primitives = primitives;
		}

		@GetId
		public int getIndex() {
			return index;
		}

		@GetPrimaries
		public PrimaryEnum[] getPrimitives() {
			return primitives.clone();
		}
	}
}
