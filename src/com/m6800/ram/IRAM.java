package com.m6800.ram;

public interface IRAM {
	byte read(int index);
	void write(int index, byte wdata);
	void init(int length,int baseadd);

}
