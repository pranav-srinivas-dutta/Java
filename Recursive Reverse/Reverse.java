public class Reverse {
	public void reverse(String s, int i) {
		if ((s.length() - i) == 1) {
			System.out.println(s.charAt(i));	
			return;
		} else {
			reverse(s, ++i);	
			System.out.println(s.charAt(--i));	
			return;
		}
	}
	
	public static void main (String ... args) {
		Reverse r= new Reverse();
		r.reverse("ABISHEK", 0);
	}
}
