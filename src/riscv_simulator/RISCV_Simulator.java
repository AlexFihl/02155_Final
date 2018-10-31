package riscv_simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RISCV_Simulator {

	public static void main(String[] args) {

		Scanner programFile = getPrgramFile();
		System.out.println(programFile.next());
		CPU cpu1 = new CPU();

		cpu1.loadProgram(readProgram());

		for (;;) {
			int x = cpu1.oneStep();
			printRG(cpu1); // For testing purpose
			if (x == 1)
				break;
		}

	}

	private static Scanner getPrgramFile() {
		Scanner consoleReader = new Scanner(System.in);

		System.out.println("Which prgram do you want to load, type in the wanted number");
		File folder = new File("test");
		File[] listOfFiles = folder.listFiles();

		int i = 0;
		for (File f : listOfFiles) {
			System.out.print(i++ + ": " + f.getName() + " ; ");
		}
		System.out.println();
		while (true) {
			try {
				int x = getScannerInt(consoleReader);
				if (x < listOfFiles.length)
					return new Scanner(listOfFiles[x]);
				else
					System.out.println("This is not a valid number");
			} catch (FileNotFoundException e) {
				System.out.println("The file couldn't be found");
			}
		}
	}

	private static void printRG(CPU cpu1) {
		for (int i = 0; i < cpu1.getReg().length; ++i) {
			System.out.print(cpu1.getReg()[i] + " ");
		}
		System.out.println();
	}

	private static int[] readProgram() {
		int temp[] = { 0x00200093, // addi x1 x0 2
				0x00300113, // addi x2 x0 3
				0x002081b3, // add x3 x1 x2
		};
		return temp;
	}

	private static int getScannerInt(Scanner reader) {
		while (true) {
			try {
				return Integer.parseInt(reader.next());
			} catch (NumberFormatException e) {
				System.out.println("The input shall be only numbers and no letters");
			}
		}
	}
}
