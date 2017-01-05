package com.di.toolkit.img;

/**
 * @author di
 */
public enum ClolorEnum {
	RED((byte) 0), GREEN((byte) 1), BLUE((byte) 2);

	private ClolorEnum(byte value) {
		this.value = value;
	}

	private byte value;

	public byte value() {
		return value;
	}

}
