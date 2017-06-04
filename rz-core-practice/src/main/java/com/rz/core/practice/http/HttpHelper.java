package com.rz.core.practice.http;

import java.io.IOException;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class HttpHelper {

	public static void main(String[] args) {

		try {
			String url = "http://192.168.38.108:8501/v1/api/index/data/portal/article?from=0&size=5000";
			try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
					CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(new HttpGet(url))) {
				HttpEntity httpEntity = closeableHttpResponse.getEntity();
				String charset = HttpHelper.getContentCharset(httpEntity, HTTP.UTF_8);
				String json = EntityUtils.toString(httpEntity, charset);
				System.out.println(json);
			}
		} catch (Exception e) {
		}

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://yz.notify.yeshj.com/push/v3/readme");

		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			System.out.println("Response content: " + EntityUtils.toString(httpEntity, "UTF-8"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getContentCharset(HttpEntity httpEntity, String defaultCharset) {
		try {
			String charset = null;
			if (null != httpEntity.getContentType()) {
				HeaderElement[] headerElements = httpEntity.getContentType().getElements();
				if (0 < headerElements.length) {
					NameValuePair nameValuePair = headerElements[0].getParameterByName("charset");
					if (null != nameValuePair) {
						charset = nameValuePair.getValue();
					}
				}
			}
			return null == charset ? defaultCharset : charset;
		} catch (Exception e) {
			return defaultCharset;
		}
	}
}
