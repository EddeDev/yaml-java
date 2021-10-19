package com.eddedev.yamljava.core;

public final class YAML {
	private static final AssertHandler DEFAULT_ASSERT_HANDLER = message -> System.err.println(message);
	
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
