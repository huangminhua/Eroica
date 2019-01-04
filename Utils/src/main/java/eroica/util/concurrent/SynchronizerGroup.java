package eroica.util.concurrent;

/**
 * 
 * @author Minhua HUANG
 *
 */
public interface SynchronizerGroup {
	public Synchornizer getSyner(Object key);

	public interface Synchornizer {
		public String getId();
		public SynchronizerGroup getGroup();
	}
}
