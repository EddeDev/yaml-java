package com.eddedev.yamljava.parser;

import java.util.Iterator;

/**
 * @author Edvin Pettersson
 */
public final class Node implements Iterable<Node> {
	public Node get(final String key) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public <T> T as(final Class<T> cls) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	public Iterator<Node> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
