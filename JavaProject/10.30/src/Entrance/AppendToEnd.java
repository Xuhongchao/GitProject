package Entrance;

public class AppendToEnd {
	public static void main(String[] args) {
		String[] strs = {
				"206k03d10141	http://bbs.rong360.com/uc_server/avatar.php?size=small&uid=45389",
				"206k03d20141	http://campaign.rong360.com/credit/20170427/jinbi/index.html",
				"206k03d30141	https://www.rong360.com/",
				"20AS03d10141	http://spi.lakala.com:7080/1.1/statistics/apps/i90xl76fitu1rvvoatn02d988g8gcy850h2qzh0swuzb4o12/sendPolicy",
				"20Aj03d10141	http://app-normal.yirendai.com/init/initVersion",
				"20Aj03d20141	http://app-fund.yirendai.com/monitor",
				"20Aj03d30141	http://app-speed.yirendai.com/monitor",
				"20Aj03d40141	http://app-normal.yirendai.com/activity/allActivity",
				
				};

		for (int i = 0; i < strs.length; i++) {
			strs[i] = strs[i].trim();
			String str = strs[i] + "\t" + "�Ϻ�";
			System.out.println(str);
		}

	}
}
