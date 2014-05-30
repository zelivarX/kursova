package com.x.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Statement;

import com.x.cmd.Command;


public class StudentClient extends Thread {
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private DatabaseManager databaseManager;
	
	private volatile boolean RUNNING;
	
	public void shutDown() {
		RUNNING = false;
	}
	
	public StudentClient(Socket socket, Statement statement) {
		this.socket = socket;
		try {
			RUNNING = true;
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
			databaseManager = new DatabaseManager(out, statement);
			
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		
		outer : while(RUNNING) {
			try {
				Command command = (Command) in.readObject();
				
				switch(command.getCommandType()) {
				case LOGIN:
					databaseManager.login(command.getCommands());
					break;
				case GET_SUBJECTS:
					databaseManager.getSubjects();
					break;
				case ADD_SUBJECT:
					databaseManager.addSubject(command.getCommands());
					break;
				case SHOW_SUBJECTS:
					databaseManager.showSubjects();
					break;
				case SHOW_PROFILE:
					databaseManager.showUserProfile();
					break;
				case SHOW_GRADES:
					databaseManager.showGrades();
					break;
				case QUIT:
					break outer;
				}
				
			} catch (ClassNotFoundException | IOException e) {
				
			}
			
		}
		quit();
		
	}
	
	private void quit() {
		
			try {
				if(in != null) in.close();
				if(out != null) out.close();
				if(socket != null) socket.close();
				StudentsServer.removeClient(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
}
