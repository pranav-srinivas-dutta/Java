public class BubbleSort {
	public static void main(String... args) {
		int[] arr= {10,15,64,32,25,12,9,4,3,25,67,10000,-9};
		
		//BubbleSort
		for (int i= 0; i < arr.length;i++) {
			for (int j= i; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					int temp= arr[i];
					arr[i]= arr[j];
					arr[j]= temp;
				}
			}
		}
		
		for (int i= 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}
}
