package com.x.server;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.x.cmd.Command;
import com.x.cmd.CommandType;

public class AdminManager {
	
	private Statement statement;
	
	public AdminManager(Statement statement) {
		this.statement = statement;
		
	}
	

	void register() {
		System.out.println("registration form: ");
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter username: ");
		String username = in.next();
		
		System.out.print("Enter pass: ");
		String pass = in.next();
		
		String confirmPass;
		do {
			System.out.print("Confirm pass: ");
			confirmPass = in.next();
		} while(!confirmPass.equals(pass));
		
		System.out.print("Enter faculty number: ");
		long faculty_number = in.nextLong();
		
		System.out.print("Enter EGN: ");
		long EGN = in.nextLong();
		
		System.out.print("Enter first name: ");
		String first_name = in.next();
		
		System.out.print("Enter last name: ");
		String last_name = in.next();
		
		
		System.out.print("Enter your sex: ");
		String sex = in.next();
		
		System.out.print("Enter your age: ");
		int age = in.nextInt();
		
		try {
			String values = String.format("%d,%d,'%s','%s','%s','%s', %d,'%s'", faculty_number, EGN, first_name, last_name, username, pass, age, sex);
			String query = String.format("insert into students (fakulteten_nomer,EGN,first_name,last_name,username,pass, age,sex) values (%s)", values);
			
			statement.executeUpdate(query);
			
			System.out.println("successfully registered !");
		} catch(Exception e) {
			System.out.println("error registering");
			e.printStackTrace();
		}
		
	}
	
	void addSubject() {
		Scanner in = new Scanner(System.in);
		
		System.out.print("Subject name: ");
		String subjectName = in.nextLine();
		
		System.out.print("Subject semester: ");
		int semester = in.nextInt();
		
		try {
			statement.executeUpdate(String.format("INSERT INTO subjects (subject_name, semester) values('%s', %d)", subjectName, semester));
			System.out.println("done");
		} catch (SQLException e) {
			System.out.println("subject aready exists");
			e.printStackTrace();
		}
	}
	
	void addGradeToStudent() {
		Scanner in = new Scanner(System.in);
		
		System.out.println("Adding grade..");
		
		System.out.print("Student's faculty number: ");
		long faculty_number = in.nextLong();
		
		System.out.print("Subject name: ");
		String subjectName = in.next();
		
		System.out.print("Enter the grade: ");
		int grade;
		
		do {
			grade = in.nextInt();
		} while (grade < 2 || grade > 6);
		
		
		try {
			statement.executeUpdate(
					String.format("insert into grades values(%d, %d, (select subject_id from subjects WHERE subject_name = '%s'))", grade, faculty_number, subjectName));
			System.out.println("done");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
