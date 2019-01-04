package eroica.network.utility.common;

import java.nio.ByteBuffer;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferFactory implements PooledObjectFactory<ByteBuffer> {
	public static final int BUFFER_SIZE = 1024 * 128;// the size of ByteBuffer

	@Override
	public PooledObject<ByteBuffer> makeObject() throws Exception {
		ByteBuffer bb = ByteBuffer.allocateDirect(BUFFER_SIZE);
		log.info(bb + " is created.");
		return new DefaultPooledObject<ByteBuffer>(bb);
	}

	@Override
	public void destroyObject(PooledObject<ByteBuffer> p) throws Exception {
		log.info(p.getObject() + " is destroyed.");
	}

	@Override
	public boolean validateObject(PooledObject<ByteBuffer> p) {
		return true;
	}

	@Override
	public void activateObject(PooledObject<ByteBuffer> p) throws Exception {
		log.info(p.getObject() + " is activated.");
		p.getObject().clear();
	}

	@Override
	public void passivateObject(PooledObject<ByteBuffer> p) throws Exception {
		log.info(p.getObject() + " is passivated.");
	}

}
