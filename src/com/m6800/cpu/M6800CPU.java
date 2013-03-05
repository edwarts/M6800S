package com.m6800.cpu;

import java.util.Stack;

import com.m6800.ram.IRAM;

public class M6800CPU {
	public Stack<Short> pcstack=new Stack<Short>();//stack for submarine program
	public IRAM ram;
	public int cycles;
	private byte A;//Accumulator A use 16bit
	private byte B;//Accumulator B use 16 bit
	
	public short SP;//Stack Pointer 16 bit
	public short PC;//Program Counter
	public short X;
	
	private int carryFlag = 0;// C
	private int zeroFlag = 0;//Z
	private int interruptMask = 0;//I
	private int halfCarryFlag = 0;//H
	private int overflowFlag = 0;//V
	private int negativeFlag = 0;//N
	public M6800CPU(IRAM m_ram)
	{
		ram=m_ram;//
	}
	public int getCycles() {
		return cycles;
	}
	public void setCycles(int cycles) {
		this.cycles = cycles;
	}
	public byte getA() {
		return A;
	}
	public void setA(byte a) {
		A = a;
	}
	public byte getB() {
		return B;
	}
	public void setB(byte b) {
		B = b;
	}
	public short getSP() {
		return SP;
	}
	public void setSP(byte sP) {
		SP = sP;
	}
	public short getPC() {
		return PC;
	}
	public void setPC(byte pC) {
		PC = pC;
	}
	public short getX() {
		return X;
	}
	public void setX(short x) {
		X = x;
	}
	public int getCarryFlag() {
		return carryFlag;
	}
	public void setCarryFlag(int carryFlag) {
		this.carryFlag = carryFlag;
	}
	public int getZeroFlag() {
		return zeroFlag;
	}
	public void setZeroFlag(int zeroFlag) {
		this.zeroFlag = zeroFlag;
	}
	public int getInterruptMask() {
		return interruptMask;
	}
	public void setInterruptMask(byte interruptMask) {
		this.interruptMask = interruptMask;
	}
	public int getHalfCarryFlag() {
		return halfCarryFlag;
	}
	public void setHalfCarryFlag(byte halfCarryFlag) {
		this.halfCarryFlag = halfCarryFlag;
	}
	public int getOverflowFlag() {
		return overflowFlag;
	}
	public void setOverflowFlag(byte overflowFlag) {
		this.overflowFlag = overflowFlag;
	}
	public int getNegativeFlag() {
		return negativeFlag;
	}
	public void setNegativeFlag(byte negativeFlag) {
		this.negativeFlag = negativeFlag;
	}
	public final void runcycle() {

		int instr = ram.read(this.PC++);
		switch(instr){
		//A+B
		case 57:
			rts();
			break;
		case 27:
			aba();
			break;
		case 139: //0x8B:Immediate ADD A=A+M
			addA(imm());
			this.cycles+=2;
			break;
		case 155: //0x9B Direct ADD A=A+M
			addA(direct());
			this.cycles+=3;
			break;		
		case 171://0xAB indexed ADD A=A+M
			addA(index());
			this.cycles+=5;
			break;
		case 187://0xBB extended ADD A=A+M
			addA(extend());
			this.cycles+=4;
			break;
					
		//addB	
		case 203: //0xCB Immediate ADD B=B+M
			addB(imm());
			this.cycles+=2;
			break;
		case 219://0xDB Direct ADD B=B+M
			addB(direct());
			this.cycles+=3;
			break;		
		case 235://0xEB indexed ADD B=B+M
			addB(index());
			this.cycles+=5;
			break;
		case 251://0xFB extended ADD B=B+M
			addB(extend());
			this.cycles+=4;
			break;

			
		//adcA	
		case 137: //0x89 Immediate ADCA A=A+M+C
			adcA(imm());
			this.cycles+=2;
			break;
		case 153: //0x99 direct ADCA
			adcA(direct());
			this.cycles+=3;
			break;
		case 169://0xA9Index ADCA
			adcA(index());
			this.cycles+=5;
			break;
		case 185://0xB9 extended ADCA
			adcA(extend());
			this.cycles+=4;
			break;
		//adcB
		case 201://0xC9 Immediate ADCB B=B+M+C
			adcB(imm());
			this.cycles+=2;
			break;
		case 217://0xD9 direct ADCB
			adcB(direct());
			this.cycles+=3;
			break;
		case 233://0xE9Index ADCB
			adcB(index());
			this.cycles+=5;
			break;
		case 249://0xF9 extended ADC
			adcB(extend());
			this.cycles+=4;
			break;
		//and A	
		case 132://0x84://Immediate AND A=A.M
			andA(imm());
			this.cycles+=2;
			break;
		case 148://0x94://Direct AND A=A.M
			andA(direct());
			this.cycles+=3;
			break;		
		case 164://0xA4://indexed AND A=A.M
			andA(index());
			this.cycles+=5;
			break;
		case 180://0xB4://extended AND A=A.M
			andA(extend());
			this.cycles+=4;
			break;
		//and B
		case 196://0xC4 Immediate AND B=B.M
			andB(imm());
			this.cycles+=2;
			break;
		case 212://0xD4 Direct AND B=B.M
			andB(direct());
			this.cycles+=3;
			break;		
		case 228://0xE4 indexed AND B=B.M
			andB(index());
			this.cycles+=5;
			break;
		case 244://0xF4 extended AND B=B.M
			andB(extend());
			this.cycles+=4;
			break;
		//bit test A
		case 133://Immediate BIT A.M
			bit(imm(),this.A);
			this.cycles+=2;
			break;
		case 149://Direct BIT A.M
			bit(imm(),this.A);
			this.cycles+=3;
			break;		
		case 165://indexed BIT A.M
			bit(imm(),this.A);
			this.cycles+=5;
			break;
		case 181://extended BIT A.M
			bit(imm(),this.A);
			this.cycles+=4;
			break;	
		
			//bit test B
		case 197://Immediate BIT B.M
			bit(imm(),this.B);
			this.cycles+=2;
			break;
		case 213://Direct BIT B.M
			bit(imm(),this.B);
			this.cycles+=3;
			break;		
		case 229://indexed BIT B.M
			bit(imm(),this.B);
			this.cycles+=5;
			break;
		case 245://extended BIT B.M
			bit(imm(),this.B);
			this.cycles+=4;
			break;	
		//clear
		case 111://0x6F extend clear M
			clr(index());
			this.cycles+=7;
			break;		
		case 127://0x7F extend clear M
			clr(extend());
			this.cycles+=6;
			break;
		case 79://0x4F clear A
			this.A=0;
			this.zeroFlag=1;
			this.negativeFlag=0;
			this.carryFlag=0;
			this.overflowFlag=1;
			this.cycles+=2;
			break;
		case 95://0x5F clear B
			this.B=0;
			this.zeroFlag=1;
			this.negativeFlag=0;
			this.carryFlag=0;
			this.overflowFlag=0;
			this.cycles+=2;
			break;
		//compare
		case 129://0x81 Immediate CMPA A-M
			cmp(this.A, imm());
			this.cycles+=2;
			break;
		case 145://0x91 Direct CMPA A-M
			cmp(this.A, direct());
			this.cycles+=3;
			break;		
		case 161://0xA1 indexed CMPA A-M
			cmp(this.A, index());
			this.cycles+=5;
			break;
		case 177://0xB1extended CMPA A-M
			cmp(this.A, extend());
			this.cycles+=4;
			break;
		case 193://0xC1 Immediate CMPB B-M
			cmp(this.B, imm());
			this.cycles+=2;
			break;
		case 209://0xD1 Direct CMPB B-M
			cmp(this.B, direct());
			this.cycles+=3;
			break;		
		case 225://0xE1 indexed CMPB B-M
			cmp(this.B, index());
			this.cycles+=5;
			break;
		case 241://0xF1 extended CMPB B-M
			cmp(this.B, extend());
			this.cycles+=4;
			break;			
		case 17://0x11 CBA A-B
			cba();
			this.cycles+=2;
			break;
		//1's complement
		case 99://0x63 indexed COM M=-M 	
			com(index());
			this.cycles+=7;
			break;
		case 115://0x73 extended COM M=-M 
	        com(extend());
	        this.cycles+=6;
	        break;
		case 67://0x43 COMA A=-A
			this.A^=255;
			setflags(this.A);
			this.overflowFlag=0;
			this.carryFlag=0;
			this.cycles+=2;
			break;			
		case 83://0x53 COMA B=-B
			this.B^=255;
			setflags(this.B);
			this.overflowFlag=0;
			this.carryFlag=0;
			this.cycles+=2;
			break;
		//2's complement
		
		case 96://0x60 indexed NEG M=00-M 	
			neg(index());
			this.cycles+=7;
			break;
		case 112://0x73 extended NEG M=-M 
	        neg(extend());
	        this.cycles+=6;
	        break;
		case 64://0x43 NegA A=00-A
			this.A^=255;
			this.A+=1;
			setflags(this.A);
			this.overflowFlag=test12(this.A,128);
			this.carryFlag=test12(this.A,0);
			this.cycles+=2;
			break;
		case 80://0x43 NegB B=00-B
			this.B^=255;
			this.B+=1;
			setflags(this.B);
			this.overflowFlag=test12(this.B,128);
			this.carryFlag=test12(this.B,0);
			this.cycles+=2;
			break;
		//Decimal Adjust
			
			
		//decrement
			
		case 106://0x6A indexed DEC M=M-1 	
			dec(index());
			this.cycles+=7;
			break;
		case 122://0x7A extended DEC M=M-1 
	        dec(extend());
	        this.cycles+=6;
	        break;
		case 74://0x4A DECA A=A-1
			this.A-=1;
			setflags(this.A);
			this.cycles+=2;
			break;
		case 90://0x5A DECB B=B-1
			this.B-=1;
			setflags(this.B);
			this.cycles+=2;
			break;
		//exclusive OR
		
		case 136: //0x88 Immediate EORA
			eorA(imm());
			this.cycles+=2;
			break;
		case 152: //0x98 direct EORA
			eorA(direct());
			this.cycles+=3;
			break;
		case 168://0xA8 Index EORA
			eorA(index());
			this.cycles+=5;
			break;
		case 184://0xB8 extended EORA
			eorA(extend());
			this.cycles+=4;
			break;			
		case 200: //0xC8 Immediate EORB
			eorB(imm());
			this.cycles+=2;
			break;
		case 216: //0xD8 direct EORB
			eorB(direct());
			this.cycles+=3;
			break;
		case 232://0xE8 Index EORB
			eorB(index());
			this.cycles+=5;
			break;
		case 248://0xF8 extended EORB
			eorB(extend());
			this.cycles+=4;
			break;
		//increment
		case 108://0x6C indexed INC M=M+1 	
			inc(index());
			this.cycles+=7;
			break;
		case 124://0x7C extended INC M=M+1 
	        inc(extend());
	        this.cycles+=6;
	        break;
		case 76://0x4C INCA A=A+1
			this.A-=1;
			setflags(this.A);
			this.cycles+=2;
			break;
		case 92://0x5C INCB B=B+1
			this.B-=1;
			setflags(this.B);
			this.cycles+=2;
			break;
		//load accumulator
		
		case 134://0x86 Immediate LDAA A=M
			ldaA(imm());
			this.cycles+=2;
			break;
		case 150://0x96 Direct LDAA A=M
			ldaA(direct());
			this.cycles+=3;
			break;		
		case 166://0xA6 indexed LDAA A=M
			ldaA(index());
			this.cycles+=5;
			break;
		case 182://0xB6 extended LDAA A=M
			ldaA(extend());
			this.cycles+=4;
			break;
		case 198://0xC6 Immediate LDAB B=M
			ldaB(imm());
			this.cycles+=2;
			break;
		case 214://0xD6 Direct LDAB B=M
			ldaB(direct());
			this.cycles+=3;
			break;		
		case 230://0xE6 indexed LDAB B=M
			ldaB(index());
			this.cycles+=5;
			break;
		case 246://0xF6 extended LDAB B=M
			ldaB(extend());
			this.cycles+=4;
			break;			
		//or inclusive
		case 138: //0x8A Immediate ORAA
			oraA(imm());
			this.cycles+=2;
			break;
		case 154: //0x9A direct ORAA
			oraA(direct());
			this.cycles+=3;
			break;
		case 170://0xAA Index ORAA
			oraA(index());
			this.cycles+=5;
			break;
		case 186://0xBA extended ORAA
			oraA(extend());
			this.cycles+=4;
			break;			
		case 202: //0xCA Immediate ORAB
			oraB(imm());
			this.cycles+=2;
			break;
		case 218: //0xDA direct ORAB
			oraB(direct());
			this.cycles+=3;
			break;
		case 234://0xEA Index EORB
			oraB(index());
			this.cycles+=5;
			break;
		case 250://0xFA extended EORB
			oraB(extend());
			this.cycles+=4;
			break;
		//push
			
		case 54://0x36 PSHA
			ram.read(this.PC + 1);
			push(this.A);
			this.cycles+=4;
			break;
		case 55://0x37 PSHB
			ram.read(this.PC + 1);
			push(this.B);
			this.cycles+=4;
			break;
		//pull
		
		case 50://0x32 PULA
			ram.read(this.PC + 1);
		    this.A = pull();
			this.cycles+=4;
			break;
		case 51://0x33 PULB
			ram.read(this.PC + 1);
			this.B=pull();
			this.cycles+=4;
			break;
		//Rotate left
		case 105://0x69 ROL
			rol(index());
		    this.cycles+=7;
			break;
		case 121://0x79 extended ROL
			rol(extend());
			this.cycles+=6;
			break;
			
		//Rotate Accumulator left
		case 73://0x49 ROLA
			rolA();
		    this.cycles+=2;
			break;
		case 89://0x59 ROLB
			rolB();
			this.cycles+=2;
			break;
			
		//Rotate right
		case 102://0x66 ROR
			ror(index());
		    this.cycles+=7;
			break;
		case 118://0x76 extended ROR
			ror(extend());
			this.cycles+=6;
			break;			
		//Rotate Accumulator right
		case 70://0x46 RORA
			rorA();
			this.cycles+=2;
			break;
		case 86://0x56 RORB
			rorB();
			this.cycles+=2;
			break;
		//Arithmetic shift left
		case 104://0x68 index ASL
			asl(index());
		    this.cycles+=7;
			break;
		case 120://0x78 extended ASL
			asl(extend());
			this.cycles+=6;
			break;			
		//Arithmetic shift left Accumulator 
		case 72://0x48 ASLA
			aslA();
			this.cycles+=2;
			break;
		case 88://0x58 ASLB
			aslB();
			this.cycles+=2;
			break;
			
		//	Arithmetic shift right
		case 103://0x67 index ASR
			asr(index());
		    this.cycles+=7;
			break;
		case 119://0x77 extended ASR
			asr(extend());
			this.cycles+=6;
			break;			
		//Arithmetic shift right Accumulator 
		case 71://0x47 ASRA
			asrA();
			this.cycles+=2;
			break;
		case 87://0x57 ASLB
			asrB();
			this.cycles+=2;
			break;
		//logic shift right
		
		case 100://0x64 index LSR
			lsr(index());
		    this.cycles+=7;
			break;
		case 116://0x74 extended LSR
			lsr(extend());
			this.cycles+=6;
			break;			
		//logic shift right Accumulator 
		case 68://0x44 ASRA
			lsrA();
			this.cycles+=2;
			break;
		case 84://0x54 ASLB
			lsrB();
			this.cycles+=2;
			break;
			
		//store accumulator
		case 151://0x97 index STAA
			staA(direct());
		    this.cycles+=4;
			break;
		case 167://0xA7 index STAB
			staA(index());
		    this.cycles+=6;
			break;
		case 183://0xB7 extend STAB
			staA(extend());
		    this.cycles+=4;
			break;
		case 215://0xD7 index STAB
			staB(direct());
		    this.cycles+=4;
			break;
		case 231://0xE7 index STAB
			staB(index());
		    this.cycles+=6;
			break;
		case 247://0xF7 extend STAB
			staB(extend());
		    this.cycles+=4;
			break;
		//Subtract with carry
		case 130: //0x82:Immediate SBCA A=A-M-C
			sbcA(imm());
			this.cycles+=2;
			break;
		case 146: //0x92 Direct SBCA A=A-M-C
			sbcA(direct());
			this.cycles+=3;
			break;		
		case 162://0xA2indexed SBCA A=A-M-C
			sbcA(index());
			this.cycles+=5;
			break;
		case 178://0xB2 extended SBCA A=A-M-C
			sbcA(extend());
			this.cycles+=4;
			break;
		case 194: //0xC2:Immediate SBCA A=A-M-C
			sbcB(imm());
			this.cycles+=2;
			break;
		case 210: //0xD2 Direct SBCA A=A-M-C
			sbcB(direct());
			this.cycles+=3;
			break;		
		case 226://0xE2 indexed SBCA A=A-M-C
			sbcB(index());
			this.cycles+=5;
			break;
		case 242://0xF2 extended SBCA A=A-M-C
			sbcB(extend());
			this.cycles+=4;
			break;
		//subtract 
		case 128: //0x80:Immediate SUBA A=A-M
			subA(imm());
			this.cycles+=2;
			break;
		case 144: //0x90 Direct SUBA A=A-M
			subA(direct());
			this.cycles+=3;
			break;		
		case 160://0xA0 indexed SUBA A=A-M
			subA(index());
			this.cycles+=5;
			break;
		case 176://0xB0 extended SUBA A=A-M
			subA(extend());
			this.cycles+=4;
			break;
		case 192: //0xC0:Immediate SUBB B=B-M
			subB(imm());
			this.cycles+=2;
			break;
		case 208: //0xD0 Direct SBCA SUBB B=B-M
			subB(direct());
			this.cycles+=3;
			break;		
		case 224://0xE0 indexed SBCA SUBB B=B-M
			subB(index());
			this.cycles+=5;
			break;
		case 240://0xF0 extended SUBB B=B-M
			subB(extend());
			this.cycles+=4;
			break;	
		case 16://0x10 SBA A=A-B//carry need
			this.A=(byte) (this.A-this.B);
			this.carryFlag = this.A >> 8 == 0?1:0;
		    this.overflowFlag = ((((this.B ^ this.A) & 0x80) != 0) && (((this.B ^this.A ) & 0x80) != 0))==true?1:0;
		    this.A = (byte) (this.A & 0xFF);
		    setflags(this.A);
			this.cycles+=2;
		case 22://0x16 TAB B=A
			this.B=this.A;
			this.overflowFlag=0;
			setflags(this.B);
			this.cycles+=2;
			break;
		case 23://0x17 TBA A=B
			this.A=this.B;
			this.overflowFlag=0;
			setflags(this.A);
			this.cycles+=2;
			break;
		case 109://0x6D index TST
		    tst(index());
		    this.cycles+=7;
		    break;
		case 125://0x7D index TST
		    tst(extend());
		    this.cycles+=6;
		    break;
		case 77://0x4D TSTA
		    tstA();
		    this.cycles+=2;
		    break;
		case 93://0x5D TSTB
		    tstB();
		    this.cycles+=2;
		    break;
		//cpx
		case 140:
			cpx(3);
			break;
			//
		case 156:
			cpx(0);
			break;
		case 172:
			cpx(1);
			break;
		case 188:
			cpx(2);
			break;
		    
		    
		
		case 9://0x09 DEX
			this.X-=1;
			this.PC++;
			this.cycles+=4;
			setflags(X);
			break;
		case 52://0x34 DES
			this.SP-=1;
			this.PC++;
			this.cycles+=4;
			setflags(SP);
			break;
		case 8://0x08 INX
			this.X+=1;
			this.PC++;
			this.cycles+=4;
			setflags(X);
			break;
		case 49://0x31 DES
			this.SP+=1;
			this.PC++;
			this.cycles+=4;
			setflags(SP);
			break;
			
		case 223://0xDF STX direct
			stx(0);
			this.cycles+=5;
		    break;
		case 239://0xEF STX index
			stx(1);
			this.cycles+=7;
		    break;
		case 255://0xFF STX extended
			stx(2);
			this.cycles+=6;
		    break;
		case 159:// 0x9F STS  
			sts(0);
			this.cycles+=5;
		    break;
		case 175://0xAF STS index
			sts(1);
			this.cycles+=7;
		    break;
		case 191://0xBF STS extended
			sts(2);
			this.cycles+=6;
		    break; 
		 //load index reister
		case 206://0xCE LDX imm 3
			this.ldx(3);
			this.cycles+=3;
			break;
		case 222://0XDE LDX direct
			this.ldx(0);
			this.cycles+=4;
			break;
		case 238://0xEE LDX index
			this.ldx(1);
			this.cycles+=6;
			break;
		case 254://0xFE LDX extended
			this.ldx(2);
			this.cycles+=5;
			break;
		    
		//load stack poniter
		case 142://0x8E LDS imm 3
			this.lds(3);
			this.cycles+=3;
			break;
		case 158://0X9E LDS direct
			this.lds(0);
			this.cycles+=4;
			break;
		case 174://0xEE LDS index
			this.lds(2);
			this.cycles+=6;
			break;
		case 190://0xFE LDS extended
			this.lds(3);
			this.cycles+=5;
			break;

			
			
		case 53://0x35 TXS
			this.SP=(byte) (this.X-1);
			this.cycles+=4;
			break;
		case 48://0x30 TSX
			this.X=(byte) (this.SP-1);
			this.cycles+=4;
			break;
		case 141://8D brach to submarine
		    brs();
		    break;
			
		//the rest of instrunciton

		case 36://0x24
			if (getCarryFlag()==0)
			{
			stbrach();
			}
			else {
			stnbrach();
			}
			break;
		case 37://0x25
			if(this.getCarryFlag()==1)
			{
			stbrach();
			}
			else {
				stnbrach();
			}
			break;
		case 39:
			if(this.getZeroFlag()==1)
			{
				stbrach();
			}
			else {
				stnbrach();
			}
			break;
		case 44://2C
			if((this.getNegativeFlag()==1)&&(getOverflowFlag())==1||((this.getNegativeFlag()==1)&&(this.getOverflowFlag())==0))
			{
				stbrach();
			}
			else {
				stnbrach();
			}
			break;
		case 46://2E
			if(((this.getZeroFlag()==0)&&(this.getOverflowFlag()==1)&&(this.getNegativeFlag()==1))||(this.getNegativeFlag()==0&&this.getOverflowFlag()==1))
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 34://22
			if(this.getCarryFlag()==0||this.getZeroFlag()==0)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 47://2F
			if((this.getZeroFlag()==1)||(this.getNegativeFlag()==1&&this.getOverflowFlag()==0)||(this.getNegativeFlag()==0&&this.getOverflowFlag()==0))
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 35://23
			if(this.getCarryFlag()==1||this.getOverflowFlag()==1)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 45://2D
			if((this.getNegativeFlag()==1&&this.getOverflowFlag()==0)||(this.getNegativeFlag()==0&&this.getOverflowFlag()==1))
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 43://2B
			if(this.getNegativeFlag()==1)
			{
				stbrach();
			}
			else
			{
				stnbrach();
				
			}
			break;
		case 38://26
			if(this.getZeroFlag()==0)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 42://2A
			if(this.getNegativeFlag()==0)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
			break;
		case 32://20
		   stbrach();
		   break;
		case 40://28
			if(this.getOverflowFlag()==0)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
		case 41://29
			if(this.getOverflowFlag()==1)
			{
				stbrach();
			}
			else
			{
				stnbrach();
			}
		}
		
	}
	private void stbrach()
	{
		this.PC=(short) (this.PC+2+ram.read(this.PC));
	}
	private void stnbrach(){
		this.PC+=2;
	}
	private void brs() {
		//store current pc first
		pcstack.push(this.PC);
		try {
			this.PC=(short) (ram.read(this.PC++)+this.PC);
		} catch (StackOverflowError es) {
			// TODO: handle exception
			System.out.println(es.toString());
		}
		
		// TODO Auto-generated method stub
		
	}
	private void rts() {
		// TODO Auto-generated method stub
		if (pcstack.empty()==false)
		{
			//not empty
			try {
				this.PC=pcstack.pop();
				short testOverLimited=(short) (this.PC+1);
				if(testOverLimited>0xc3fe)
				{
					System.out.println("Over system index limited");
				}
				else {
					this.PC+=1;
				}
				
			} catch (Exception es) {
				System.out.println(es.toString());
				// TODO: handle exception
			}
			
		}
		else {
		  System.out.println("Empty pc list exception!");
		}
	}
	//3 im
	//0 direct
	//1 extend
	//2 indirect
	private void cpx(int addr) {
		// TODO Auto-generated method stub
		  byte[] conData = new byte[2];
		  conData[0] = (byte) (this.X);//low 8
		  conData[1] = (byte) (this.X >>> 8);//high 8
		  byte result;
		  byte result2;
		
		if(addr==3)
		{
	      result=(byte) (conData[1]-ram.read(this.PC++));//compare high
		  result2=(byte) (conData[0]-ram.read(this.PC++));
		}
		else if(addr==0)
		{
			  result=(byte) (conData[1]-ram.read(ram.read(this.PC++)));//compare high
			  result2=(byte) (conData[0]-ram.read(ram.read(this.PC++)));
		}
		else if(addr==1)
		{
			
			 result=(byte) (conData[1]-ram.read(ram.read(this.PC++)));//compare high
			  result2=(byte) (conData[0]-ram.read(ram.read(this.PC++)));
		}
		else if(addr==2)
		{
			
		}
	}
	private void aba() {
		// TODO Auto-generated method stub
		byte result=(byte) (this.A+this.B);
		this.overflowFlag=(result>128|result<-127)==true?1:0;
		this.carryFlag=0;
		this.halfCarryFlag=0;
		this.interruptMask=0;
		this.zeroFlag=result==0?1:0;
		if(this.overflowFlag==0)//not overflow
		{
			this.negativeFlag=getbit(result,1)==1?1:0;
		}
		else
		{
			this.negativeFlag=0;
		}
		this.A=result;
		imm();
	}
	/*
	 * This is add function including 4 types addressing mode
	 */
	private void addA(int addr) {
	    int value = ram.read(addr);
	    int result = value + this.A;
	    this.carryFlag = result >> 8 != 0?1:0;
	    this.overflowFlag = (((((this.A ^ value) & 0x80) == 0) && (((this.A ^ result) & 0x80) != 0)))==true?1:0;
	    this.A = (byte) (result & 0xFF);
	    setflags(this.A);//flag cahange
	  }
	private void addB(int addr) {
	    int value = ram.read(addr);
	    int result = value + this.B;
	    this.carryFlag = (result >> 8 != 0?1:0);
	    this.overflowFlag = ((((this.B ^ value) & 0x80) == 0) && (((this.B ^ result) & 0x80) != 0))==true?1:0;
	    this.B = (byte) (result & 0xFF);
	    setflags(this.B);//flag change
	  }
	
	
	private void adcA(int addr) {
	    int value = ram.read(addr);

	    int result = value + this.A ;

	    this.carryFlag = (result >> 8 != 0?1:0);

	    this.overflowFlag = ((((this.A ^ value) & 0x80) == 0) && (((this.A ^ result) & 0x80) != 0))==true?1:0;
	    this.A = (byte) (result & 0xFF);
	    setflags(this.A);
	  }
	
