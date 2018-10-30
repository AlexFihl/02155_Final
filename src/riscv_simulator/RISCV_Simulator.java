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
				switch(funt7) {
				case 0x00:
					reg[rd] = reg[rs1] + reg[rs2];
					break;
				case 0x20:
					reg[rd] = reg[rs1] - reg[rs2];
					break;
				default:
					System.out.println("Funt7 " + funt7 + " not yet implemented");
					break;
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
