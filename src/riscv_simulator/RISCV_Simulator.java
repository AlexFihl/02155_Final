package riscv_simulator;

public class RISCV_Simulator {
	static int pc;
	static int reg[] = new int[32];
	static int program[];
	
	public static void main(String[] args) {
		pc = 0;
		reg[0] = 0;
		readProgram();
		
		for(;;) {
			int instruction = program[pc];
			int opcode = instruction & 0x7f;
			int rd = (instruction >> 7) & 0x1f;
			int funt3 = (instruction >> 12) & 0x7;
			int rs1 = (instruction >> 15) & 0x1f;
			int rs2 = (instruction >> 20) & 0x1f;
			int funt7 = instruction >> 25;
			int imm = instruction >> 20;
			
			switch(opcode) {
			case 0x13:
				reg[rd] = reg[rs1] + imm;
				break;
			case 0x33:
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
				break;
			default:
				System.out.println("Opcode " + opcode + " not yet implemented");
				break;
			}
			
			pc++;
			for(int i = 0; i < reg.length; ++i)
			{
				System.out.print(reg[i] + " ");
			}
			System.out.println();
			if(pc >= program.length)
				break;
		}
	}
	
	
	private static void readProgram() {
		int temp[] = {
				0x00200093, // addi x1 x0 2
				0x00300113, // addi x2 x0 3
				0x002081b3, // add x3 x1 x2
		};
		program = temp;
	}
}
