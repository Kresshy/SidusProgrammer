package hu.kresshy.sidusprogrammer.application;

import hu.kresshy.sidusprogrammer.bluetooth.SidusConnectionService;
import hu.kresshy.sidusprogrammer.bluetooth.SidusConnectionService.State;
import android.app.Application;

public class SidusApplication extends Application {
	
	private SidusConnectionService mConnectionService = null;
	private State state = State.disconnected;
	
	@Override
	public void onCreate() {
		
	}
	
	public SidusConnectionService getConnectionService() {
		return mConnectionService;
	}

	public void setConnectionService(SidusConnectionService mConnectionService) {
		this.mConnectionService = mConnectionService;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