	private void adcB(int addr) {
	    int value = ram.read(addr);

	    int result = value + this.B ;

	    this.carryFlag = (result >> 8 != 0?1:0);

	    this.overflowFlag = ((((this.B ^ value) & 0x80) == 0) && (((this.B ^ result) & 0x80) != 0))==true?1:0;
	    this.B = (byte) (result & 0xFF);
	    setflags(this.B);
	  }
	
	private void andA(int addr)
	  {
	    this.A &= ram.read(addr);
	    this.overflowFlag =0;
	    setflags(this.A);
	  }
	private void andB(int addr)
	  {
	    this.B &= ram.read(addr);
	    this.overflowFlag =0;
	    setflags(this.B);
	  }
	
	private void bit(int addr, int reg)
	  {
	    int data = ram.read(addr);
	    this.zeroFlag = ((data & reg) == 0)?1:0;
	    this.negativeFlag = getbit(data, 7);
	    this.overflowFlag = 0;
	  }
	
	private void clr(int addr)
	  {
		byte k=0;
		ram.write(addr,k);
		this.zeroFlag=1;
		this.negativeFlag=0;
		this.carryFlag=0;
		this.overflowFlag=0;
	 }
	 private void cmp(int regval, int addr)
	  {
	    int result = regval - ram.read(addr);
	    if (result < 0) {
	      this.negativeFlag = getbit(result, 7);
	      this.carryFlag = 0;
	      this.zeroFlag = 0;
	    } else if (result == 0) {
	      this.negativeFlag = 0;
	      this.carryFlag = 1;
	      this.zeroFlag = 1;
	    } else {
	      this.negativeFlag = getbit(result, 7);
	      this.carryFlag = 1;
	      this.zeroFlag = 0;
	    }
	  }
	
