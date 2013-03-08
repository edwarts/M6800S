package com.m6800.ram;

public class M6800RAM implements IRAM{

	///////
	// in one unit, only 2bytes are stored , so 512 store space needed for this ram. one int store 2 bytes
	//////
	private int[] data;//to avoid the unsigned byte, short problem, use int for all the variable
	private int basedaddress=0;
	@Override
	public int read(int index) {
		// TODO Auto-generated method stub
		if(index!=-1&&(basedaddress+index<=data.length-1))
		{
		return data[basedaddress+index];
		}
		else
		{
			return 0;
		}
	}


	@Override
	public void init(int length,int baseadd) {
		basedaddress=baseadd;
		// TODO Auto-generated method stub
		data=new int[length];
	}


	@Override
	public void write(int index, int wdata) {
		if(index!=-1&&index<=data.length-1)
		{
		data[basedaddress+index]=wdata;
		}
		else
		{
		
		}
		
	}

}
