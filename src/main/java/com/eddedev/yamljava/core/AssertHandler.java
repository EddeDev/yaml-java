package com.eddedev.yamljava.core;

/**
 * @author Edvin Pettersson
 */
public interface AssertHandler {
	default void Assert(final boolean condition) {
		Assert(condition, "Assertion failed");
	}

	default void Assert(final boolean condition, final String message) {
		if (!condition)
			Assert(message);
	}

	void Assert(final String message);
}