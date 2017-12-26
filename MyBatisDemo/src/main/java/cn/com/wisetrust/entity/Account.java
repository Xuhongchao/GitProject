package cn.com.wisetrust.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by @author xu on @date 2017年 12月 21日 上午11:35:01
 * 
 * 账户表
 */
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5804217265748910620L;

	private int id;
	private String login_name;
	private String cellphone;
	private String password;
	private String salt;
	private int type;
	private String pos;
	private int status;
	private Date last_login_time;
	private Date gmt_create;
	private Date gmt_modified;
	private String open_id;
	private List<Note> note; // 备忘录

	public Account() {
		super();
	}

	public Account(String login_name, String cellphone, String password, String salt, int type, String pos, int status,
			Date last_login_time, Date gmt_create, Date gmt_modified, String open_id, List<Note> note) {
		super();
		this.login_name = login_name;
		this.cellphone = cellphone;
		this.password = password;
		this.salt = salt;
		this.type = type;
		this.pos = pos;
		this.status = status;
		this.last_login_time = last_login_time;
		this.gmt_create = gmt_create;
		this.gmt_modified = gmt_modified;
		this.open_id = open_id;
		this.note = note;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
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

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public List<Note> getNote() {
		return note;
	}

	public void setNote(List<Note> note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", login_name=" + login_name + ", cellphone=" + cellphone + ", password="
				+ password + ", salt=" + salt + ", type=" + type + ", pos=" + pos + ", status=" + status
				+ ", last_login_time=" + last_login_time + ", gmt_create=" + gmt_create + ", gmt_modified="
				+ gmt_modified + ", open_id=" + open_id + "]";
	}

}
