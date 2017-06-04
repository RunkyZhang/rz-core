package com.rz.core.practice.error;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ErrorHelper {
	public static void main(String[] args) {
		ErrorHelper errorHelper = new ErrorHelper();

		try {
//			errorHelper.test4(o -> {
//				
//			});
			errorHelper.test();
		} catch (AggregateException e) {
			List<Throwable> causes = e.getCauses();
			for (Throwable cause : causes) {
				System.out.println(cause.getMessage());
			}
		}

		System.out.println("End_ErrorHelper...");
	}

	private void test() throws AggregateException {
		AggregateException aggregateException = new AggregateException();

		List<Throwable> throwables = new ArrayList<>();
		try {
			this.test1();
		} catch (Exception e) {
			throwables.add(e);
			aggregateException.initCauses(e);
		}

		try {
			this.test2();
		} catch (Exception e) {
			throwables.add(e);
			aggregateException.initCauses(e);
		}

		try {
			this.test3();
		} catch (Throwable e) {
			throwables.add(e);
			aggregateException.initCauses(e);
		}
		aggregateException.initCauses(throwables.toArray(new Throwable[throwables.size()]));

		throw aggregateException;
	}

	private void test1() {
		throw new RuntimeException("runtime");
	}

	private void test2() throws Exception {
		throw new Exception("exception");
	}

	private void test3() throws Throwable {
		throw new Exception("Throwable");
	}

//	private void test4(Consumer<String> consumer) {
//		try {
//			consumer.accept("ssss");
//		} catch (AggregateException e) {
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}
}
