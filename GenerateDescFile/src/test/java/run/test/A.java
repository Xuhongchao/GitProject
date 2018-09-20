package run.test;

/**
 * 
 * @ClassName Project
 * @Description
 * @Reason
 * @date 2018年9月20日上午9:41:38
 *
 * @author 徐洪超
 * @Email 1028873786@qq.com
 * @version
 * @since JDK 1.8
 */
public class A {

	public static void main(String[] args) {
		try {
			System.out.println(r());
			System.out.println("3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String r() throws Exception {
		int i = 1 / 0;
		try {
			System.out.println("1");
			return "2";
		} catch (Exception e) {
			throw new RuntimeException("------");
		}

		
	}

}
