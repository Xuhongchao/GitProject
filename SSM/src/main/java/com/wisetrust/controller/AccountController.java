package com.wisetrust.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wisetrust.BaseRequest;
import com.wisetrust.dto.AccountVO;
import com.wisetrust.entity.Account;
import com.wisetrust.service.AccountService;
import com.wisetrust.util.DateUtil;
import com.wisetrust.util.Executor;
import com.wisetrust.util.OutputMessage;

/**
 * Created by @author xu on @date 2018年 月 日 下午4:02:16
 * 
 * account controller
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;

	/**
	 * 得到所有账户
	 * 
	 * @param request
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public OutputMessage getAccountByID(@Validated BaseRequest request, BindingResult result) {
		return Executor.execute(new Executor() {

			@Override
			protected Object execute() {
				List<Account> accountDetails = accountService.queryAccountAll();
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
