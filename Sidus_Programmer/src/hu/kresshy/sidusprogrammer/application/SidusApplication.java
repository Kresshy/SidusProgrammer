package hu.kresshy.sidusprogrammer.application;

import hu.kresshy.sidusprogrammer.bluetooth.ConnectionService;
import hu.kresshy.sidusprogrammer.bluetooth.ConnectionService.State;
import android.app.Application;

public class SidusApplication extends Application {
	
	private ConnectionService mConnectionService = null;
	private State state = State.disconnected;
	
	@Override
	public void onCreate() {
		
	}
	
	public ConnectionService getConnectionService() {
		return mConnectionService;
	}

	public void setConnectionService(ConnectionService mConnectionService) {
		this.mConnectionService = mConnectionService;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
