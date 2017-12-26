package cn.com.wisetrust.service;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import cn.com.wisetrust.dao.GetSqlSession;
import cn.com.wisetrust.dao.IAccount;
import cn.com.wisetrust.entity.Account;
import cn.com.wisetrust.entity.Note;

/**
 * Created by @author xu on @date 2017年 12月 21日 下午6:31:48
 */
public class UserService {
	@Test
	public void getUser() throws Exception {
		SqlSession session = GetSqlSession.getSqlSession().openSession();
		Account account = session.selectOne("cn.com.wisetrust.entity.AccountMapper.GetAccountByID", 1);
		System.out.println(account);
	}

	@Test
	public void getUserByInterface() throws Exception {
		SqlSessionFactory sqlSessionFactory = GetSqlSession.getSqlSession();
		sqlSessionFactory.getConfiguration().addMapper(IAccount.class);

		SqlSession session = sqlSessionFactory.openSession();
		IAccount iAccount = session.getMapper(IAccount.class);
		Account account = iAccount.getAccountByID(1);

		System.out.println(account);
	}

	public static void main(String[] args) {
		SqlSession session = GetSqlSession.getSqlSession().openSession();
		IAccount iAccount = session.getMapper(IAccount.class);

		// 查找全部 getAccountList
		// List<Account> list = iAccount.getAccountList();
		// for (Account account : list) {
			// System.out.println(account);
		// }

		// 添加 addAcount
		// Account account = new Account("徐洪超", "15110232314", "111111", "2FA058D354B79CA0", 1, "开发", 1, new Date(),
				// new Date(), new Date(), null);
		// iAccount.addAcount(account);
		// session.commit();

		// 更新 updateAccount
		// Account account = iAccount.getAccountByID(13);
		// account.setLast_login_time(new Date());
		
		// iAccount.updateAccount(account);
		// session.commit();
		
		// 删除 deleteAccount
		// iAccount.deleteAccount(13);
		// session.commit();
		
	}
	
	@Test
	public void getAccountIncludeNote() throws Exception {
		SqlSession session = GetSqlSession.getSqlSession().openSession();
		IAccount iAccount = session.getMapper(IAccount.class);
		Account account = iAccount.getAccountByID(2);
		
		//Account account = session.selectOne("getAccountByID", 2);
		List<Note> notes = account.getNote();
		for(Note note : notes){
			System.out.println(note.getTitle());
		}
	}
}