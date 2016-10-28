package net.youtoolife.supernova.handlers;

import net.youtoolife.supernova.handlers.gui.RMEMessage;
import net.youtoolife.supernova.screens.Surface;

public class RMECmd {
	
	public String cmd = null;
	public RMECmd next = null;
	
	private RMEPack pack = Surface.pack;
	
	public boolean done = false;
	private boolean chaildDone = false, myDone = false;
	public float time = 0;
	
	public RMECmd(String cmd) {
		this.cmd = cmd;
		
		String[] args = cmd.split("<>");
		if (args.length > 2)
			time = Float.parseFloat(args[2]);
	}
	
	
	private void run(float delta) {
		
		String[] args = cmd.split("<>");
		
		if (args[0].equalsIgnoreCase("msg")) {
			pack.addMsg(new RMEMessage(args[1]));
		}
		
	}
	
	public void handle(float delta) {
		
		if (next != null && !chaildDone) {
			next.handle(delta);
			chaildDone = next.done;
		}
		else
			chaildDone = true;
		
		if (!myDone)
		if (time > 0) {
			time -= delta;
			return;
		}
		else {
			//....do
			run(delta);
			myDone = true;
		}
		
		done = myDone && chaildDone;
		
	}

}
