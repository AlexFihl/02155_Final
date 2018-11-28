package riscv_simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RISCV_Simulator {

	static boolean debug;;

	public static void main(String[] args) {

		Scanner consoleReader = new Scanner(System.in);
		debug = false;

		System.out.println("Welcome to a RISC-V simulator \n" + "Make by Alex and Anders");
		while (true) {
			System.out.print("1: Run a program\n" + "2: Debugging\n" + "3: Exit \n");
			int number = getScannerInt(consoleReader);
			if (number == 1)
				runProgram(consoleReader);
			else if (number == 2)
				debug = !debug;
			else
				break;
		}
		System.out.println("Thank you for using the simulator");
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
				System.out.println("Exit code was: " + cpu.getExitCode());

			System.out.println("Do you want to save the registers to a file? (Y/n):");
			if (!getScannerString(consoleReader).toLowerCase().equals("n")) {
				System.out.println("Write name of the output file: ");
				String nameOfOutputFile = getScannerString(consoleReader);
				printRegistersToFile(cpu, nameOfOutputFile);
			}

			System.out.println("Do you want to run another program? (Y/n):");
			if (getScannerString(consoleReader).toLowerCase().equals("n"))
				break;
		}
	}

	private static int[] getTheProgramFromAFile(Scanner consoleReader) {
		byte[] programFile = getProgramFile(consoleReader);
		int[] programLines = new int[programFile.length / 4];
		for (int i = 0; i < programFile.length / 4; i++)
			for (int x = 0; x <= 3; x++)
				programLines[i] += (programFile[i * 4 + x] & 0xff) << 8 * x;
		return programLines;
	}

	private static byte[] getProgramFile(Scanner consoleReader) {

		System.out.println("Which program do you want to load, type in the wanted number");
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
					System.out.println("Please enter a valid program number!");
			} catch (FileNotFoundException e) {
				System.out.println("The file couldn't be found");
			} catch (IOException e) {

			}
		}
	}

	private static void printRG(CPU cpu1) {
		System.out.print("After PC: " + cpu1.getOldPC()/4 + " ");
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
				System.out.println("The input must only be numbers and no letters");
			}
		}
	}

	private static String getScannerString(Scanner consoleReader) {
		while (true) {
			String text = consoleReader.nextLine();
			if(text.length() != 0)
				return text;
			else
				System.out.println("Please input a valid string");
		}
	}

	private static void printRegistersToFile(CPU cpu, String nameOfOutputFile) {
		byte data[] = new byte[cpu.getReg().length * 4];
		for (int i = 0; i < 32; i++)
			for (int x = 0; x < 4; x++)
				data[i*4 + x] = (byte) ((cpu.getReg()[i] >> 8 * x) & 0xff);
		Path file = Paths.get(nameOfOutputFile + ".res");
		try {
			Files.write(file, data);
		} catch (IOException e) {
			System.out.println("File was not saved");
		}
	}
}
