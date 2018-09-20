package day1;

/**
 * Created by @author xu on @date 2018年 月 日 下午11:40:22 </br>
 * 选择排序 </br>
 */
public class SelectSort {

	public static void main(String[] args) {
		Integer[] arr = { 2222222, 1222222221, 43223, 311111, 623232232, -1117, -111111115, 109, -8 };
		Integer[] arr2 = selectSort(arr);
		for (Integer i : arr2) {
			System.out.print(i);
			System.out.print(',');
		}
	}

	public static Integer[] selectSort(Integer[] arr) {
		if (arr == null) {
			return null;
		}
		if (arr.length == 1) {
			return arr;
		}

		// 选择排序
		for (int i = 0; i < arr.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				minIndex = arr[j] < arr[minIndex] ? j : minIndex; // 交换下标
			}
			arr = swap(arr, i, minIndex); // 交换值
		}
		return arr;
	}

	private static Integer[] swap(Integer[] arr, int j, int minIndex) {
		int temp = arr[j];
		arr[j] = arr[minIndex];
		arr[minIndex] = temp;

		return arr;
	}
}