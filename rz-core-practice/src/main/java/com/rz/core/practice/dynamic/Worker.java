package com.rz.core.practice.dynamic;

public class Worker implements IWorker {

	@Override
	public String run(int value) {
		System.out.println(String.valueOf(value));
		return String.valueOf(value);
	}
}
