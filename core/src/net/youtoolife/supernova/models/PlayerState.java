package net.youtoolife.supernova.models;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PlayerState implements State<PlayerAgent> {
	
	AIRBORN() {
	@Override
	public void update(PlayerAgent playerAgent) {
		
		}
	};
	

	@Override
	public void enter(PlayerAgent entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PlayerAgent entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(PlayerAgent entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(PlayerAgent entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}

}