	 private void cba()
	  {
	    int result = this.A-this.B;
	    if (result < 0) {
	      this.negativeFlag = getbit(result, 7);
	      this.carryFlag = 0;
	      this.zeroFlag = 0;
	    } else if (result == 0) {
	      this.negativeFlag = 0;
	      this.carryFlag = 1;
	      this.zeroFlag = 1;
	    } else {
	      this.negativeFlag = getbit(result, 7);
	      this.carryFlag = 1;
	      this.zeroFlag = 0;
	    }
	  }
	 
	 private void com(int addr){
		 byte result=(byte) ((ram.read(addr))^255);
		ram.write(addr,result);
		 setflags(result);
		 this.carryFlag=1;
		 this.overflowFlag=0;
	}
	 
	 private void neg(int addr){
		 byte result=(byte) (((ram.read(addr))^255)+1);
			ram.write(addr,result);
			 setflags(result);
			 this.carryFlag=test12(result,128);
			 this.overflowFlag=test12(result,0);
		}
	 private void dec(int addr) {
		 byte tmp = (byte) ram.read(addr);
		    ram.write(addr, tmp);
		    tmp--;
		    tmp &= 255;
		    ram.write(addr, tmp);
		    setflags(tmp);
		  }
	 
	 private void eorA(int addr) {
		    this.A ^= ram.read(addr);
		    this.A &= 255;
		    setflags(this.A);
		    this.carryFlag=0;
		  }
	 private void eorB(int addr) {
		    this.B ^= ram.read(addr);
		    this.B &= 255;
		    setflags(this.B);
		    this.carryFlag=0;
		  }
	 private void inc(int addr)
	  {
		 byte tmp = (byte) ram.read(addr);
	    ram.write(addr, tmp);

	    tmp++;
	    tmp &= 255;
	    ram.write(addr, tmp);

	    setflags(tmp);
	  }
	 
