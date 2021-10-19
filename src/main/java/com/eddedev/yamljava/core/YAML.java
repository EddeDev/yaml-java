package com.eddedev.yamljava.core;

/**
 * @author Edvin Pettersson
 */
public final class YAML {
	private static final AssertHandler DEFAULT_ASSERT_HANDLER = System.err::println;
	
	private static AssertHandler assertHandler = DEFAULT_ASSERT_HANDLER;
	
	public static void setDefaultAssertHandler() {
		assertHandler = DEFAULT_ASSERT_HANDLER;
	}
	
	public static void setAssertHandler(final AssertHandler handler) {
		assertHandler = handler;
	}
	
	public static AssertHandler getAssertHandler() {
		return assertHandler;
	}
}
