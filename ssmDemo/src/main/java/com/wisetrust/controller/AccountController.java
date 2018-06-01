package com.wisetrust.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wisetrust.pojo.Account;
import com.wisetrust.request.AccountRequest;
import com.wisetrust.request.BaseRequest;
import com.wisetrust.response.AccountVO;
import com.wisetrust.service.IAccountService;
import com.wisetrust.util.DateUtil;
import com.wisetrust.util.ErrorCode;
import com.wisetrust.util.Executor;
import com.wisetrust.util.OutputMessage;
import com.wisetrust.util.ServiceException;

/**
 * Created by @author xu on @date 2018年 1月 15日 下午4:23:59
 * 
 * <br/>
 * 账户试图控制器
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private IAccountService accountService;

	/**
	 * 通过id查找
	 * 
	 * @param request
	 * @return 账户
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public OutputMessage getAccountByID(@Validated AccountRequest request, BindingResult result) {
		return Executor.execute(new Executor() {

			@Override
			protected Object execute() {

				if (request.getId() == null) {
					throw new ServiceException("请求异常", ErrorCode.BAD_REQUEST);
				}

				Account accountDetail = accountService.getAccountByID(Long.parseLong(request.getId()));
				if (accountDetail != null) {
					AccountVO accountVO = new AccountVO();
					accountVO.setId(accountDetail.getId());
					accountVO.setName(accountDetail.getLoginName());
					accountVO.setPhone(accountDetail.getCellphone());
					accountVO.setPost(accountDetail.getPos());
					accountVO.setStatus(accountDetail.getStatus());
					accountVO.setLastLoginTime(DateUtil.format(accountDetail.getLastLoginTime(), "yyyyMMddHHmmss"));
					accountVO.setGmtModified(DateUtil.format(accountDetail.getGmtModified(), "yyyyMMddHHmmss"));

					return accountVO;
				}
				return null;
			}
		}, result);
	}

	/**
	 * 获取全部账户信息
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public OutputMessage getList(@Validated BaseRequest request, BindingResult result) {
		return Executor.execute(new Executor() {

			@Override
			protected Object execute() {
				List<Account> accountDetails = accountService.getAccountList();
				if (accountDetails != null) {
					List<AccountVO> accountVOs = new ArrayList<AccountVO>();
					for (Account accountDetail : accountDetails) {
						if (accountDetail != null) {
							AccountVO accountVO = new AccountVO();
							accountVO.setId(accountDetail.getId());
							accountVO.setName(accountDetail.getLoginName());
							accountVO.setPhone(accountDetail.getCellphone());
							accountVO.setPost(accountDetail.getPos());
							accountVO.setStatus(accountDetail.getStatus());
							accountVO.setLastLoginTime(
									DateUtil.format(accountDetail.getLastLoginTime(), "yyyyMMddHHmmss"));
							accountVO.setGmtModified(DateUtil.format(accountDetail.getGmtModified(), "yyyyMMddHHmmss"));

							accountVOs.add(accountVO);
						}
					}
					return accountVOs;
				}
				return null;
			}
		}, result);
	}

}
