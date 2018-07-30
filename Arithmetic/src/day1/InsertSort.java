package day1;

/**
 * Created by @author xu on @date 2018�� �� �� ����11:01:28 </br>
 * ��������
 */
public class InsertSort {

	public static void main(String[] args) {
		Integer[] arr = { 3, 1, 4, 2, -1, 0 };
		Integer[] arr2 = insertSort(arr);
		for (Integer i : arr2) {
			System.out.print(i);
			System.out.print(',');
		}
	}

	// ��������
	public static Integer[] insertSort(Integer[] arr) {
		if (arr == null || arr.length < 1) {
			return null;
		}
		if (arr.length == 1) {
			return arr;
		}

		// ����
		for (int i = 1; i <= arr.length - 1; i++) {
			for (int j = i - 1; j >= 0 && arr[j + 1] < arr[j]; j--) {
				arr = swap(arr, j, j + 1);
			}
		}
		return arr;
	}

	// ����
	public static Integer[] swap(Integer[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;

		return arr;
	}

}
