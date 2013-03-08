package com.m6800.ram;

public interface IRAM {
	int read(int index);
	void write(int index, int wdata);
	void init(int length,int baseadd);

}
