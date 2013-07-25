package hu.kresshy.sidusprogrammer.types;

public class SidusCommandHelper {

	// GENERAL FORMAT
	// |PREAMBLE| SYNC | LENGTH | COMMAND | DATA | CHECKSUM | TERMINATOR |
	// | 0x00 |0x00 0x00 0x00 0x00| 1 BYTE | 1 BYTE | (VARIABLE LENGTH) | 1 BYTE
	// | 0x45 |
	//
	// LENGHT: total number of bytes except the PREAMBLE, SYNC and TERMINATOR
	// fields CHECKSUM: Sum MODULO 256 of all bytes including COMMAND, DATA
	//
	// The DATA fields are filled with 0x00, always change when packing the
	// command before send, checksum is 0x00 always compute!

	// get software version
	private final static byte[] CMD_GETSWVER = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6B, 0x6B,
			0x45 };
	// reply to get software version (COMMAND = 0x6C, CATEGORY: 0x42 = B, 0x51 =
	// Q, 0x45 = E, SWVERSION 0x35 0x33 0x36 = 536, FUNC_NUM = 0x0A, MEM = 0x04,
	// SERV_NUM = 0x03
	private final static byte[] RSP_GETSWVER = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x0C, 0x6C, 0x00,
			0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x20, 0x00, 0x45 };

	// request time values
	private final static byte[] CMD_GETTIMERS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x61, 0x61,
			0x45 };
	// reply to reaquest time values NOTE!: Values are come as little-endian, 1
	// time is 2 bytes
	private final static byte[] RSP_GETTIMERS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x19, 0x66, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x45 };

	// send new time values NOTE!: watch for little-endian notation, 1 time is 2
	// bytes
	private final static byte[] CMD_SETTIMERS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x19, 0x65, 0x61,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x45 };
	// reply to send new time values ?? SAME COMMAND AS RSP_GETTIMERS ?? TODO
	private final static byte[] RSP_SETTIMERS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x19, 0x66, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x45 };

	// send new servo position (function, servo, position) !servo= 0, 1, 2;
	// !function = 0, ...., 0A; !position = 0, ...,82 (130 steps in decimal)
	private final static byte[] CMD_SETSERVO = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x63, 0x00,
			0x00, 0x00, 0x00, 0x45 };
	// reply to send new servo position
	private final static byte[] RSP_SETSERVO = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x64, 0x64,
			0x45 };

	// request servo positions
	private final static byte[] CMD_GETSERVOPOS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x67, 0x67,
			0x45 };
	// reply to request servo positions NF + 1 servo positions NF: Number of
	// Functions on timer
	private final static byte[] RSP_GETSERVOPOS = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x68, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x45 };

	// move servos to function
	private final static byte[] CMD_MOVETOFUNC = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x69, 0x00,
			0x00, 0x45 };
	// reply to move servos to function
	private final static byte[] RSP_MOVETOFUNC = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6A, 0x6A,
			0x45 };

	// request enable motor
	private final static byte[] CMD_UNLOCK = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6D, 0x6D, 0x45 };
	// reply to request enable motor
	private final static byte[] RSP_UNLOCK = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6A, 0x6A, 0x45 };

	// request jump to function after early DT ?? to which function TODO
	private final static byte[] CMD_JUMP2 = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x6F, 0x6F, 0x45 };
	// reply to jump to function after early DT
	private final static byte[] RSP_JUMP2 = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x70, 0x70, 0x45 };

	// is this correct ? request current memory index
	private final static byte[] CMD_GETTMEMORY = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x71, 0x71,
			0x45 };
	// reply to request current memory index ?? which index TODO
	private final static byte[] RSP_GETTMEMORY = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x72, 0x72,
			0x45 };

	// set current memory index (memory, chk)
	private final static byte[] CMD_SETTMEMORY = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x73, 0x00,
			0x00, 0x45 };
	// reply to set current memory index
	private final static byte[] RSP_SETTMEMORY = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x74, 0x74,
			0x45 };

	// request idle throttle
	private final static byte[] CMD_GETLOWRPM = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x75, 0x75,
			0x45 };
	// reply to request idle throttle (rpm, chk)
	private final static byte[] RSP_GETLOWRPM = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x76, 0x00,
			0x00, 0x45 };

	// set idle throttle (rpm, chk)
	private final static byte[] CMD_SETLOWRPM = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x77, 0x00,
			0x00, 0x45 };
	// reply to set idle throttle
	private final static byte[] RSP_SETLOWRPM = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x78, 0x78,
			0x45 };

	private SidusTimerProgram sidusTimerProgram;

	public SidusCommandHelper(SidusTimerProgram sidusTimerProgram) {
		this.sidusTimerProgram = sidusTimerProgram;
	}

	public byte[] prepareCommand(byte[] command, byte function, byte servo, byte position_value,
			byte memory, byte rpm) {

		byte[] commandToPrepare = null;

		// if any of the argument values are bad quit
		if (function > sidusTimerProgram.getMaxElements()
				|| servo > sidusTimerProgram.getSERVO_NUMBER() || position_value > 130
				|| memory > 4 || rpm > 100) {
			return commandToPrepare;
		}

		// prepare the command to send
		switch (command[6]) {
		case 0x6B:
			commandToPrepare = CMD_GETSWVER;
			break;
		case 0x61:
			commandToPrepare = CMD_GETTIMERS;
			break;
		case 0x65:
			commandToPrepare = CMD_SETTIMERS;
			// TODO
			break;
		case 0x63:
			commandToPrepare = CMD_SETSERVO;
			// function
			commandToPrepare[7] = function;
			// servo
			commandToPrepare[8] = servo;
			// position
			commandToPrepare[9] = position_value;
			break;
		case 0x67:
			commandToPrepare = CMD_GETSERVOPOS;
			break;
		case 0x69:
			commandToPrepare = CMD_MOVETOFUNC;
			// function
			commandToPrepare[7] = function;
			break;
		case 0x6D:
			commandToPrepare = CMD_UNLOCK;
			break;
		case 0x6F:
			commandToPrepare = CMD_JUMP2;
			// TODO
			break;
		case 0x71:
			commandToPrepare = CMD_GETTMEMORY;
			break;
		case 0x73:
			commandToPrepare = CMD_SETTMEMORY;
			// memory
			commandToPrepare[7] = memory;
			break;
		case 0x75:
			commandToPrepare = CMD_GETLOWRPM;
			break;
		case 0x77:
			commandToPrepare = CMD_SETLOWRPM;
			// rpm
			commandToPrepare[7] = rpm;
			break;
		default:
			break;
		}

		commandToPrepare[commandToPrepare.length - 2] = computeCheckSum(commandToPrepare);

		return commandToPrepare;
	}

	public byte computeCheckSum(byte[] command) {

		byte checksum = 0x00;
		int sum = 0;

		for (int i = 7; i < command.length - 2; i++) {
			sum += command[i];
		}

		checksum = (byte) (sum / 256);
		return checksum;
	}

	public SidusTimerProgram getSidusTimerProgram() {
		return sidusTimerProgram;
	}

	public void setSidusTimerProgram(SidusTimerProgram sidusTimerProgram) {
		this.sidusTimerProgram = sidusTimerProgram;
	}
}
