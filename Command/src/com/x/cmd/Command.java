package com.x.cmd;

import java.io.Serializable;


public class Command implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5839544922916903169L;
	private CommandType type;
	private Object[] commands;
	
	public Command(CommandType type, Object...commands) {
		this.type = type;
		this.commands = commands;
	}
	
	public Object[] getCommands() {
		return commands;
	}
	
	public CommandType getCommandType() {
		return type;
	}
}