	 private void ldaA(int addr) {
		    this.A = (byte) ram.read(addr);
		    setflags(this.A);
		    this.overflowFlag=0;		  
	 }

	 private void ldaB(int addr) {
		    this.B = (byte) ram.read(addr);
		    setflags(this.B);
		    this.overflowFlag=0;
		  }
	 
	 private void oraA(int addr) {
		    this.A |= ram.read(addr);
		    this.A  &= 255;
		    setflags( this.A);
		    this.overflowFlag=0;
		  }
	 private void oraB(int addr) {
		    this.B |= ram.read(addr);
		    this.B  &= 255;
		    setflags( this.B);
		    this.overflowFlag=0;
		  }
	 private void push(byte byteToPush) {
		    ram.write(256 + (this.SP & 0xFF), (byte) byteToPush);
		    this.SP -= 1;
		    this.SP &= 255;
		  }
	 private byte pull() {
		    this.SP += 1;
		    this.SP &= 127;
		    return (byte) ram.read(127 + this.SP);
		  }
/*Rotate left*/ 
	 
	 private void rol(int addr) {
		 byte data = (byte) ram.read(addr);
		    ram.write(addr, data);
		    data = (byte) (data << 1 | (this.carryFlag));
		    this.carryFlag =getbit(data, 8);
		    data &= 255;
		    setflags(data);
		    ram.write(addr, data);
		  }	
/*Rotate accumulator A Left*/
	 private void rolA() {
		    this.A = (byte) (this.A << 1 | (this.carryFlag));
		    this.carryFlag = getbit(this.A, 8);
		    this.A &= 255;
		    setflags(this.A);
		  }
	 /*Rotate accumulator B Left*/
	 private void rolB() {
		   this.B = (byte) (this.B << 1 | (this.carryFlag));
		    this.carryFlag = getbit(this.B, 8);
		    this.B &= 255;
		    setflags(this.B);
		  }
		  
/*Rotate Right*/		  
	private void ror(int addr) {
			    int data = ram.read(addr);
			    ram.write(addr, (byte) data);
			    int tmp = this.carryFlag;
			    this.carryFlag = getbit(data, 0);
			    data >>= 1;
			    data &= 127;
			    data |= (tmp==1 ? 128 : 0);
			    setflags(data);
			    ram.write(addr, (byte) data);
			  }
/*Rotate accumulator Right*/
	private void rorA() {
			    int tmp = this.carryFlag;
			    this.carryFlag = getbit(this.A, 0);
			    this.A >>= 1;
		        this.A &= 127;
		        this.A |= (tmp==1 ? 128 : 0);
			    setflags(this.A);
			  }
	private void rorB() {
		 int tmp = this.carryFlag;
		    this.carryFlag = getbit(this.B, 0);
		    this.B >>= 1;
	        this.B &= 127;
	        this.B |= (tmp==1 ? 128 : 0);
		    setflags(this.B);
	  }
/*Arithmetic shift left*/	
	
