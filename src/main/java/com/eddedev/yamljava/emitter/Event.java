package com.eddedev.yamljava.emitter;

/**
 * @author Edvin Pettersson
 */
public enum Event {
	BEGIN_SEQUENCE,
	END_SEQUENCE,
	
	BEGIN_MAP,
	END_MAP,
	
	KEY,
	VALUE,
	
	FLOW,
	BLOCK
}
