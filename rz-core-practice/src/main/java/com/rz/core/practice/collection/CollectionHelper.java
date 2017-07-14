package com.rz.core.practice.collection;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

public class CollectionHelper {
	public static void main(String[] args) {
		Set<String> set = new HashSet<>();
		set.add("aaa");
		set.add("aaa");
		set.add("bbb");

		Map<String, Object> map = new HashMap<>();
		map.put("asd", new Object());
		for(Map.Entry<String, Object> entry : map.entrySet()){
			System.out.println(entry.getKey());
		}

		Stack stack = new Stack();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		while (0 != stack.size()) {
			System.out.println(stack.pop());
		}
	}
}
