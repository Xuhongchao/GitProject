package com.pxene.entrance;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pxene.entity.Data;
import com.pxene.service.LoadNotEnterUrl;
import com.pxene.service.LoadReg;
import com.pxene.service.PreInputCheckCore;
import com.pxene.util.IOUtil;

/**
 * Created by @author xu on @date 2017�� 12�� 24�� ����2:14:30
 * 
 * <ul>
 * <li>¼��ǰ�ж�url�ĳ������</li>
 * <li>ע�����
 * <ul>
 * <li>1��������Ҫ��app_crawl_detail.xlsx�ļ��д����ڶ����������ǵڶ�����sheet������Ҫ¼������ݷŵ����sheet��</li>
 * <li>2�����г�����Ҫ����ָ��λ�ô�����same.txt��diff.txt�����ļ�</li>
 * <li>3��Դ�ļ���ַΪ�������޸����IOUtil���е�SOURCE_PATH��ֵ���ɣ���SOURCE_PATH =
 * "C:\\Users\\xuhongchao\\Desktop\\app_crawl_detail.xlsx";</li>
 * </ul>
 * </li>
 * </ul>
 */
public class Entrance2 {
	private static final String SAME_PATH = "C:\\Users\\xu\\Desktop\\same.txt";
	private static final String DIFF_PATH = "C:\\Users\\xu\\Desktop\\diff.txt";

	public static void main(String[] args) {
		IOUtil ioUtil = IOUtil.getInstance();
		
		List<Data> list = new LoadReg().readFromExcelToData(); // ������

		List<String> noEnterUrl = new LoadNotEnterUrl().getNotEnterUrl(); // �ô�¼������

		Map<List<String>, List<String>> map = new PreInputCheckCore().check(list, noEnterUrl); // ���Ѿ������ݿ��зŵ�һ�������У��������ݿ�ķŵ�һ��������
		
		for(Entry<List<String>, List<String>> entry : map.entrySet()){
			List<String> same = entry.getKey();
			System.out.println(same.size());
			ioUtil.writeToFile(new File(SAME_PATH), same);
			
			List<String> diff = entry.getValue();
			System.out.println(diff.size());
			ioUtil.writeToFile(new File(DIFF_PATH), diff);
		}
	}

}
