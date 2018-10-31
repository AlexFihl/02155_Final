package riscv_simulator;

public class CPU {
	
	private int pc;
	private int reg[] = new int[32];
	private int program[];
	
	private int instruction;
	private int opcode;
	private int rd;
	private int funt3;
	private int rs1;
	private int rs2;
	private int funt7;
	private int imm;
	
	public CPU() {
		
	}
	
	public int oneStep() {
		instruction = program[pc];
		opcode = instruction & 0x7f;
		rd = (instruction >> 7) & 0x1f;
		funt3 = (instruction >> 12) & 0x7;
		rs1 = (instruction >> 15) & 0x1f;
		rs2 = (instruction >> 20) & 0x1f;
		funt7 = instruction >> 25;
		imm = instruction >> 20;
		
		switch(opcode) {
		case 0x13:
			opCode0x13();
			break;
		case 0x33:
			opCode0x33();
			break;
		default:
			System.out.println("Opcode " + opcode + " not yet implemented");
			break;
		}
		
		pc++;
		if(pc >= program.length)
			return 1;
		return 0;
	}

	private void opCode0x13() {
		reg[rd] = reg[rs1] + imm;
	}

	private void opCode0x33() {
		switch(funt3) {
		case 0x0:
			if(funt7 == 0x00)
				reg[rd] = reg[rs1] + reg[rs2]; //add
			else
				reg[rd] = reg[rs1] - reg[rs2]; //sub
			break;
		case 0x1:
			reg[rd] = reg[rs1] << reg[rs2]; //sll
			break;
		case 0x2:
			reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0; //slt
			break;
		case 0x3:
			reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0; //sltu
			break;
		case 0x4:
			reg[rd] = reg[rs1] ^ reg[rs2]; //xor
			break;
		case 0x5:
			if(funt7 == 0x00)
				reg[rd] = reg[rs1] >> reg[rs2]; //srl
			else
				reg[rd] = reg[rs1] >> reg[rs2]; //sra
			break;
		case 0x6:
			reg[rd] = reg[rs1] | reg[rs2]; //or;
			break;
		case 0x7:
			reg[rd] = reg[rs1] & reg[rs2]; //and
		}
	}

	public void loadProgram(int[] readProgram) {
		program = readProgram;
	}

	public int[] getReg() {
		return reg;
	}
}
