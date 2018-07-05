package day1;

/**
 * Created by @author xu on @date 2018Äê ÔÂ ÈÕ ÏÂÎç10:40:02 </br>
 * Ã°ÅİÅÅĞò </br>
 */
public class BubblingSort {

	public static void main(String[] args) {
		Integer[] arr = { 2222222, 1222222221, 43223, 311111, 623232232, -1117, -111111115, 109, -8 };
		Integer[] arr2 = bubblingSort(arr);
		for (Integer i : arr2) {
			System.out.print(i);
			System.out.print(',');
		}
	}

	public static Integer[] bubblingSort(Integer[] arr) {
		if (arr == null) {
			return null;
		}
		if (arr.length == 1) {
			return arr;
		}

		// Ã°ÅİÅÅĞò
		for (int i = arr.length - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (arr[j] > arr[j + 1]) {
					arr = swap(arr, j, j + 1); // ½»»»Öµ
				}
			}
		}
		return arr;
	}

	private static Integer[] swap(Integer[] arr, int startIndex, int endIndex) {
		int temp = arr[startIndex];
		arr[startIndex] = arr[endIndex];
		arr[endIndex] = temp;

		return arr;
	}

}