	private void asl(int addr) {
		byte data = (byte) ram.read(addr);
	    ram.write(addr, data);
	    this.carryFlag =getbit(data, 7);
	    data <<= 1;
	    data &= 255;
	    setflags(data);
	    ram.write(addr, data);
	  }
/*Arithmetic shift left accumulator A*/
	  private void aslA() {
	    this.carryFlag = getbit(this.A, 7);
	    this.A <<= 1;
	    this.A &= 255;
	    setflags(this.A);
	  }
/*Arithmetic shift left accumulator B*/
	  private void aslB() {
	    this.carryFlag = getbit(this.B, 7);
	    this.B <<= 1;
	    this.B &= 255;
	    setflags(this.B);
	  }
	  
	  /*Arithmetic shift right*/	
		
		private void asr(int addr) {
			byte data = (byte) ram.read(addr);
		    ram.write(addr, data);
		    this.carryFlag =getbit(data, 7);
		    data >>= 1;
		    data &= 255;
		    setflags(data);
		    ram.write(addr, data);
		  }
	/*Arithmetic shift right accumulator A*/
		  private void asrA() {
		    this.carryFlag = getbit(this.A, 7);
		    this.A >>= 1;
		    this.A &= 255;
		    setflags(this.A);
		  }
	/*Arithmetic shift left accumulator B*/
		  private void asrB() {
		    this.carryFlag = getbit(this.B, 7);
		    this.B >>= 1;
		    this.B &= 255;
		    setflags(this.B);
		  }
	  
