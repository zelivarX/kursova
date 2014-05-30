package com.x.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StudentsServer {
	
	private static ServerSocket server;
	private static Connection databaseConnection;
	
	private static void SetupServer() {
		try {
			server = new ServerSocket(1234);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static final String DATABASE_USERNAME = "root";
	public static final String DATABASE_PASSWORD = "velizar1";
	public static final String DATABASE_NAME = "kursova";
	
	private static volatile boolean running;
	
	public static boolean isRunning() {
		return running;
	}
	
	private static volatile AdminManager admin;
	
	private static void SetupDatabase() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DATABASE_NAME, DATABASE_USERNAME, DATABASE_PASSWORD);
			System.out.println("successfully connected to " + DATABASE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Statement st = databaseConnection.createStatement();
			st.executeUpdate("insert into school_subjects values(1, 'Matematika')");
			st.executeUpdate("insert into school_subjects values(2, 'Fizika')");
			st.executeUpdate("insert into school_subjects values(3, 'Pluvane')");
			st.executeUpdate("insert into school_subjects values(4, 'Informatika')");
		} catch (Exception e) {
		}
	}
	
	private static List<StudentClient> clients = new ArrayList<StudentClient>();
	
	public static synchronized void removeClient(StudentClient client) {
		clients.remove(client);
	}
	
	
	
	private static void runOnBg () {
		System.out.println("waiting for connections..");
		while(isRunning()) {
			System.out.println();
			try {
				Socket socket = server.accept();
				System.out.println("a client cpnnected");
				StudentClient studentClient = new StudentClient(socket, databaseConnection.createStatement());
				clients.add(studentClient);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(StudentClient i : clients) {
			i.shutDown();
			try {
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		SetupServer();
		SetupDatabase();
		
		try {
			admin = new AdminManager(databaseConnection.createStatement());
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		running = true;
		
		Thread serverT = new Thread () {
			public void run() {
				runOnBg();
				
			}
		};
		serverT.start();
		
		adminMenu();
		
		System.out.println("END");
		try {
			serverT.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Server turned off.");
	}
	
	private static final int add_grade = 1, add_subject = 2, add_student = 3, turn_off_server = 4;
	
	private static void adminMenu() {
		
		int choise;
		
		do {
			choise = adminAnswer();
			
			switch(choise) {
			case add_grade:
				admin.addGradeToStudent();
				break;
			case add_subject:
				admin.addSubject();
				break;
			case add_student:
				admin.register();
				break;
			}
			
			
		} while(choise != turn_off_server);
		
		running = false;
	}
	
	private static int adminAnswer() {
		
		
		int option;
		
		Scanner in = new Scanner(System.in);
		
		do {
			
			System.out.println("1. Add grade to student.");
			System.out.println("2. Add subject.");
			System.out.println("3. add new student.");
			System.out.println("4. shut down server.");
			System.out.print("your choise: ");
			
			option = in.nextInt();
			
		} while (option < add_grade || option > turn_off_server);
		
		return option;
	}

}
