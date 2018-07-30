package day1;

/**
 * Created by @author xu on @date 2018�� �� �� ����11:38:50 <br/>
 * <p>
 * ��������ͨ��������������֤һ���㷨�Ƿ���ȷ <br/>
 * </p>
 * 
 * <pre>
 * ʹ��ǰ��׼���� 
 * 	(1)׼�������������
 * 	(2)һ��������֤�Ŀ϶���ȷ���㷨��ʱ�临�Ӷȿ��Ժܲ�ǿ϶���ȷ
 * ʹ�ò��裺
 * 	(1)ͨ������������������ɹ�����ʹ�õ�����
 * 	(2)�����ɵ����������copyArr
 * 	(3)ʹ���Լ�������㷨�����ݽ��в���
 * 	(4)ʹ�þ�����ȷ���㷨��copy�����ݽ��в���
 * 	(5)�����������ɵ����ݽ��бȶԣ��ڼ�����ǧ��ıȶ��ж���ȫ��ͬ��˵���㷨û������
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

		System.out.println("������");
	}

	// ************�ж����������Ƿ���� ***********
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

	// ������鷢����
	public static Integer[] generateRandomArray(int maxSize, int maxValue) {
		Integer[] arr = new Integer[(int) ((maxSize * Math.random()) + 1)];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue * Math.random()) + 1) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// copy����
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

	// �����������
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
