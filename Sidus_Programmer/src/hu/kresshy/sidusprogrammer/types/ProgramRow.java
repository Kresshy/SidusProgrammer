package hu.kresshy.sidusprogrammer.types;

/* 
 * Author: Szabolcs Varadi
 * 
 * Class for a single row in the TimerProgram 
 * The TimerProgram class uses it as an ArrayList<ProgramRow> 
 */

public class ProgramRow {

	// number of row
	public int num;
	
	// time of execution
	public float time;
	
	// servo positions
	public byte servo1;
	public byte servo2;
	public byte servo3;
	
	// engine value for f1q or f1c?
	public float engine;

	// constructors for setting the values of this row
	public ProgramRow() {
		this.num = 0;
		this.time = 0;
		this.servo1 = 0;
		this.servo2 = 0;
		this.servo3 = 0;
		this.engine = 0;
	}

	public ProgramRow(int num, float time, byte servo1, byte servo2, byte servo3) {
		this.num = num;
		this.time = time;
		this.servo1 = servo1;
		this.servo2 = servo2;
		this.servo3 = servo3;
		this.engine = 0;
	}

	public ProgramRow(int num, float time, byte servo1, byte servo2, byte servo3, float engine) {
		this.num = num;
		this.time = time;
		this.servo1 = servo1;
		this.servo2 = servo2;
		this.servo3 = servo3;
		this.engine = engine;
	}
	
	// engine value not returned yet
	@Override
	public String toString() {
		return num + "\t" + time + "\t" + servo1 + "\t" + servo2 + "\t" + servo3;
	}
}
