package com.wisetrust.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.alibaba.fastjson.JSON;

public abstract class Executor {
	private static Logger logger  = LoggerFactory.getLogger(Executor.class);

	public static OutputMessage execute(Executor executor,BindingResult result) {
		try {
			OutputMessage output = new OutputMessage();

			if (result != null && result.hasErrors()) {
				Validators.addParameterErrors(output, result);
				return output;
			}

			try {
				Object value = executor.execute();
				output.setPayload(value);
				
			} catch (ServiceException e) {
				output.setBusinessErrors(e.getMessage(),e.errorCode);;
			} catch (Exception e) {
				output.setSystemError();
			}
			logger.info("===========响应信息start=============");
	        logger.info("响应状态：[{}]",output.getResultCode());
	        ErrorResult errorResult = output.getErrorCode();
	        logger.info("响应错误状态码：[{}]",errorResult==null?null:errorResult.err);
	        logger.info("响应错误信息：[{}]",errorResult==null?null:errorResult.res);
	        logger.info("响应报文内容：[{}]",JSON.toJSONString(output.getPayload()));
	        logger.info("===========请求信息end=============");
			return output;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract Object execute();

}
