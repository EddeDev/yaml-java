package com.eddedev.yamljava.emitter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edvin Pettersson
 */
public final class GroupRegistry {
	private final List<Group> groups;
	
	public GroupRegistry() {
		groups = new ArrayList<Group>();
	}
	
	public void push(final Group group) {
		groups.add(group);
	}
	
	public void pop(final Group group) {
		groups.remove(group);
	}
	
	public Group get(final int index) {
		return groups.get(index);
	}
	
	public int size() {
		return groups.size();
	}
	
	public boolean empty() {
		return size() == 0;
	}
	
	public Group back() {
		return get(size() - 1);
	}
}
