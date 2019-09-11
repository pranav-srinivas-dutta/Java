import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class DementedNumbers {
	private int depth= 0;
	private ArrayList<Integer> failList= new ArrayList<> ();

	public boolean dementNumber (int sum) {

		if (sum != 1) {
			for (int f : failList) {
				if (f == sum) {
					return false;
				}
			}
			failList.add(sum);
		} else {
			return true;
		}

		if (depth > 9000)	{
			return false;
		} else {
			depth++;
		}
		
		int temp= sum;
		int result= 0;
		while (temp != 0) {
			int digit= temp % 10;		
			result+= (digit * digit);
			temp/= 10;
		}
		return this.dementNumber(result);
	}
	
	public static void main(String[] args) throws IOException {
		Scanner s= new Scanner(System.in);
		System.out.print("Enter 2 numbers: ");
		int l= Integer.parseInt(s.nextLine());
		int m= Integer.parseInt(s.nextLine());
		int counter= 0;
		for (int i= l; i <= m; i++) {
			//int n= Integer.parseInt(s.nextLine());
			DementedNumbers dn= new DementedNumbers ();
			if (dn.dementNumber(i)) {
				System.out.println(i);
				counter++;
			}
		}
		System.out.println("There are " + counter + " number of Demented Numbers in the interval " + l + ":" + m);
	}
}
