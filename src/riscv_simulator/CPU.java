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

		switch (opcode) {
		case 0x13:
			opCode0x13();
			break;
		case 0x33:
			opCode0x33();
			break;
//		case 0x73:
//			opCode0x73();
//			break;
		default:
			System.out.println("Opcode " + String.format("0x%01X", opcode) + " not yet implemented");
			break;
		}

		pc++;
		if (pc >= program.length)
			return 1;
		else
			return 0;
	}

	private void opCode0x73() {
		switch (funt3) {
		case 0x00: // ecall
			break;
		default:
			System.out.println("funt3 " + String.format("0x%01X", funt3) + " not yet implemented");
			break;
		}
	}

	private void opCode0x13() {
		reg[rd] = reg[rs1] + imm;
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
			reg[rd] = reg[rs1] << reg[rs2];
			break;
		case 0x2: // slt
			reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0;
			break;
		case 0x3: // sltu
			reg[rd] = reg[rs1] < reg[rs2] ? 1 : 0;
			break;
		case 0x4:// xor
			reg[rd] = reg[rs1] ^ reg[rs2];
			break;
		case 0x5:
			if (funt7 == 0x00) // srl
				reg[rd] = reg[rs1] >> reg[rs2];
			else // sra
				reg[rd] = reg[rs1] >> reg[rs2];
			break;
		case 0x6: // or
			reg[rd] = reg[rs1] | reg[rs2];
			break;
		case 0x7: // and
			reg[rd] = reg[rs1] & reg[rs2];
		default:
			System.out.println("funt3 " + String.format("0x%01X", funt3) + " not yet implemented");
			break;
		}
	}

	public void loadProgram(int[] program) {
		this.program = program;
	}

	public int[] getReg() {
		return reg;
	}
}
