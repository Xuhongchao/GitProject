package day1;

/**
 * Created by @author xu on @date 2018年 月 日 下午10:39:16 二分查找
 */
public class BinarySearch {

	public static void main(String[] args) {
		int[] arr = { 2, 5, 6, 8, 9 };
		System.out.println(binarySearch(arr, 9, 0, arr.length - 1));
	}

	/*
	 * 二分查找
	 */
	private static Integer binarySearch(int[] arr, int key, int startIndex, int endIndex) {
		if (startIndex > endIndex || arr.length <= 0) {
			return null;
		}
		int middle = (startIndex + endIndex) / 2;
		if (arr[middle] == key) {
			return middle;
		} else if (key < arr[middle]) {
			return binarySearch(arr, key, startIndex, middle - 1);
		} else if (key > arr[middle]) {
			return binarySearch(arr, key, middle + 1, endIndex);
		} else {
			return null;
		}
	}

}