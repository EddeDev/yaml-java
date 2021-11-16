package com.eddedev.yamljava.emitter;

/**
 * @author Edvin Pettersson
 */
public final class Group {
	private GroupType type;
	private FlowType flowType;
	
	private int indent;
	private int childCount;
	
	public Group(final GroupType type) {
		this(type, FlowType.NONE);
	}
	
	public Group(final GroupType type, final FlowType flowType) {
		this.type = type;
		this.flowType = flowType;
	}
	
	public GroupType getType() {
		return type;
	}
	
	public void setType(final GroupType type) {
		this.type = type;
	}
	
	public FlowType getFlowType() {
		return flowType;
	}

	public void setFlowType(final FlowType flowType) {
		this.flowType = flowType;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(final int indent) {
		this.indent = indent;
	}

	public int getChildCount() {
		return childCount;
	}
	
	public void incrementChildCount() {
		childCount++;
	}
}
