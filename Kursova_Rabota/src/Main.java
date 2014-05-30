import java.util.Scanner;


public class Main {
	
	private static final int LOGIN = 1;
	private static final int SHOW_PROFILE = 4;
	private static final int SHOW_GRADES = 5;
	private static final int LOGOUT = 7;
	private static final int QUIT = 8;
	
	static void login() {
		
	}
	
	static void register() {
		
	}
	
	static boolean online;
	
	static int getMenuOptionlifeCycle() {
		System.out.println("1. Show Profile");
		System.out.println("2. show grades");
		System.out.println("3. logout");
		System.out.println("4. Quit");
		
		System.out.print("Enter your option: ");
		
		int option;
		Scanner in = new Scanner(System.in);
		do {
			option = in.nextInt();
			switch(option) {
			case 1: option = SHOW_PROFILE; break;
			case 2: option = SHOW_GRADES; break;
			case 3: option = LOGOUT; break;
			case 4: option = QUIT; break;
			
			}
		} while (option < SHOW_PROFILE || option > QUIT);
		
		return option;
	}
	
	private static boolean lifecyle(OptionsManager optionsManager) {
		int option;
		
		do {
			option = getMenuOptionlifeCycle();
			
			switch(option) {
			
			case SHOW_PROFILE: 
				optionsManager.show_profile();
				break;
			case SHOW_GRADES:
				optionsManager.showGrades();
				break;
			case LOGOUT:
				return false;
			
			}
			System.out.println();
			System.out.println();

		} while(option != QUIT);
		
		return true;
	}
	
	
	static int getMenuOption() {
		System.out.println("1. Login");
		System.out.println("2. Quit");
		
		System.out.print("Enter your option: ");
		
		int option;
		Scanner in = new Scanner(System.in);
		do {
			option = in.nextInt();
			if(option == 3) option = QUIT;
		} while (option != LOGIN && option != QUIT);
		
		return option;
	}
	
	public static void main(String[] args) {
		int option;
		OptionsManager optionsManager = new OptionsManager();
		
		do {
			option = getMenuOption();
			
			switch(option) {
			
			case LOGIN: 
				
				if(optionsManager.login())
					if(lifecyle(optionsManager) == true) { // if true is return then the QUIT option was selected
						option = QUIT;
					}
				break;
			
			}
			System.out.println();
			System.out.println();

		} while(option != QUIT);
		
		optionsManager.quit();
		
		System.out.println("GOODBYE :))");
	}

}