	 /*logic shift Right*/
		  
	  private void lsr(int addr) {
		  byte data = (byte) ram.read(addr);
		    ram.write(addr, data);
		    this.carryFlag = getbit(data, 0);
		    data =(byte) ((data & 0xFF)>>> 1);
		    ram.write(addr, data);
		    setflags(data);
		  }

	  private void lsrA() {
		    this.carryFlag = getbit(this.A, 0);
		    this.A=(byte) ((this.A & 127)>>>1);
		    setflags(this.A);
		  }	 
	  private void lsrB() {
			    this.carryFlag = getbit(this.B, 0);
			    this.B=(byte) ((this.B & 127)>>>1);
			    setflags(this.B);
			  }	
	  
	  //store accumulator A
	  private void staA(int addr) {
		    ram.write(addr, this.A);
		    this.overflowFlag=0;
		  }
	  private void staB(int addr) {
		    ram.write(addr, this.B);
		    this.overflowFlag=0;
		  }
	  //Subtraction with carry
	  private void sbcA(int addr) {
		    int value = ram.read(addr);
		    int result = this.A - value - this.carryFlag;
		    this.carryFlag = (result >> 8 == 0?1:0);
		    this.overflowFlag = ((((this.A ^ value) & 0x80) != 0) && (((this.A ^ result) & 0x80) != 0))==true?1:0;
		    this.A = (byte) (result & 0xFF);
		    setflags(this.A);
		  }
	  private void sbcB(int addr) {
		    int value = ram.read(addr);

		    int result = this.B - value - this.carryFlag;
		    this.carryFlag = (result >> 8 == 0)?1:0;
		    this.overflowFlag = ((((this.B ^ value) & 0x80) != 0) && (((this.B ^ result) & 0x80) != 0))==true?1:0;
		    this.B = (byte) (result & 0xFF);
		    setflags(this.B);
		  }
	  private void subA(int addr) {
		    int value = ram.read(addr);
		    int result = value - this.A;
		    this.carryFlag = (result >> 8 == 0)?1:0;
		    this.overflowFlag = ((((this.A ^ value) & 0x80) != 0) && (((this.A ^ result) & 0x80) != 0))?1:0;
		    this.A = (byte) (result & 0xFF);
		    setflags(this.A);//flag change
		  }
	  private void subB(int addr) {
		    int value = ram.read(addr);
		    int result = value - this.B;
		    this.carryFlag = (result >> 8 == 0)?1:0;
		    this.overflowFlag = ((((this.B ^ value) & 0x80) != 0) && (((this.B ^ result) & 0x80) != 0))?1:0;
		    this.B = (byte) (result & 0xFF);
		    setflags(this.B);//flag change
		  }
	  private void tst(int addr) {
		    int value = ram.read(addr);
		    this.carryFlag = 0;
		    this.overflowFlag = 0;
		    setflags(value);//flag change
	  }
		
