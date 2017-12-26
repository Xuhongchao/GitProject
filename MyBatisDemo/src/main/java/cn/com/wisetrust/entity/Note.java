package cn.com.wisetrust.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by @author xu on @date 2017年 12月 22日 下午1:52:18
 * 
 * 备忘录
 */
public class Note implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7036430299949746017L;

	private int id;
	private String title; // 标题
	private String content; // 内容
	private Date gmt_create; // 创建时间
	private Date gmt_modified; // 修改时间
	private Account account; // 创建人账户

	public Note() {
		super();
	}

	public Note(int id, String title, String content, Date gmt_create, Date gmt_modified, Account account) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.gmt_create = gmt_create;
		this.gmt_modified = gmt_modified;
		this.account = account;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(Date gmt_create) {
		this.gmt_create = gmt_create;
	}

	public Date getGmt_modified() {
		return gmt_modified;
	}

	public void setGmt_modified(Date gmt_modified) {
		this.gmt_modified = gmt_modified;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Note [id=" + id + ", title=" + title + ", content=" + content + ", gmt_create=" + gmt_create
				+ ", gmt_modified=" + gmt_modified + ", account_id=" + account.getId() + "]";
	}

}
