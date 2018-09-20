package day2;

import day1.Calibrator;

/**
 * Created by @author xu on @date 2018�� �� �� ����11:46:46
 * 
 * 荷兰国旗问题
 */
public class FlagOfTheNetherlandsMethod {

	public static void main(String[] args) {
		Integer[] arr = new Integer[] { 2, 4, 34, 56, -13, 34, -5, -45, 0 };
		arr = partition(arr, 18);
		System.out.println(Calibrator.throughTheArray(arr));
	}

	/**
	 * ����
	 * 
	 * @param arr
	 *            ����
	 * @param num
	 *            �м�ֵ
	 * @return
	 */
	public static Integer[] partition(Integer[] arr, int num) {
		int less = -1;
		int more = arr.length;
		int current = 0;

		while (current < more) {
			if (arr[current] < num) {
				swap(arr, ++less, current++);
			} else if (arr[current] > num) {
				swap(arr, --more, current);
			} else {
				current++;
			}
		}
		return arr;
	}

	/**
	 * ������ֵ����
	 * 
	 * @param arr
	 *            ����
	 * @param i
	 * @param j
	 */
	private static void swap(Integer[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}