	  private void tstA() {
		    this.carryFlag = 0;
		    this.overflowFlag = 0;
		    this.PC++;
		    setflags(this.A);//flag change
	  }
	  private void tstB() {
		    this.carryFlag = 0;
		    this.overflowFlag = 0;
		    this.PC++;
		    setflags(this.B);//flag change
	  }
	  //0 for im
	  //1 for extended
	  //2 for indirected
	  private void stx(int addr) {
		  byte[] conData = new byte[2];
		  conData[0] = (byte) (this.X);//low 8
		  conData[1] = (byte) (this.X >>> 8);//high 8
		  
		   if(addr==0)
		   {
		    ram.write(this.PC++, conData[1]);
		    //addr++;
		    ram.write(this.PC++, conData[0]);
		    setflags(this.X);
		    this.overflowFlag=0;
		    this.negativeFlag = getbit(conData[1], 1);
		   }
		   else if(addr==1)
		   {
			  short address1=combinationByteToShort(ram.read(this.PC++), ram.read(this.PC++));
			  
			 ram.write(address1++, conData[1]);
			 ram.write(address1,conData[0]);
		   }
		   else if(addr==2)
		   {
			   //address overflow?
			   //address overflow?
			   byte indirecttmp=ram.read(this.PC++);//get indirect data first.
			   short storeaddressdata=(short) (this.X+indirecttmp);
			   indirecttmp=ram.read(this.PC++);
			   short storeaddressdata2=(short) (this.X+indirecttmp);
			   ram.write(storeaddressdata, conData[1]);
			   ram.write(storeaddressdata2, conData[0]);
			   
		   }
		  }
	//0 for im
	  //3 for direct
	  //1 for extended
	  //2 for indirected
	  private void ldx(int addr) {
		  byte[] conData = new byte[2];
		  //conData[0] = ram.read();//high8;
		  //conData[1] = (byte) (this.X >>> 8);//high 8
		  
		   if(addr==0)
		   {
		    conData[0]=ram.read(this.PC++);//high 8
		    //addr++;
		    conData[1]=ram.read(this.PC++);//low8
		  
		   }
		   else if(addr==3)
		   {
			 //ram.write(ram.read(this.PC++), conData[1]);
				 conData[0]=ram.read(ram.read(this.PC++));
				 //ram.write(ram.read(this.PC++),conData[0]);
				 conData[1]=ram.read(ram.read(this.PC++));
		   }
		   else if(addr==1)
		   {
			 //ram.write(ram.read(this.PC++), conData[1]);
				 conData[0]=ram.read(ram.read(this.PC++));
				 //ram.write(ram.read(this.PC++),conData[0]);
				 conData[1]=ram.read(ram.read(this.PC++));
				 short address1=combinationByteToShort(conData[0],conData[1]);//in put of 
				 short address2=(short) (address1+1);
				 conData[0]=ram.read(address1);
				 conData[0]=ram.read(address2); 
			 
		   }
		   else if(addr==2)
		   {
			   
			   byte indirecttmp=ram.read(this.PC++);//get indirect data first.
			   short storeaddressdata=(short) (this.X+indirecttmp);
			   indirecttmp=ram.read(this.PC++);
			   short storeaddressdata2=(short) (this.X+indirecttmp);
			   
			   conData[0]=ram.read(storeaddressdata);
			   conData[1]=ram.read(storeaddressdata2);///////////
			   
		   }
		   short k=conData[0];
			//k=(short) (0x00ff&k);
			k=(short) (k<<8);

			k=(short)(k+(conData[1]&0x00ff));
		    this.X=k;
		    setflags(this.X);
		    this.overflowFlag=0;
		    this.negativeFlag = getbit(conData[0], 1);
		  }
	//0 for im
	  //3 for direct
	  //1 for extended
	  //2 for indirected
	  private void lds(int addr) {
		  byte[] conData = new byte[2];
		  //conData[0] = ram.read();//high8;
		  //conData[1] = (byte) (this.X >>> 8);//high 8
		  
		   if(addr==0)
		   {
		    conData[0]=ram.read(this.PC++);//high 8
		    //addr++;
		    conData[1]=ram.read(this.PC++);//low8
		  
		   }
		   else if(addr==3)
		   {
			   conData[0]=ram.read(ram.read(this.PC++));
			   conData[1]=ram.read(ram.read(this.PC++));
		   }
		   else if(addr==1)
		   {
			 //ram.write(ram.read(this.PC++), conData[1]);
			 conData[0]=ram.read(ram.read(this.PC++));
			 //ram.write(ram.read(this.PC++),conData[0]);
			 conData[1]=ram.read(ram.read(this.PC++));
			 short address1=combinationByteToShort(conData[0],conData[1]);//in put of 
			 short address2=(short) (address1+1);
			 conData[0]=ram.read(address1);
			 conData[0]=ram.read(address2);
			 
		   }
		   else if(addr==2)
		   {
			   
			   byte indirecttmp=ram.read(this.PC++);//get indirect data first.
			   short storeaddressdata=(short) (this.SP+indirecttmp);
			   indirecttmp=ram.read(this.PC++);
			   short storeaddressdata2=(short) (this.SP+indirecttmp);
			   
			   conData[0]=ram.read(storeaddressdata);
			   conData[1]=ram.read(storeaddressdata2);///////////
			   
		   }
		   short k=conData[0];
			//k=(short) (0x00ff&k);
			k=(short) (k<<8);

			k=(short)(k+(conData[1]&0x00ff));
		    this.SP=k;
		    setflags(this.SP);
		    this.overflowFlag=0;
		    this.negativeFlag = getbit(conData[0], 1);
		  }
	  private byte[] divShortToByte(short orginal)
	  {
		  byte[] conData = new byte[2];
		  conData[0] = (byte) (orginal);//low 8
		  conData[1] = (byte) (orginal >>> 8);//high 8
		  return conData;
	  }
	  private short combinationByteToShort(byte high,byte low)
	  {
		   short k=high;
				//k=(short) (0x00ff&k);
				k=(short) (k<<8);

				k=(short)(k+(low&0x00ff));
				return k;
			    
	  }
	  private void sts(int addr) {
		    /*ram.write(addr, this.SP);
		    setflags(this.SP);
		    this.overflowFlag=0;
		    this.negativeFlag = getbit(this.SP, 15);*/
		  byte[] conData = new byte[2];
		  conData[0] = (byte) (this.SP);//low 8
		  conData[1] = (byte) (this.SP >>> 8);//high 8
		  
		   if(addr==0)
		   {
		    ram.write(this.PC++, conData[1]);
		    //addr++;
		    ram.write(this.PC++, conData[0]);
		    setflags(this.X);
		    this.overflowFlag=0;
		    this.negativeFlag = getbit(conData[1], 1);
		   }
		   else if(addr==1)
		   {
			   short address1=combinationByteToShort(ram.read(this.PC++), ram.read(this.PC++));
				  
				 ram.write(address1++, conData[1]);
				 ram.write(address1,conData[0]);
		   }
		   else if(addr==2)
		   {
			   //address overflow?
			   byte indirecttmp=ram.read(this.PC++);//get indirect data first.
			   short storeaddressdata=(short) (this.SP+indirecttmp);
			   indirecttmp=ram.read(this.PC++);
			   short storeaddressdata2=(short) (this.SP+indirecttmp);
			   ram.write(storeaddressdata, conData[1]);
			   ram.write(storeaddressdata2, conData[0]);
		   }
		  }
	  
