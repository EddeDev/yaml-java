package com.eddedev.yamljava.emitter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eddedev.yamljava.utils.FileUtils;

/**
 * @author Edvin Pettersson
 */
public final class Emitter {
	private final GroupRegistry groupRegistry;
	private final OutputBuffer buffer;
	
	private final Event mapFormat;
	private final Event sequenceFormat;
	
	private final int indent;
	private int currentIndent;
	private int documentCount;
	
	public Emitter() {
		groupRegistry = new GroupRegistry();
		buffer = new OutputBuffer();
		
		mapFormat = Event.BLOCK;
		sequenceFormat = Event.BLOCK;
		
		indent = 2;	
	}

	public Emitter write(final String value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(value);
		onScalarBegin();
		return this;
	}

	public Emitter write(final boolean value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}

	public Emitter write(final char value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(value);
		onScalarBegin();
		return this;
	}

	public Emitter write(final int value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}

	public Emitter write(final short value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}
	
	public Emitter write(final long value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}

	public Emitter write(final float value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}
	
	public Emitter write(final double value) {
		prepareNode(NodeType.SCALAR);
		buffer.write(String.valueOf(value));
		onScalarBegin();
		return this;
	}
	
	public Emitter write(final Object value) {
		if (value == null) {
			prepareNode(NodeType.SCALAR);
			buffer.write('~');
			onScalarBegin();
			return this;
		}
		
		if (value instanceof Enum) {
			write(((Enum<?>) value).name());
			return this;
		}
		
		write(value.toString());
		return this;
	}
	
	private void onScalarBegin() {
		onNodeBegin();
	}

	private void onGroupBegin(final GroupType type) {
		onNodeBegin();
		
		final int lastGroupIndent = getCurrentGroupIndent();
		currentIndent += lastGroupIndent;

		final Group group = new Group(type);
		
		if (getFlowType(type) == Event.BLOCK)
			group.setFlowType(FlowType.BLOCK);
		else if (getFlowType(type) == Event.FLOW)
			group.setFlowType(FlowType.FLOW);
		group.setIndent(indent);
		
		groupRegistry.push(group);
	}

	private void onGroupEnd(final GroupType type) {
		final Group finishedGroup = groupRegistry.back();
		groupRegistry.pop(finishedGroup);
		
		final int lastIndent = getCurrentGroupIndent();
		currentIndent -= lastIndent;		
	}

	private void onNodeBegin() {
		if (groupRegistry.empty())
			documentCount++;
		else
			groupRegistry.back().incrementChildCount();
	}

	private void forceFlow() {
		if (!groupRegistry.empty())
			groupRegistry.back().setFlowType(FlowType.FLOW);
	}
	
	private NodeType getCurrentGroupNodeType() {
		if (groupRegistry.empty())
			return NodeType.NONE;
		
		Group group = groupRegistry.back();
		return NodeType.get(group.getType(), group.getFlowType());
	}

	private FlowType getCurrentGroupFlowType() {
		return groupRegistry.empty() ? FlowType.NONE : groupRegistry.back().getFlowType();
	}

	private int getCurrentGroupIndent() {
		return groupRegistry.empty() ? 0 : groupRegistry.back().getIndent();
	}

	private int getCurrentGroupChildCount() {
		return groupRegistry.empty() ? documentCount : groupRegistry.back().getChildCount();
	}

	private Event getFlowType(final GroupType groupType) {
		return getCurrentGroupFlowType() == FlowType.FLOW ? Event.FLOW : (groupType == GroupType.SEQUENCE ? sequenceFormat : mapFormat);
	}

	private NodeType getNextGroupType(final GroupType type) {
		return NodeType.get(type, getFlowType(type));
	}
	
	private int getLastIndent() {
		return groupRegistry.empty() ? 0 : currentIndent - groupRegistry.get(groupRegistry.size() - 2).getIndent();
	}
	
	public Emitter write(final Event value) {
		switch (value) {
		case BEGIN_SEQUENCE:
			emitBeginSequence();
			break;
		case END_SEQUENCE:
			emitEndSequence();
			break;
		case BEGIN_MAP:
			emitBeginMap();
			break;
		case END_MAP:
			emitEndMap();
			break;
		default:
			break;
		}
		
		return this;
	}
	
	private void emitBeginSequence() {
		prepareNode(getNextGroupType(GroupType.SEQUENCE));
		onGroupBegin(GroupType.SEQUENCE);
	}
	
	private void emitEndSequence() {
		if (getCurrentGroupChildCount() == 0)
			forceFlow();
		
		if (getCurrentGroupFlowType() == FlowType.FLOW) {
			buffer.writeIndent(currentIndent);
			
			if (getCurrentGroupChildCount() == 0)
				buffer.write('[');
			buffer.write(']');
		}
		
		onGroupEnd(GroupType.SEQUENCE);
	}
	
