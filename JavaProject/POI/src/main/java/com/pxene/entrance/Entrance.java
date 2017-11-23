package com.pxene.entrance;

import java.util.List;
import java.util.Map;

import com.pxene.entity.Data;
import com.pxene.service.Core;
import com.pxene.service.LoadLine;
import com.pxene.service.LoadUrlExam;

/**
 * Created by @author xuhongchao on @date 2017�� 11�� 13�� ����7:42:00
 */

public class Entrance {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		List<Data> list = new LoadLine().readFromExcelToData(); // ������
		Map<String, String> map = new LoadUrlExam().getAllUrl(); // ��url
		Core core = new Core();
		core.filter(list, map); // ���ķ���
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		long end = System.currentTimeMillis();
		System.out.println("����ʱ�䣺" + (end - start) / 1000 + "s");
	}
}
