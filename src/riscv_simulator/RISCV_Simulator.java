package riscv_simulator;

import java.util.Scanner;

public class RISCV_Simulator {
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		
		CPU cpu1 = new CPU();
		
		cpu1.loadProgram(readProgram());
		
		for(;;)
		{
			int x = cpu1.oneStep();
			printRG(cpu1); //For testing purpose
			if(x == 1)
				break;
		}
		
		
		
		
			
		}

	private static void printRG(CPU cpu1) {
		for(int i = 0; i < cpu1.getReg().length; ++i)
		{
			System.out.print(cpu1.getReg()[i] + " ");
		}
		System.out.println();
	}
	
	
	private static int[] readProgram() {
		int temp[] = {
				0x00200093, // addi x1 x0 2
				0x00300113, // addi x2 x0 3
				0x002081b3, // add x3 x1 x2
		};
		return temp;
	}
}
