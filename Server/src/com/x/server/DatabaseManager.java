package com.x.server;

import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.x.cmd.Command;
import com.x.cmd.CommandType;
import com.x.cmd.Subject;


public class DatabaseManager {
	
	private ObjectOutputStream out;
	private Statement statement;
	private long my_faculty_number;
	
	public DatabaseManager(ObjectOutputStream out, Statement statement) {
		this.out = out;
		this.statement = statement;
	}
	
	public void showGrades() {
		try {
			String query =
				String.format("select avg(g.grade), s.semester, s.subject_name from grades g JOIN subjects s ON g.student_fakulteten_nomer = %d AND g.subject_id = s.subject_id JOIN students st ON st.fakulteten_nomer = g.student_fakulteten_nomer AND st.semester <= s.semester  GROUP BY s.semester ORDER BY s.semester asc", 
						this.my_faculty_number
						
					);
					
			ResultSet rs = statement.executeQuery(query);
			
			Map<Subject, Integer> subjects = new LinkedHashMap<Subject, Integer>();
			
			
			while(rs.next()) {
				String subject = rs.getString(3);
				int grade = rs.getInt(1);
				int semester = rs.getInt(2);
				
				Subject sb = new Subject(subject, semester);
				
				subjects.put(sb, grade);
				
			}
			
			out.writeObject(new Command(CommandType.SHOW_GRADES, subjects));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void login(Object[] commands) {
		String username = (String) commands[0];
		String pass = (String) commands[1];
		
		try {
			String query = "select fakulteten_nomer from students where username = '" + username + "' and pass = '" + pass + "'";
			ResultSet rs = statement.executeQuery(query);
			
			boolean loged = false;
			
			if(rs.next()) {
				my_faculty_number = rs.getInt(1);
				loged = true;
			}
			
			out.writeObject(new Command(CommandType.LOGIN, loged)); // sends true if the user is loged in
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	/*
	public void register(Object[] commands) {
		
		String username = (String) commands[0];
		String pass = (String) commands[1];
		long faculty_number = (long) commands[2];
		long EGN = (long) commands[3];
		String first_name = (String) commands[4];
		String last_name = (String) commands[5];
		int semester = (int) commands[6];
		String sex = (String) commands[7];
		int age = (int) commands[8];
		
		try {
			String values = String.format("%d,%d,'%s','%s','%s','%s',%d,%d,'%s'", faculty_number, EGN, first_name, last_name, username, pass, semester, age, sex);
			String query = String.format("insert into students (fakulteten_nomer,EGN,first_name,last_name,username,pass,semester,age,sex) values (%s)", values);
			//ResultSet rs = statement.executeQuery(query);
			boolean registered = statement.executeUpdate(query) != 0 ? true : false;
			
			out.writeObject(new Command(CommandType.REGISTER, registered)); // sends true if the user has registered successfully
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public void addSubject(Object[] commands) {
		int subject_id = (int) commands[0];
		
		try {
			
			String query = String.format("insert into student_attends values(%d, %d)", my_faculty_number, subject_id);
			
			int result = statement.executeUpdate(query);
			
			Command cmd = new Command(CommandType.ADD_SUBJECT, result > 0 ? true : false);				
			out.writeObject(cmd);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showUserProfile() {
		
		try {
			
			String query = String.format("select first_name, last_name, username, semester, sex, age from students where fakulteten_nomer = %d", this.my_faculty_number);
			ResultSet rs = statement.executeQuery(query);
			
			boolean userExists = false;
			
			
			
			
			if(rs.next()) {
				userExists = true;
				
				String first_name = rs.getString(1);
				String last_name = rs.getString(2);
				String username = rs.getString(3);
				int semester = rs.getInt(4);
				String sex = rs.getString(5);
				int age = rs.getInt(6);
				
				Command cmd = new Command(CommandType.SHOW_PROFILE, userExists, 
						first_name,
						last_name,
						username,
						semester,
						sex,
						age,
						this.my_faculty_number
					);				
				out.writeObject(cmd);
			} else {
				out.writeObject(new Command(CommandType.SHOW_PROFILE, userExists)); // sends false if the user doesn't exist in the database
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public void showSubjects() {
		
		try {
			
			String query = String.format("select subject_name from subjects where "
					+ "subject_id in (select subject_id from student_attends where student_fakulteten_nomer = %d)", my_faculty_number);
				ResultSet rs = statement.executeQuery(query);
			
			List<Object> list = new ArrayList<Object>();
			
			while(rs.next()) {
				String subject_name = rs.getString(1);
				list.add(subject_name);
			}
			
			if(list.size() != 0) {
				Command cmd = new Command(CommandType.SHOW_SUBJECTS, list.toArray());				
				out.writeObject(cmd);
			} else {
				out.writeObject(new Command(CommandType.SHOW_SUBJECTS));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void getSubjects() {
		
		
		try {
			
			String query = String.format("select subject_id, subject_name from subjects where "
					+ "subject_id not in (select subject_id from student_attends where student_fakulteten_nomer = %d)", my_faculty_number);
			ResultSet rs = statement.executeQuery(query);
			
			List<Object> list = new ArrayList<Object>();
			
			while(rs.next()) {
				int subject_id = rs.getInt(1);
				String subject_name = rs.getString(2);
				list.add(new Object[] {subject_id, subject_name});
			}
			
			if(list.size() != 0) {
				Command cmd = new Command(CommandType.GET_SUBJECTS, list.toArray());				
				out.writeObject(cmd);
			} else {
				out.writeObject(new Command(CommandType.GET_SUBJECTS));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
