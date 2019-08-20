package eroica.util.concurrent;

/**
 * If you want to make some queues for mutually exclusive events whose exclusivity can be
 * defined by key objects(such as strings), you are suggested to try this
 * class.<br>
 * <code>private static final HashSynchronizerGroup SYNG = new HashSynchronizerGroup();</code><br>
 * ...<br>
 * <code>String key = ...;</code><br>
 * <code>synchronized (SYNG.getSyner(key)) {</code><br>
 * ...<br>
 * <code>}</code>
 * 
 * @author Minhua HUANG
 *
 */
public class HashSynchronizerGroup {
	private final static int DEFAULT_SIZE = 131;
	private final HashSynchronizer[] syners;
	private final int size;

	/**
	 * Create a HashSynchronizerGroup
	 * 
	 * @param size the queue count you want to make synchronized
	 */
	public HashSynchronizerGroup(int size) {
		this.size = size;
		syners = new HashSynchronizer[size];
		for (int i = 0; i < size; i++)
			syners[i] = new HashSynchronizer(String.valueOf(i));
	}

	/**
	 * Create a HashSynchronizerGroup of the default size 131.
	 */
	public HashSynchronizerGroup() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Get the corresponding synchronizer object of the key.
	 * 
	 * @param key the key controlling mutually exclusive events
	 * @return the object to be synchronized
	 */
	public HashSynchronizer getSyner(Object key) {
		return syners[hash(key)];
	}

	private int hash(Object key) {
		int i = key.hashCode() % size;
		return i >= 0 ? i : i + size;
	}

	/**
	 * The object of synchronization.
	 * 
	 * @author Minhua HUANG
	 *
	 */
	public class HashSynchronizer {
		private final String id;

		private HashSynchronizer(String id) {
			this.id = id;
		}

		String getId() {
			return id;
		}

		HashSynchronizerGroup getGroup() {
			return HashSynchronizerGroup.this;
		}
	}
}
