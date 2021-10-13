package com.eddedev.yamljava.emitter;

/**
 * @author Edvin Pettersson
 */
public enum NodeType {
	NONE, 
	PROPERTY, 
	SCALAR, 
	FLOW_SEQUENCE, 
	BLOCK_SEQUENCE, 
	FLOW_MAP, 
	BLOCK_MAP;
	
	public static NodeType get(final GroupType groupType, final FlowType flowType) {
		if (groupType == GroupType.SEQUENCE) {
			if (flowType == FlowType.FLOW)
				return FLOW_SEQUENCE;
			else if (flowType == FlowType.BLOCK)
				return BLOCK_SEQUENCE;
		} else if (groupType == GroupType.MAP) {
			if (flowType == FlowType.FLOW)
				return FLOW_MAP;
			else if (flowType == FlowType.BLOCK)
				return BLOCK_MAP;
		}
		
		return NodeType.NONE;
	}
	
	public static NodeType get(final GroupType groupType, final Event flowType) {
		if (groupType == GroupType.SEQUENCE) {
			if (flowType == Event.FLOW)
				return NodeType.FLOW_SEQUENCE;
			else if (flowType == Event.BLOCK)
				return NodeType.BLOCK_SEQUENCE;
		} else if (groupType == GroupType.MAP) {
			if (flowType == Event.FLOW)
				return NodeType.FLOW_MAP;
			else if (flowType == Event.BLOCK)
				return NodeType.BLOCK_MAP;
		}
		
		return NodeType.NONE;
	}
}
