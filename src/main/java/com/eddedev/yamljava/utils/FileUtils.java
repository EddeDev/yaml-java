package com.eddedev.yamljava.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.eddedev.yamljava.core.YAML;

/**
 * @author Edvin Pettersson
 */
public final class FileUtils {
	public static String read(final Path filepath) {
		String result = null;
		try {
			result = new String(Files.readAllBytes(filepath));
		} catch (final IOException e) {
			YAML.getAssertHandler().Assert(false, "Could not read from file: " + filepath);
		}
		return result;
	}
	
	public static void write(final Path filepath, final String string) {
		try {
			if (!Files.exists(filepath))
				Files.createFile(filepath);
			
			Files.write(filepath, string.getBytes());
		} catch (final IOException e) {
			YAML.getAssertHandler().Assert(false, "Could not write to file: " + filepath);
		}
	}
}
