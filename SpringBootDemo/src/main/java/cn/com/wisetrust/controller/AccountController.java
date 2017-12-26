package cn.com.wisetrust.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @author xu on @date 2017年 12月 25日 下午3:05:35
 */

@RestController
@SpringBootApplication
@RequestMapping("/Account")
public class AccountController {
	@Value(value = "${book.name}")
	private String bookName;
	@Value(value = "${book.author}")
	private String bookAuthor;
	
	
	@RequestMapping("/name")
	public String name() {
		return "Hello World " + bookName + "-" + bookAuthor;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AccountController.class, args);
	}
}
