package day1;

/**
 * Created by @author xu on @date 2018年 月 日 下午11:01:28 </br>
 * 插入排序
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

	// 插入排序
	public static Integer[] insertSort(Integer[] arr) {
		if (arr == null || arr.length < 1) {
			return null;
		}
		if (arr.length == 1) {
			return arr;
		}

		// 排序
		for (int i = 1; i <= arr.length - 1; i++) {
			for (int j = i - 1; j >= 0 && arr[j + 1] < arr[j]; j--) {
				arr = swap(arr, j, j + 1);
			}
		}
		return arr;
	}

	// 交换
	public static Integer[] swap(Integer[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;

		return arr;
	}

}
