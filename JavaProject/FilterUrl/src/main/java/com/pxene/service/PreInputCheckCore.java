package com.pxene.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pxene.entity.Data;
import com.pxene.util.StringUtil;

/**
 * Created by @author xu on @date 2017�� 12�� 24�� ����3:42:45
 * 
 * ������ǰ���м��
 */
public class PreInputCheckCore {

	/**
	 * ��¼��url�����жϸ�url�����ݿ����Ƿ��Ѿ�������
	 * 
	 * @param list
	 *            ������ȡ���򲿷�
	 * @param noEnterUrl
	 *            �ȴ�¼���url
	 * @return map - ����Ѵ��ڵ�url��δ���ڵ�url��map.put(same, diff)��
	 */
	public Map<List<String>, List<String>> check(List<Data> list, List<String> noEnterUrl) {
		Map<List<String>, List<String>> map = new HashMap<List<String>, List<String>>();
		List<String> diff = new ArrayList<String>();
		List<String> same = new ArrayList<String>();

		// ��ʼ����ʶΪ��
		boolean b = false;

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		for (String string : noEnterUrl) {
			String url = StringUtil.decode(StringUtil.killBeginOfUrl(string.replaceAll("%(?![0-9a-fA-F]{2})", "%25")));

			for (Data data : list) {
				String domain = data.getDomain();
				String paramReg = data.getParam();
				String urlReg = data.getUrlReg();
				// ����ƥ��
				String compile = domain + urlReg;
				Pattern p = Pattern.compile(compile);
				Matcher m = p.matcher(url);
				b = m.matches();
				if (b) {
					String[] params = paramReg.split("\t");
					if (params.length == 1 && "NULL".equals(params[0])) {
						// �������ֻΪNULL����������������������ֱ���������true
						continue;
					}
					for (int i = 0; i < params.length; i++) {
						if ("NULL".equals(params[i])) {
							continue;
						}
						try {
							p = Pattern.compile(params[i]);
						} catch (Exception e) {
							System.out.println(params[i]);
							e.printStackTrace();
						}
						m = p.matcher(url);
						b = m.find();
						if (!b) { // ��һ����ͬ��Ϊ��ͬ
							break;
						}
					}
				}

				if (b) { // ������������url�������в�������Ĳ�����true
					same.add(string);
					break; // �������һ����ͬ�ģ��Ϳ����˳�ѭ��
				}
			}

			/*
			 * ���������
			 * 1��������������+url�������������
			 * 2��������������������ģ�ֻҪ��һ��������ľ��㣨�����������е�һ�����߼�����
			 */
			if (!b) {
				diff.add(string);
			}
		}
		map.put(same, diff);
		return map;
	}
}
