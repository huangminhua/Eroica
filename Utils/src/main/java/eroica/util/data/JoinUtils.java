package eroica.util.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Try this class if the two result sets fetched from two databases are to
 * joined together.
 * 
 * @author Minhua HUANG
 *
 */
public class JoinUtils {

	/**
	 * Join the two result sets.
	 * 
	 * @param <LR>          the two result sets should extend the same LR class
	 * @param <V>           the type that result translated to
	 * @param lc            the left result set
	 * @param rc            the left result set
	 * @param joinOn        the join condition and the sort condition
	 * @param joinType      the join type
	 * @param resultBuilder the function of translating JoinRelation to V
	 * @return
	 */
	public static <LR, V> List<V> join(Collection<? extends LR> lc, Collection<? extends LR> rc,
			Comparator<? super LR> joinOn, JoinType joinType, Function<JoinRelation<LR>.Row, V> beanBuilder) {
		JoinRelation<LR> jr = join(lc, rc, joinOn, joinType);
		List<V> l = new ArrayList<V>(jr.size());
		for (JoinRelation<LR>.Row e : jr)
			l.add(beanBuilder.apply(e));
		return l;
	}

	/**
	 * Join the two result sets.
	 * 
	 * @param <LR>     the two result sets should extend the same LR class
	 * @param lc       the left result set
	 * @param rc       the left result set
	 * @param joinOn   the sort condition of lc and rc, and the join condition of
	 *                 them
	 * @param joinType the join type
	 * @return
	 */
	public static <LR> JoinRelation<LR> join(Collection<? extends LR> lc, Collection<? extends LR> rc,
			Comparator<? super LR> joinOn, JoinType joinType) {
		List<LR> l = new ArrayList<LR>(lc.size());
		l.addAll(lc);
		l.sort(joinOn);
		List<LR> r = new ArrayList<LR>(rc.size());
		r.addAll(rc);
		r.sort(joinOn);
		Iterator<LR> lIt = l.iterator();
		Iterator<LR> rIt = r.iterator();
		JoinRelation<LR> jr = new JoinRelation<LR>();
		LR lCur = null;
		LR rCur = null;
		List<LR> lTmp = new ArrayList<LR>();
		List<LR> rTmp = new ArrayList<LR>();
		while (true) {
			if (lTmp.isEmpty()) {
				if (lCur != null) {
					lTmp.add(lCur);
					lCur = null;
				}
				while (lIt.hasNext()) {
					lCur = lIt.next();
					if (lCur == null)
						continue;
					if (lTmp.isEmpty() || joinOn.compare(lCur, lTmp.get(0)) == 0) {
						lTmp.add(lCur);
						lCur = null;
					} else
						break;
				}
			}
			if (rTmp.isEmpty()) {
				if (rCur != null) {
					rTmp.add(rCur);
					rCur = null;
				}
				while (rIt.hasNext()) {
					rCur = rIt.next();
					if (rCur == null)
						continue;
					if (rTmp.isEmpty() || joinOn.compare(rCur, rTmp.get(0)) == 0) {
						rTmp.add(rCur);
						rCur = null;
					} else
						break;
				}
			}
			if (lTmp.isEmpty() && rTmp.isEmpty())
				return jr;
			else if (lTmp.isEmpty()) {
				if (JoinType.FULL_OUTER_JOIN.equals(joinType) || JoinType.RIGHT_OUTER_JOIN.equals(joinType))
					jr.add(null, rTmp);
				rTmp.clear();
			} else if (rTmp.isEmpty()) {
				if (JoinType.FULL_OUTER_JOIN.equals(joinType) || JoinType.LEFT_OUTER_JOIN.equals(joinType))
					jr.add(lTmp, null);
				lTmp.clear();
			} else {
				int comp = joinOn.compare(lTmp.get(0), rTmp.get(0));
				if (comp < 0) {
					if (JoinType.FULL_OUTER_JOIN.equals(joinType) || JoinType.LEFT_OUTER_JOIN.equals(joinType))
						jr.add(lTmp, null);
					lTmp.clear();
				} else if (comp > 0) {
					if (JoinType.FULL_OUTER_JOIN.equals(joinType) || JoinType.RIGHT_OUTER_JOIN.equals(joinType))
						jr.add(null, rTmp);
					rTmp.clear();
				} else {
					jr.add(lTmp, rTmp);
					lTmp.clear();
					rTmp.clear();
				}
			}
		}
	}
}
