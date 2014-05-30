import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.x.cmd.Command;
import com.x.cmd.CommandType;
import com.x.cmd.Subject;


public class OptionsManager {
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public OptionsManager() {
		try {
			socket = new Socket("localhost", 1234);
		} catch (UnknownHostException e) {
			System.out.println("unnable to find server");
		} catch (IOException e) {
			System.out.println("unnable to connet to server");
			System.exit(1);
		}
		
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			
		} catch (IOException e) {
			System.exit(1);
		}
	}
	
	public boolean login() {
		String username, pass;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter username: ");
		username = in.next();
		System.out.print("Enter pass: ");
		pass = in.next();
		try {
			out.writeObject(new Command(CommandType.LOGIN, username, pass));
			out.flush();
			Command cmd = (Command) this.in.readObject();
			if(cmd.getCommandType() == CommandType.LOGIN) {
				if( (boolean) cmd.getCommands()[0] == true) {
					System.out.println("successfully loged in");
					return true;
				}
			} 
			System.out.println("wrong username or password");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void showGrades() {
		try {
			out.writeObject(new Command(CommandType.SHOW_GRADES));
			out.flush();
			Command cmd = (Command) this.in.readObject();
			if(cmd.getCommandType() == CommandType.SHOW_GRADES) {
				Map <Subject, Integer> subjects = (LinkedHashMap <Subject, Integer>) cmd.getCommands()[0];
				if(subjects != null) {
					System.out.println("successfully loged in");
					int lastSemester = -1;
					for(Entry<Subject, Integer> i : subjects.entrySet()) {
						if(lastSemester < i.getKey().semester) {
							System.out.printf("\tsemester %d", i.getKey().semester);
							System.out.println();
							lastSemester = i.getKey().semester;
						}
						System.out.println(i);
					}
				}
				else System.out.println("null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void menu(Object[] subjects) {
		
		Scanner in = new Scanner(System.in);
		List<Integer> options = new ArrayList<Integer>();
		
		for(Object i : subjects) {
			Object[] subject = (Object[]) i;
			options.add((int) subject[0]);
			System.out.println((int) subject[0] + ". " + (String) subject[1]);
		}
		
		int choise;
		do {
			System.out.println("Enter your choice: ");
			choise = in.nextInt();
		} while(!options.contains(choise));
		
		
		try {
			Command cmd = new Command(CommandType.ADD_SUBJECT, choise);
			out.writeObject(cmd);
			out.flush();
			
			cmd = (Command) this.in.readObject();
			if(cmd.getCommandType() == CommandType.ADD_SUBJECT) {
				if( (boolean) cmd.getCommands()[0] == true) {
					System.out.println("successfully added subject");
				} else System.out.println("NE");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showSubjects() {
		try {
			Command cmd = new Command(CommandType.SHOW_SUBJECTS);
			out.writeObject(cmd);
			out.flush();
			
			cmd = (Command) this.in.readObject();
			if(cmd.getCommandType() == CommandType.SHOW_SUBJECTS) {
				System.out.print("attending subjects: ");
				if( cmd.getCommands().length != 0) {
					Object[] subjects = cmd.getCommands();
					for(Object subject : subjects) {
						
						System.out.print((String) subject + ", ");
					}
				} else System.out.println("-");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void show_profile() {
		
		try {
			Command cmd = new Command(CommandType.SHOW_PROFILE);
			out.writeObject(cmd);
			out.flush();
			
			cmd = (Command) this.in.readObject();
			if(cmd.getCommandType() == CommandType.SHOW_PROFILE) {
				if( (boolean) cmd.getCommands()[0] == true) {
					showUser(cmd.getCommands());
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void showUser(Object[] user) {
		String first_name = (String) user[1];
		String last_name = (String) user[2];
		String username = (String) user[3];
		int semester = (int) user[4];
		String sex = (String) user[5];
		int age = (int) user[6];
		long faculty_number = (long) user[7];
		
		System.out.println();
		System.out.println("MY PROFILE");
		System.out.println();
		System.out.println("username: " + username);
		System.out.println("names: " + first_name + " " + last_name);
		System.out.println("faculty number: " + faculty_number);
		System.out.println("sex: " + (sex != null ? sex : ""));
		System.out.println("current semester: " + semester);
		System.out.println("age: " + age);
	}
	
	
	public void quit() {
		try {
			out.writeObject(new Command(CommandType.QUIT));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
