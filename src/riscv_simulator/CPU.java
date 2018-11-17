package riscv_simulator;

public class CPU {

	private int pc;
	private int reg[] = new int[32];
	private int memory[] = new int[0xffff];
	private int program[];

	private int instruction;
	private int opcode;
	private int rd;
	private int funt3;
	private int rs1;
	private int rs2;
	private int funt7;
	private int imm;

	private int exit;
	private boolean jump;

	public CPU() {
		exit = -1;
		jump = false;
		reg[2] = memory.length - 1;
	}

	public boolean oneStep() {
		instruction = program[pc];
		opcode = instruction & 0x7f;
		rd = (instruction >> 7) & 0x1f;
		funt3 = (instruction >> 12) & 0x7;
		rs1 = (instruction >> 15) & 0x1f;
		rs2 = (instruction >> 20) & 0x1f;
		funt7 = (instruction >> 25) & 0x7f;
		
		switch (opcode) {
		case 0x03:
			opCode0x03();
			break;
		case 0x13:
			opCode0x13();
			break;
		case 0x33:
			opCode0x33();
			break;
		case 0x37:
			reg[rd] = instruction & (0xfffff << 12);
			break;
		case 0x63:
			opCode0x63();
			break;
		case 0x73:
			opCode0x73();
			break;
		default:
			printOpCode();
			break;
		}
		if (!jump)
			pc++;
		else
			jump = false;

		if (pc >= program.length || exit != -1)
			return false;
		else
			return true;
	}
	
	private void opCode0x03() {
		imm = instruction >> 20;
		switch(funt3) {
		case 0x0: //LB
			reg[rd] = (memory[rs1 << imm] & 0x00ff) + (memory[rs1 << imm] >> 32);
			break;
		case 0x1: //LH
			reg[rd] = (memory[rs1 << imm] & 0xffff) + (memory[rs1 << imm] >> 32);
			break;
		case 0x2: //LW
			reg[rd] = memory[rs1 << imm];
			break;
		case 0x3: //LBU
			reg[rd] = memory[rs1 << imm] & 0x00ff;
			break;
		case 0x4: //LHU
			reg[rd] = memory[rs1 << imm] & 0xffff;
			break;
		}
	}

	private void opCode0x63() {
		imm = (((instruction >> 8) & 0x0f) << 1) + (((instruction >> 25) & 0x3f) << 5)
				+ (((instruction >> 7) & 0x01) << 11) + ((instruction >> 31) << 12);
		switch (funt3) {
		case 0x0: // BEQ
			if (reg[rs1] == reg[rs2])
				jumpPcByImm();
			break;
		case 0x1: // BNE
			if (reg[rs1] != reg[rs2])
				jumpPcByImm();
			break;
		case 0x4: // BLT
			if (reg[rs1] < reg[rs2])
				jumpPcByImm();
			break;
		case 0x5: // BGE
			if (reg[rs1] >= reg[rs2])
				jumpPcByImm();
			break;
		case 0x6: // BLTU
			if ((reg[rs1] < reg[rs2]) ^ (reg[rs1] < 0) ^ (reg[rs2] < 0))
				jumpPcByImm();
			break;
		case 0x7: // BGEU
			if (!((reg[rs1] < reg[rs2]) ^ (reg[rs1] < 0) ^ (reg[rs2] < 0)))
				jumpPcByImm();
			break;
		}
	}

	private void jumpPcByImm() {
		pc += (imm / 4);
		jump = true;
	}

	private void printOpCode() {
		System.out.println("PC: " + pc + ". Opcode " + String.format("0x%01X", opcode) + " not yet implemented");
	}

	private void opCode0x73() {
		switch (funt3) {
		case 0x0: // ecall
			switch (reg[10]) {
			case 0x01:
				System.out.println(reg[11]);
				break;
			case 0x04:
				break;
			case 0x09:
				break;
			case 0x0a:
				exit = reg[10];
				break;
			case 0x0b:// Print out ASCII signed
				break;
			case 0x11:
				exit = reg[10];
				break;
			}
			break;
		default:
			printFunct3();
			break;
		}
	}

	private void printFunct3() {
		System.out.println("PC: " + pc + ". funt3 " + String.format("0x%01X", funt3) + " not yet implemented");
	}

	private void opCode0x13() {
		imm = instruction >> 20;
			reg[rd] = 0;
		switch (funt3) {
		case 0x0: // Addi
			reg[rd] = reg[rs1] + imm;
			break;
		case 0x1: // SLLI
			reg[rd] = reg[rs1] << (imm & 0x1f);
			break;
		case 0x2: // SLTI
			reg[rd] = reg[rs1] < imm ? 1 : 0;
			break;
		case 0x3: // SLTIU
			reg[rd] = ((reg[rs1] < imm) ^ (reg[rs1] < 0) ^ (imm < 0)) ? 1 : 0;
			break;
		case 0x4: // XORI
			reg[rd] = reg[rs1] ^ imm;
			break;
		case 0x5:
			if ((imm >>> 7) == 0x00) // SRLI
				reg[rd] = reg[rs1] >>> (imm & 0x1f);
			else // SRAI
				reg[rd] = reg[rs1] >> (imm & 0x1f);
			break;
		case 0x6: // ORI
			reg[rd] = reg[rs1] | imm;
			break;
		case 0x7: // ANDI
			reg[rd] = reg[rs1] & imm;
			break;
		}

	}

	private void opCode0x33() {
		switch (funt3) {
		case 0x0:
			if (funt7 == 0x00) // add
				reg[rd] = reg[rs1] + reg[rs2];
			else // sub
				reg[rd] = reg[rs1] - reg[rs2];
			break;
		case 0x1: // sll
			reg[rd] = reg[rs1] << (reg[rs2] & 0x1f);
			break;
		case 0x2: // slt
			reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0;
			break;
		case 0x3: // sltu
			reg[rd] = ((reg[rs1] < reg[rs2]) ^ (reg[rs1] < 0) ^ (reg[rs2] < 0)) ? 1 : 0;
			break;
		case 0x4:// xor
			reg[rd] = reg[rs1] ^ reg[rs2];
			break;
		case 0x5:
			if (funt7 == 0x00) // srl
				reg[rd] = reg[rs1] >>> (reg[rs2] & 0x1f);
			else // sra
				reg[rd] = reg[rs1] >> (reg[rs2] & 0x1f);
			break;
		case 0x6: // or
			reg[rd] = reg[rs1] | reg[rs2];
			break;
		case 0x7: // and
			reg[rd] = reg[rs1] & reg[rs2];
		default:
			printFunct3();
			break;
		}
	}

	public void loadProgram(int[] program) {
		this.program = program;
	}

	public int[] getReg() {
		return reg;
	}

	public int getExit() {
		return exit;
	}
	
	public int getPC() {
		return pc;
	}
}
