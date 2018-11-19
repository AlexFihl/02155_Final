package riscv_simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class RISCV_Simulator {

	static boolean debug;;

	public static void main(String[] args) {

		Scanner consoleReader = new Scanner(System.in);
		debug = false;

		System.out.println("Welcome to an RISC-V simulator \n" + "Make by Alex and Anders");
		while (true) {
			System.out.print("1: Run a program\n" + "2: Debuging\n" + "3: Exit \n");
			int number = getScannerInt(consoleReader);
			if (number == 1)
				runProgram(consoleReader);
			else if (number == 2)
				debug = !debug;
			else
				break;
		}
		System.out.println("Thank you for using the simulartor");
	}

	private static void runProgram(Scanner consoleReader) {

		while (true) {
			int[] programLines = getTheProgramFromAFile(consoleReader);
			CPU cpu = new CPU();
			cpu.loadProgram(programLines);

			boolean nextStep = true;
			while (nextStep) {
				nextStep = cpu.oneStep();
				if (debug)
					printRG(cpu); // For testing purpose
			}

			System.out.println("The content of the registers was:\n");
			int[] reg = cpu.getReg();
			for (int i = 0; i < reg.length; i++)
				System.out.println("x" + String.format("%02d", i) + ": " + String.format("0x%08X", reg[i]));
			System.out.println();

			if (debug)
				System.out.println("Exit code was: " + cpu.getExit());

			System.out.println("Do you want to run another program? (Y/n):");
			if (getScannerString(consoleReader).toLowerCase().equals("n")) {
				break;
			}
		}
	}

	private static int[] getTheProgramFromAFile(Scanner consoleReader) {
		byte[] programFile = getPrgramFile(consoleReader);
		int[] programLines = new int[programFile.length / 4];
		for (int i = 0; i < programFile.length / 4; i++)
			for (int x = 0; x <= 3; x++)
				programLines[i] += (programFile[i * 4 + x] & 0xff) << 8 * x;
		return programLines;
	}

	private static byte[] getPrgramFile(Scanner consoleReader) {

		System.out.println("Which prgram do you want to load, type in the wanted number");
		File folder = new File("test");
		File[] listOfFiles = folder.listFiles();

		int i = 0;
		for (File f : listOfFiles)
			System.out.print(i++ + ": " + f.getName() + "; ");

		System.out.println();
		while (true) {
			try {
				int x = getScannerInt(consoleReader);
				if (x < listOfFiles.length)
					return Files.readAllBytes(listOfFiles[x].toPath());
				else
					System.out.println("This is not a valid number");
			} catch (FileNotFoundException e) {
				System.out.println("The file couldn't be found");
			} catch (IOException e) {

			}
		}
	}

	private static void printRG(CPU cpu1) {
		System.out.print("After PC: " + cpu1.getOldPC() + " ");
		for (int i = 0; i < cpu1.getReg().length; ++i) {
			System.out.print(String.format("0x%08X", cpu1.getReg()[i]) + " ");
		}
		System.out.println();
	}

	private static int getScannerInt(Scanner consoleReader) {
		while (true) {
			try {
				return Integer.parseInt(getScannerString(consoleReader));
			} catch (NumberFormatException e) {
				System.out.println("The input shall be only numbers and no letters");
			}
		}
	}

	private static String getScannerString(Scanner consoleReader) {
		String text = consoleReader.nextLine();
		return text;
	}
}
