package eroica.util.concurrent;

/**
 * If you want to make some queues for mutually exclusive events which can be
 * controlled by key objects(such as strings), you are suggested to try this
 * class instead of queue.
 * {@code private static final HashSynchronizerGroup SYN = new HashSynchronizerGroup();<br>
 * ...<br>
 * String key = ...;<br>
 * synchronized (SYNG.getSyner(key)) {<br>
 * ...<br> } }
 * 
 * @author Minhua HUANG
 *
 */
public class HashSynchronizerGroup implements SynchronizerGroup {
	private static int DEFAULT_SIZE = 128;
	private HashSynchronizer[] syners;
	private int size;

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
	 * Create a HashSynchronizerGroup of the default size 128.
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
	@Override
	public Synchornizer getSyner(Object key) {
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
	public class HashSynchronizer implements Synchornizer {
		private HashSynchronizer(String id) {
			this.id = id;
		}

		String id;

		public String getId() {
			return id;
		}

		@Override
		public SynchronizerGroup getGroup() {
			return HashSynchronizerGroup.this;
		}
	}
}
