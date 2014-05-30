package com.x.cmd;

import java.io.Serializable;

public class Subject implements Serializable {

	public String subject;
	public int semester;
	
	public Subject(String subject, int semester) {
		this.subject = subject;
		this.semester = semester;
	}
	
	@Override
	public String toString() {
		return subject;
	}
	
	
}
