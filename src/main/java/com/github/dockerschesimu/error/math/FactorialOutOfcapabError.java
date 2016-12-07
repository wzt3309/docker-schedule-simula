package com.github.dockerschesimu.error.math;

/**
 * 阶乘计算超出能力，需要换用BaseMath.factorialBig(int n)
 * @author wzt
 *
 */
@SuppressWarnings("serial")
public class FactorialOutOfcapabError extends Exception {

	public FactorialOutOfcapabError() {
		super();
	}

	public FactorialOutOfcapabError(String message) {
		super(message);
	}

}