	  private void branch(boolean condition) {
		    if (condition) {
		     this.PC = (byte) rel();
      
		    }
		    else {
		      this.PC=(byte) (this.PC+2);
		    }
		  }
	/*
	 * Immediate addressing mode
	 */
	 protected final int imm()
	  {
	    return this.PC++;
	  }	
     /*
      * Direct addressing mode
      */
	 protected final int direct()
	  {
		return ram.read(this.PC++);		
	  }	
	 
	 /*M=-M 
      * Indexed addressing mode
      */
	 protected final int index()
	  {
		//return ram.read(this.PC++ + this.X);		old solution
		 //new solution
		 return ram.read(ram.read(this.PC++)+this.X);
	  }
	 
	 /*
      * Extended addressing mode
      */
	 protected final int extend()
	  {
		   short k=ram.read(this.PC++);
			//k=(short) (0x00ff&k);
			k=(short) (k<<8);

			k=(short)(k+(ram.read(this.PC++)&0x00ff));
		 return k;	
	  }
	
	 protected final int rel()
	  {
	    return (byte)ram.read(this.PC++) + this.PC;
	  }
	 
	 
	 private void setflags(int result) {
		    this.zeroFlag = (result == 0)?1:0;
		    this.negativeFlag = getbit(result, 7);
			  }
	 
	
	 public static byte getbit(int num, int bitnum)
	  {
		return (byte)(num>>(8-bitnum)&0x01);
	  } 
	 
	 public static byte test12(int data, int vaule)
	  {
	    if((data!= vaule)==true)
	    {
	    	return 1;
	    }
	    else
	    {
	    	return 0;
	    }
	  } 
	 

}
