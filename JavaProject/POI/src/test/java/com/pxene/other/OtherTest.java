package com.pxene.other;

/**
 * Created by @author xuhongchao on @date 2017�� 11�� 14�� ����5:43:27
 */

public class OtherTest {
	public static void main(String[] args) {
		int[] arr = { 3, 5, 2, 4 };
		int res = partition(arr, 0, arr.length - 1);
		System.out.println(res);
	}

	public static int partition(int[] arr, int low, int high) {
		int key = arr[low];
		while (low < high) {
			while (low < high && arr[high] >= key) {
				high--;
			}
			arr[low] = arr[high];
			while (low < high && arr[low] <= key) {
				low++;
			}
			arr[high] = arr[low];
		}
		arr[low] = key;
		return low;
	}

}
