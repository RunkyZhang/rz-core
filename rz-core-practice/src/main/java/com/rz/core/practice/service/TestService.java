package com.rz.core.practice.service;

import javax.annotation.Resource;

import org.elasticsearch.client.Client;
import org.springframework.stereotype.Service;

@Service
public class TestService implements ITestService {

//	@Resource
//	private Client elasticsearchClient;

	@Override
	public String Test(String value) {
	    return "";
//		if(null == this.elasticsearchClient){http://localhost:8080/hello/abc
//			return "null";testService
//		}
//		else{
//			return this.elasticsearchClient.settings().toString();
//		}
	}
}
