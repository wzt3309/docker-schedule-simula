package com.github.dockerschesimu.error.device;

/**
 * 设置cpu核心组利用率时，传入参数长度大于cpu核心数
 * @author wzt
 *
 */
@SuppressWarnings("serial")
public class SetCpuUseOutOfBoundError extends SetCpuUseError {

	public SetCpuUseOutOfBoundError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SetCpuUseOutOfBoundError(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
