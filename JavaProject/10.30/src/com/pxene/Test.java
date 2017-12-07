package com.pxene;

import java.util.ArrayList;

/**
 * Created by @author xuhongchao on @date 2017年 月 日 下午6:18:21
 */

public class Test {
	public static void main(String[] args) {
		int[] arr = { 1, 1, 3, 2, 3 };
		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < arr.length; i++) {
			if (list.contains(arr[i])) {
				continue;
			}
			list.add(arr[i]);
		}

		System.out.println(list.toString());

	}
}
