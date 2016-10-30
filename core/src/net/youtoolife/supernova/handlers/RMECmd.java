package net.youtoolife.supernova.handlers;

import net.youtoolife.supernova.handlers.gui.RMEMessage;
import net.youtoolife.supernova.screens.Surface;

public class RMECmd {
	
	public String cmd = null;
	public RMECmd next = null;
	
	private RMEPack pack = Surface.pack;
	
	public boolean done = false;
	private boolean chaildDone = false, myDone = false, postDone = true;
	public float time = 0;
	public double postTime = 0;
	
	public RMECmd(String cmd) {
		this.cmd = cmd;
		
		String[] args = cmd.split("<>");
		if (args.length > 2)
			time = Float.parseFloat(args[2]);
	}
	
	
	private void run(float delta) {
		
		String[] args = cmd.split("<>");
		
		if (args[0].equalsIgnoreCase("msg")) {
			RMEMessage msg = new RMEMessage(args[1]);
			pack.addMsg(msg);
			postTime = msg.time;
			postDone = false;
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
		
		if (!postDone)
		if (postTime > 0) {
			postTime -= delta;
			return;
		}
		else {
			postDone = true;
		}
		
		done = myDone && chaildDone && postDone;
		
	}

}
