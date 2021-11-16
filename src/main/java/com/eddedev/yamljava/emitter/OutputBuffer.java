package com.eddedev.yamljava.emitter;

/**
 * @author Edvin Pettersson
 */
public final class OutputBuffer {
	private static final int INITIAL_BUFFER_SIZE = 1024 * 16;
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
			
	private final StringBuffer buffer;
	
	private int position;
	private int row;
	private int column;
	
	public OutputBuffer() {
		buffer = new StringBuffer(INITIAL_BUFFER_SIZE);
	}
	
	public void write(final char ch) {
		buffer.append(ch);
		
		position++;
		column++;
		if (ch == '\n') {
			row++;
			column = 0;
		}
	}
	
	public void write(final String string) {
		buffer.append(string);
		
		char[] chars = string.toCharArray();
		for (char ch : chars) {
			position++;
			column++;
			if (ch == '\n') {
				row++;
				column = 0;
			}
		}
	}
	
	public void writeNewLine() {
		write(LINE_SEPARATOR);
	}
	
	public void writeIndent(final int indent) {
		while (column < indent)
			write(' ');
	}

	public String string() {
		return buffer.toString();
	}
	
	public int getPosition() {
		return position;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
