package com.rz.core.practice.error;

import java.util.ArrayList;
import java.util.List;

public class AggregateException extends Exception {
	private static final long serialVersionUID = -188760075294368106L;
	private List<Throwable> causes;

	public AggregateException() {
		this.causes = new ArrayList<>();
	}

	public void initCauses(Throwable... causes) {
		if (null != causes) {
			for (Throwable cause : causes) {
				if (null != cause) {
					this.causes.add(cause);
				}
			}
		}
	}

	public List<Throwable> getCauses() {
		return this.causes;
	}
}
