package prepare.test.list;

import java.util.Vector;

public class Stack {
	private int top;
	private Vector<String> list;
	
	public Stack(int initialSize) {
		this.list= new Vector<> (initialSize);
		this.top= -1;
	}
	
	public void push(String item) throws Exception {
		if (item == null)
			throw new Exception("null Items cannot be inserted into the Stack.");
		this.list.add(item);
		this.top++;
	}
	
	public String viewTop() {
		return (top == -1)?"":this.list.get(this.top);
	}
	
	public String pop() {
		if (top == -1) {
			System.out.println("Stack is Empty. Cannot Pop anymore.");
		} else {
			String item= this.list.get(this.top);
			this.list.remove(this.top--);
			return item;
		}
		return null;
	}
	
	public String toString() {
		this.list.forEach(System.out::println);
		return "";
	}
	
	public static void main(String... args) throws Exception {
		Stack stk= new Stack(20);
		stk.push("10");
		stk.push("20");
		stk.push("30");
		System.out.println(stk.pop());
		stk.push("40");
		stk.push("50");
		stk.push("60");
		System.out.println(stk.pop());
		stk.push("70");
		stk.push("80");
		System.out.println(stk.pop());
		stk.push("90");
		System.out.println(stk.pop());
		stk.push("100");
		
		System.out.println(stk.list);
	}
}
