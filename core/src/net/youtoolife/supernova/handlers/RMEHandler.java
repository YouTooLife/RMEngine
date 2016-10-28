package net.youtoolife.supernova.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import net.youtoolife.supernova.handlers.gui.RMEMessage;

public class RMEHandler {
	
	//private RMEPack pack;
	
	private Array<RMECmd> cmds;
	
	public RMEHandler() {
		//this.pack = pack;
		
		cmds = new Array<RMECmd>();
	}
	
	private void handle(float delta) {
		
		if (cmds.size < 1)
			return;
		
		if (cmds.size > 0) {
			
			RMECmd cmd = cmds.first();
			cmd.handle(delta);
			
			if (cmd.done)
				cmds.removeValue(cmd, false);
		}
		
	}
	
	private RMECmd parse(String src) {
		
		String[] cmd = src.split("->");
		
		
		if (cmd.length > 1) {
			RMECmd head = null;
			RMECmd cmd1 = null;
		for (int i = 0; i < cmd.length; i++) {
			if (i == 0) {
				cmd1 = parse(cmd[i]);
				head = cmd1;
			}
			else {
				RMECmd cmd2 = parse(cmd[i]);
				cmd1.next = cmd2;
				cmd1 = cmd2;
			}
		}
		return head;
		}
		else {
		cmd = src.split("<-");
		if (cmd[0].equalsIgnoreCase("file")) {
			String cmd2 = Gdx.files.local(cmd[1]).readString();
			System.out.println(cmd2);
			return parse(cmd2);
		}
		}
		
		return new RMECmd(src);
	}
	
	public void update(float delta) {
		handle(delta);
	}
	
	public void addCmd(String cmd) {
		cmds.add(parse(cmd));
	}
	
}
