package com.rz.core.practice.serialization;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rz.core.practice.model.MapDto;
import com.rz.core.practice.model.MonitorDto;

public class JsonHelper {
	public static void main(String[] args){
		test();
		
		System.out.println("End JsonHelper...");
	}
	
	public static void test(){
		MonitorDto monitorDto = new MonitorDto();
		monitorDto.setAge(32222222);;
		monitorDto.setName("22222222");
		MapDto mapDto = new MapDto();
		mapDto.setEnglishName("setEnglishName");
		mapDto.setResult(true);
		monitorDto.setMap(mapDto);
		String json = JSON.toJSONString(monitorDto);
		System.out.println(json);
		monitorDto = (MonitorDto)JSON.parseObject(json, MonitorDto.class);
		System.out.println(monitorDto.toString());
		JSONObject jsonObject = JSON.parseObject(json);		
		System.out.println(jsonObject.getJSONObject("map").getString("englishName"));
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", "value");
		map.put("age", 20);
		System.out.println(JSON.toJSONString(map));
		
		JSON.parseObject("asd");
	} 
}
