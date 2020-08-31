package eroica.util.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Join relation result of JoinUtils.join
 * 
 * @author Minhua HUANG
 *
 */
public class JoinRelation<LR> implements Iterable<JoinRelation<LR>.Row> {
	private List<Row> eList = new ArrayList<Row>();

	JoinRelation() {
	}

	void add(List<LR> l, List<LR> r) {
		if (l == null) {
			for (LR re : r)
				eList.add(new Row(null, re));
		} else if (r == null) {
			for (LR le : l)
				eList.add(new Row(le, null));
		} else {
			for (LR le : l)
				for (LR re : r)
					eList.add(new Row(le, re));
		}
	}

	public int size() {
		return eList.size();
	}

	public boolean isEmpty() {
		return eList.isEmpty();
	}

	public void sort(Comparator<? super JoinRelation<LR>.Row> c) {
		eList.sort(c);
	}

	public JoinRelation<LR>.Row get(int index) {
		return eList.get(index);
	}

	public class Row {
		private LR left;
		private LR right;

		private Row(LR l, LR r) {
			this.left = l;
			this.right = r;
		}

		public LR getLeft() {
			return left;
		}

		public LR getRight() {
			return right;
		}
	}

	@Override
	public Iterator<JoinRelation<LR>.Row> iterator() {
		return new Iterator<JoinRelation<LR>.Row>() {
			Iterator<JoinRelation<LR>.Row> it = eList.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public JoinRelation<LR>.Row next() {
				return it.next();
			}
		};
	}
}
