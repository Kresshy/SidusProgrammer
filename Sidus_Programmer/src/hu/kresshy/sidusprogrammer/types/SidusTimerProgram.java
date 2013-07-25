package hu.kresshy.sidusprogrammer.types;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Szabolcs Varadi
 * 
 * This class represents the program of the timer.
 * Always create with category and MAX_ELEMENTS!
 * Automatically generates the program rows by the number of MAX_ELEMENTS.
 * 
 * 
 */

public class SidusTimerProgram {

	// maximum elements in the program
	private int MAX_ELEMENTS;
	private int SERVO_NUMBER;
	
	// supported categories
	public final static int F1A = 1;
	public final static int F1B = 2;
	public final static int F1C = 3;
	public final static int F1Q = 4;

	// F1A, F1B, F1C, F1Q
	private int FAICategory;

	// this List stores ProgramRows
	private List<SidusProgramRow> program;

	public SidusTimerProgram(int maxElements, int category, int servoNumber) {
		this.MAX_ELEMENTS = maxElements;
		this.FAICategory = category;
		this.SERVO_NUMBER = servoNumber;
		
		program = new ArrayList<SidusProgramRow>();

		generateProgramRows();
	}

	private void generateProgramRows() {
		for (int i = 0; i < MAX_ELEMENTS; i++) {
			program.add(new SidusProgramRow());
		}
	}

	public int getMaxElements() {
		return MAX_ELEMENTS;
	}

	public void setMaxElements(int maxElements) {
		this.MAX_ELEMENTS = maxElements;
	}

	public int getFAICategory() {
		return FAICategory;
	}

	public void setFAICategory(int category) {
		this.FAICategory = category;
	}

	public SidusProgramRow getProgramRowAt(int index) {
		if (program.size() > index) {
			return program.get(index);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sidus Timer Program for category: ");
		
		switch (FAICategory) {
		case 1:
			sb.append("F1A");
			break;
		
		case 2:
			sb.append("F1B");
			break;
		
		case 3:
			sb.append("F1C");
			break;
		
		case 4:
			sb.append("F1Q");
			break;
			
		default:
			sb.append("Unknown");
			break;
		}
		
		sb.append("\n");
		sb.append("-------------------------------------------\n");
		sb.append("#\ttime\tservo_1\tservo_2\tservo_3\n");
		
		for(int i = 0; i < MAX_ELEMENTS; i++) {
			SidusProgramRow pRow = program.get(i);
			sb.append(pRow.toString());
		}
		
		sb.append("-------------------------------------------\n");
		sb.append("copyright 2013 Sidus Programmer for Android\n");
		
		return sb.toString();
	}

	public int getSERVO_NUMBER() {
		return SERVO_NUMBER;
	}

	public void setSERVO_NUMBER(int SERVO_NUMBER) {
		SERVO_NUMBER = SERVO_NUMBER;
	}

}
