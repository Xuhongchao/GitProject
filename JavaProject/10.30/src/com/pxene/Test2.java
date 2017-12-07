package com.pxene;

/**
 * Created by @author xuhongchao on @date 2017�� 11�� 27�� ����6:13:21
 */

public class Test2 {
	public static void main(String[] args) {
		ListNode head = new ListNode(1);
		ListNode head1 = new ListNode(2);
		ListNode head2 = new ListNode(3);
		ListNode head3 = new ListNode(4);
		ListNode head4 = new ListNode(5);

		head.next = head1;
		head1.next = head2;
		head2.next = head3;
		head3.next = head4;

		ListNode node = new Test2().reverseList(head);
		System.out.println(node.data);

	}

	public static ListNode reverseList(ListNode head) {
		ListNode prev = null;
		while (head != null) {
			ListNode tmp = head.next;
			head.next = prev;
			prev = head;
			head = tmp;
		}
		return prev;
	}
}

class ListNode {
	Object data;
	ListNode next;

	public ListNode(Object data) {
		super();
		this.data = data;
	}
}
