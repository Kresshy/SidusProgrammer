package hu.sch.kresshy.sidusprogrammer;

import java.io.Serializable;

public class TimerProgram implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 5668851520451041277L;

	private double[] time;
	private int[][] servopos;
	private final String DELIMITER = "|";
	private final String DELIMITERCOLOUMNS = "#";

	public TimerProgram() {

		setTime(new double[11]);
		setServopos(new int[11][3]);

	}

	public double[] getTime() {
		return time;
	}

	public void setTime(double[] time) {
		this.time = time;
	}

	public int[][] getServopos() {
		return servopos;
	}

	public void setServopos(int[][] servopos) {
		this.servopos = servopos;
	}

	public void setOneServopos(int value, int row, int coloumn) {
		this.servopos[row - 1][coloumn - 1] = value;
	}

	public int getOneServopos(int row, int coloumn) {
		return this.servopos[row - 1][coloumn - 1];
	}

	public void setOneTime(double value, int timepos) {
		this.time[timepos - 1] = value;
	}

	public double getOneTime(int timepos) {
		return this.time[timepos - 1];
	}

	// public void setTimerProgram(String valueString){
	//
	// String[] coloumns = valueString.split(DELIMITERCOLOUMNS);
	// String[] time = coloumns[0].split(DELIMITER);
	// String[] servo1 = coloumns[1].split(DELIMITER);
	// String[] servo2 = coloumns[2].split(DELIMITER);
	// String[] servo3 = coloumns[3].split(DELIMITER);
	//
	// //TODO implementing method
	//
	// }

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(time[0]).append(DELIMITER).append(time[1]).append(DELIMITER)
				.append(time[2]).append(DELIMITER).append(time[3])
				.append(DELIMITER).append(time[4]).append(DELIMITER)
				.append(time[5]).append(DELIMITER).append(time[6])
				.append(DELIMITER).append(time[7]).append(DELIMITER)
				.append(time[8]).append(DELIMITER).append(time[9])
				.append(DELIMITER).append(time[10]).append(DELIMITERCOLOUMNS)
				.append(servopos[0][0]).append(DELIMITER)
				.append(servopos[1][0]).append(DELIMITER)
				.append(servopos[2][0]).append(DELIMITER)
				.append(servopos[3][0]).append(DELIMITER)
				.append(servopos[4][0]).append(DELIMITER)
				.append(servopos[5][0]).append(DELIMITER)
				.append(servopos[6][0]).append(DELIMITER)
				.append(servopos[7][0]).append(DELIMITER)
				.append(servopos[8][0]).append(DELIMITER)
				.append(servopos[9][0]).append(DELIMITER)
				.append(servopos[10][0]).append(DELIMITERCOLOUMNS)
				.append(servopos[0][1]).append(DELIMITER)
				.append(servopos[1][1]).append(DELIMITER)
				.append(servopos[2][1]).append(DELIMITER)
				.append(servopos[3][1]).append(DELIMITER)
				.append(servopos[4][1]).append(DELIMITER)
				.append(servopos[5][1]).append(DELIMITER)
				.append(servopos[6][1]).append(DELIMITER)
				.append(servopos[7][1]).append(DELIMITER)
				.append(servopos[8][1]).append(DELIMITER)
				.append(servopos[9][1]).append(DELIMITER)
				.append(servopos[10][1]).append(DELIMITERCOLOUMNS)
				.append(servopos[0][2]).append(DELIMITER)
				.append(servopos[1][2]).append(DELIMITER)
				.append(servopos[2][2]).append(DELIMITER)
				.append(servopos[3][2]).append(DELIMITER)
				.append(servopos[4][2]).append(DELIMITER)
				.append(servopos[5][2]).append(DELIMITER)
				.append(servopos[6][2]).append(DELIMITER)
				.append(servopos[7][2]).append(DELIMITER)
				.append(servopos[8][2]).append(DELIMITER)
				.append(servopos[9][2]).append(DELIMITER)
				.append(servopos[10][2]);

		return sb.toString();
	}

}
