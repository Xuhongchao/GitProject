package day1;

/**
 * Created by @author xu on @date 2018年 月 日 下午11:38:50 <br/>
 * <p>
 * 对数器：通过大量数据来验证一个算法是否正确 <br/>
 * </p>
 * 
 * <pre>
 * 使用前期准备： 
 * 	(1)准备随机数发生器
 * 	(2)一个经过验证的肯定正确的算法，时间复杂度可以很差但是肯定正确
 * 使用步骤：
 * 	(1)通过随机数发生器来生成供测试使用的数据
 * 	(2)将生成的数据组进行copyArr
 * 	(3)使用自己定义的算法对数据进行操作
 * 	(4)使用绝对正确的算法对copy的数据进行操作
 * 	(5)对两组操作完成的数据进行比对，在几百万几千万的比对中都完全相同就说明算法没有问题
 * </pre>
 * 
 */
public class Calibrator {

	public static void main(String[] args) {

		for (int i = 0; i <= 10000000; i++) {
			Integer[] arr1 = generateRandomArray(5, 9);
			Integer[] arr2 = copyArr(arr1);
			Integer[] arr3 = copyArr(arr1);

			Integer[] resultA = BubblingSort.bubblingSort(arr1);
			// Integer[] resultB = InsertSort.insertSort(arr2);
			Integer[] resultC = SelectSort.selectSort(arr2);

			// boolean flag = equal(resultA, resultB);
			boolean flag = equal(resultA, resultC);
			if (!flag)
				System.out.println(throughTheArray(arr3));
		}

		System.out.println("检查完毕");
	}

	// ************判断两个数组是否相等 ***********
	private static boolean equal(Integer[] arr1, Integer[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (!arr1[i].equals(arr2[i])) {
				return false;
			}
		}
		return true;
	}

	// 随机数组发生器
	public static Integer[] generateRandomArray(int maxSize, int maxValue) {
		Integer[] arr = new Integer[(int) ((maxSize * Math.random()) + 1)];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue * Math.random()) + 1) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// copy数组
	public static Integer[] copyArr(Integer[] arr) {
		if (arr == null || arr.length < 1) {
			return new Integer[0];
		}
		Integer[] obj = new Integer[arr.length];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = arr[i];
		}
		return obj;
	}

	// 遍历数组输出
	public static String throughTheArray(Integer[] arr) {
		StringBuilder sb = new StringBuilder();
		if (arr == null || arr.length <= 0) {
			return null;
		}
		if (arr.length == 1) {
			return arr[0] + "";
		}
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i] + ",");
		}
		return sb.substring(0, sb.length() - 1);
	}
}
