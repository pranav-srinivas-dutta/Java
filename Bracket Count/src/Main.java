import prepare.test.list.Stack;
import java.util.Scanner;

public class Main {
	public static void main (String... args) throws Exception {
		Scanner scanner= new Scanner(System.in);
		String brackets= scanner.nextLine();
		Stack stack= new Stack(20);
		
		for (int i= 0; i < brackets.length(); i++) {
			stack.push(brackets.charAt(i) + "");
		}
		
		int counter= 0;
		boolean isError= false;
		for (int i= 0; i < brackets.length(); i++) {
			String top= stack.pop();
			
			if (")".equals(top)) {
				counter++;
			} else if ("(".equals(top)) {
				counter--;
			}
			
			if (counter < 0) {
				isError= true;
				break;
			}
		}
		
		if (isError || (counter != 0)) {
			System.out.println("Error in Expression.");
		} else {
			System.out.println("Correct Expression");
		}
	}
}
