package com.trn.ns.enums;

public enum RenderingMode {
	Auto(0), Lossy(1);

	private final int value;
    private RenderingMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
