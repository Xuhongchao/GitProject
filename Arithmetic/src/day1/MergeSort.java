package day1;

/**
 * Created by @author xu on @date 2018年 月 日 下午10:56:44 <br/>
 * 
 * 归并排序，时间复杂度O(NlogN)
 */
public class MergeSort {

	public static void main(String[] args) {
		Integer[] arr = { 2, -101014, 0, -1, 9, 2, 2, 4, 45, 6, 8, 8, -10, 10202 };
		mergeSort(arr);
		System.out.println(Calibrator.throughTheArray(arr));
	}

	// 归并排序
	public static void mergeSort(Integer[] arr) {
		if (arr == null || arr.length < 2)
			return;
		mergeSort(arr, 0, arr.length - 1); // 执行正真的归并排序操作
	}

	/**
	 * 
	 * 具体的归并排序操作<br/>
	 * 
	 * <pre>
	 * 一、将数组分成左右两个部分，分别进行排序<br/>
	 * 二、每个部分做递归排序操作<br/>
	 * 三、将两个排好序的部分进行合并<br/>
	 * </pre>
	 * 
	 * @param arr
	 *            数组
	 * @param leftMargin
	 *            左排序边界
	 * @param rightMargin
	 *            右排序边界
	 */
	private static void mergeSort(Integer[] arr, int leftMargin, int rightMargin) {
		if (leftMargin == rightMargin)
			return;
		int mid = (leftMargin + rightMargin) / 2;
		mergeSort(arr, leftMargin, mid);
		mergeSort(arr, mid + 1, rightMargin);
		merge(arr, leftMargin, mid, rightMargin);
	}

	/**
	 * 合并操作
	 * 
	 * @param arr
	 *            数组
	 * @param left
	 *            左排序边界
	 * @param right
	 *            右排序边界
	 * @param mid
	 */
	private static void merge(Integer[] arr, int left, int mid, int right) {
		Integer[] temp = new Integer[(right - left) + 1];
		int i = 0;
		int p1 = left;
		int p2 = mid + 1;

		while (p1 <= mid && p2 <= right) {
			temp[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
		}

		while (p1 <= mid) {
			temp[i++] = arr[p1++];
		}

		while (p2 <= right) {
			temp[i++] = arr[p2++];
		}

		for (i = 0; i < temp.length; i++) {
			arr[left + i] = temp[i];
		}
	}

}
