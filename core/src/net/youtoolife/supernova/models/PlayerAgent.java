package net.youtoolife.supernova.models;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;

import net.youtoolife.supernova.handlers.StateAgent;

public class PlayerAgent implements StateAgent {
	
	public StateMachine<PlayerAgent, PlayerState> stateMachine;
	
	public PlayerAgent() {
		stateMachine = new DefaultStateMachine<PlayerAgent, PlayerState>(this, PlayerState.AIRBORN);
	}
}