	private void emitBeginMap() {
		prepareNode(getNextGroupType(GroupType.MAP));
		onGroupBegin(GroupType.MAP);
	}
	
	private void emitEndMap() {
		if (getCurrentGroupChildCount() == 0)
			forceFlow();
		
		if (getCurrentGroupFlowType() == FlowType.FLOW) {
			buffer.writeIndent(currentIndent);
			
			if (getCurrentGroupChildCount() == 0)
				buffer.write('{');
			buffer.write('}');
		}
		
		onGroupEnd(GroupType.MAP);
	}
	
	private void prepareNode(final NodeType childType) {
		switch (getCurrentGroupNodeType()) {
		case NONE:
			break;
		case FLOW_SEQUENCE:
			prepareFlowSequenceNode(childType);
			break;
		case BLOCK_SEQUENCE:
			prepareBlockSequenceNode(childType);
			break;
		case FLOW_MAP:
			prepareFlowMapNode(childType);
			break;
		case BLOCK_MAP:
			prepareBlockMapNode(childType);
			break;
		default:
			break;
		}
	}
	
	private void prepareFlowSequenceNode(final NodeType childType) {
		buffer.writeIndent(getLastIndent());
		
		if (getCurrentGroupChildCount() == 0)
			buffer.write('[');
		else
			buffer.write(',');
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			if (buffer.getColumn() > 0 && getCurrentGroupChildCount() > 0)
				buffer.write(' ');
			buffer.writeIndent(getLastIndent());
			break;
		case BLOCK_SEQUENCE:
		case BLOCK_MAP:
			throw new AssertionError();
		default:
			break;
		}
	}
	
	private void prepareFlowMapNode(final NodeType childType) {
		if (getCurrentGroupChildCount() % 2 == 0)
			prepareKeyFlowMap(childType);
		else
			prepareKeyValueFlowMap(childType);
	}
	
	private void prepareKeyFlowMap(final NodeType childType) {
		if (childType == NodeType.NONE)
			return;
		
		if (getCurrentGroupChildCount() > 0)
			buffer.writeNewLine();
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			buffer.writeIndent(currentIndent);
			break;
		case BLOCK_SEQUENCE:
		case BLOCK_MAP:
			break;
		default:
			break;
		}
	}
	
	private void prepareKeyValueFlowMap(final NodeType childType) {
		if (childType == NodeType.NONE)
			return;
		
		buffer.write(':');
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			if (buffer.getColumn() > 0)
				buffer.write(' ');
			buffer.writeIndent(currentIndent + getCurrentGroupIndent());
			break;
		case BLOCK_SEQUENCE:
		case BLOCK_MAP:
			buffer.writeNewLine();
			break;
		default:
			break;
		}
	}
	
	private void prepareBlockSequenceNode(final NodeType childType) {
		if (childType == NodeType.NONE)
			return;
		
		if (getCurrentGroupChildCount() > 0)
			buffer.writeNewLine();
		buffer.writeIndent(currentIndent);
		buffer.write('-');
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			buffer.writeIndent(currentIndent + getCurrentGroupIndent());
			break;
		case BLOCK_SEQUENCE:
			buffer.writeNewLine();
			break;
		default:
			break;
		}
	}
	
	private void prepareBlockMapNode(final NodeType childType) {
		if (getCurrentGroupChildCount() % 2 == 0)
			prepareKeyBlockMap(childType);
		else
			prepareKeyValueBlockMap(childType);
	}
	
	private void prepareKeyBlockMap(final NodeType childType) {
		if (childType == NodeType.NONE)
			return;
		
		if (getCurrentGroupChildCount() > 0)
			buffer.writeNewLine();
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			buffer.writeIndent(currentIndent);
			break;
		default:
			break;
		}
	}
	
	private void prepareKeyValueBlockMap(final NodeType childType) {
		if (childType == NodeType.NONE)
			return;
		
		buffer.write(':');
		
		switch (childType) {
		case NONE:
			break;
		case PROPERTY:
		case SCALAR:
		case FLOW_SEQUENCE:
		case FLOW_MAP:
			if (buffer.getColumn() > 0)
				buffer.write(' ');
			buffer.writeIndent(currentIndent + getCurrentGroupIndent());
			break;
		case BLOCK_SEQUENCE:
		case BLOCK_MAP:
			buffer.writeNewLine();
			break;
		default:
			break;
		}
	}

	public String string() {
		return buffer.string();
	}
	
	public void writeToFile(final String filepath) {
		FileUtils.write(Paths.get(filepath), string());
	}
	
	public void writeToFile(final Path filepath) {
		FileUtils.write(filepath, string());
	}
	
	public void writeToFile(final File file) {
		FileUtils.write(file.toPath(), string());
	}
}
