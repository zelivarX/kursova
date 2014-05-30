package com.x.cmd;

import java.io.Serializable;


public enum CommandType implements Serializable {
	LOGIN,
	REGISTER,
	LOGOUT,
	GET_SUBJECTS,
	ADD_SUBJECT,
	SHOW_SUBJECTS,
	GET_GRADES,
	SHOW_PROFILE,
	SHOW_GRADES,
	QUIT
}
