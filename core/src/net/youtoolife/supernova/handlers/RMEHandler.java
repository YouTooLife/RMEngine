package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.handlers.gui.RMEMessage;

public class RMEHandler {
	
	private RMEPack pack;
	
	private Array<String> cmds;
	
	public RMEHandler(RMEPack pack) {
		this.pack = pack;
		
		cmds = new Array<String>();
	}
	
	private void handle() {
		
		if (cmds.size < 1)
			return;
		
		String src = cmds.first();
		String[] cmd = src.split("<>");
		
		if (cmd.length < 1)
			return;
		
		if (cmd[0].equalsIgnoreCase("msg")) {
			pack.addMsg(new RMEMessage(cmd[1]));
			
			cmds.removeIndex(0);
		}
	}
	
	public void update(float delta) {
		handle();
	}
	
	public void addCmd(String cmd) {
		cmds.add(cmd);
	}
	
}
