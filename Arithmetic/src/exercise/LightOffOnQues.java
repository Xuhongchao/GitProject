package exercise;

/**
 * 
 * @ClassName LightOffOnQues
 * @Description 灯开关问题
 * @Reason
 * @date 2018年9月10日下午6:31:18
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class LightOffOnQues {

	/**
	 * <pre>
	 * 问题描述：
	 * 	一间屋子里有 100 盏灯排成一行，按从左至右的顺序编上号 1 、 2 、 3 、 4 、 5.....99 、 100 ，
	 * 	每盏灯都有一个开关，开始全都关着，
	 * 	把 100 个学生排在后面，第 1 个学生把 1 的倍数的灯会都拉一下，
	 * 	第 2 个学生把 2 的倍数的灯会都拉一下......第 100 个学生把 100 的倍数的都拉一下，
	 * 	这时灯有多少是开着的？
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] count = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 计数数组
		int[] arr = new int[10]; // 隐式的表示灯的标号

		for (int i = 1; i <= 10; i++) { // 灯
			for (int j = i; j <= 10; j++) { // 人，人的标号从比灯的标号大的开始
				if (j % i == 0) { // 人的下标是灯的倍数人就可以去碰灯
					arr[i - 1] = ++count[i - 1];
				}
			}
		}

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] % 2 == 0) { // 偶是关
				System.out.println("第" + (i + 1) + "盏灯，是关的");
			} else { // 奇是开
				System.out.println("第" + (i + 1) + "盏灯，是开的");
			}
		}
	}
}
