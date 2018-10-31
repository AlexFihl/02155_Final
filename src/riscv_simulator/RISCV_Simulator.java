package riscv_simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class RISCV_Simulator {

	public static void main(String[] args) {

		byte[] programFile = getPrgramFile();
		int[] programLines = new int[programFile.length / 4];
		for (int i = 0; i < programFile.length / 4; i++)
			for (int x = 0; x <= 3; x++)
				programLines[i] += (programFile[i * 4 + x] & 0xff) << 8 * x;
		System.out.println();

		CPU cpu1 = new CPU();
		cpu1.loadProgram(programLines);

		for (;;) {
			int x = cpu1.oneStep();
			printRG(cpu1); // For testing purpose
			if (x == 1)
				break;
		}

	}

	private static byte[] getPrgramFile() {
		Scanner consoleReader = new Scanner(System.in);

		System.out.println("Which prgram do you want to load, type in the wanted number");
		File folder = new File("test");
		File[] listOfFiles = folder.listFiles();

		int i = 0;
		for (File f : listOfFiles)
			System.out.print(i++ + ": " + f.getName() + " ; ");

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
		for (int i = 0; i < cpu1.getReg().length; ++i) {
			System.out.print(cpu1.getReg()[i] + " ");
		}
		System.out.println();
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
