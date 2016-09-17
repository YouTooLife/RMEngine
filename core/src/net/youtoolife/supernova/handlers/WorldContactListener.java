package net.youtoolife.supernova.handlers;

import net.youtoolife.supernova.models.Player;
import net.youtoolife.supernova.models.SurfaceX;
import net.youtoolife.supernova.models.Wall;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
	
	
	boolean wall = false;

	@Override
	public void beginContact(Contact contact) {
		System.out.println("lool");
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureA();
		
		RMESprite spriteA = (RMESprite) fixA.getUserData();
		RMESprite spriteB = (RMESprite) fixB.getUserData();
		
		if (spriteA != null && spriteB != null) {
			//if (!wall)
			if ((spriteA instanceof Player && spriteB instanceof Wall)) {
				((Player) spriteA).getBody().setLinearVelocity(0, 0);
				Vector2 keyForce = ((Player) spriteA).keyForce;
				Vector2 keyForce2 = new Vector2();
				if (keyForce.x > 0) keyForce2.x = -10;
				if (keyForce.x < 0) keyForce2.x = 10;
				if (keyForce.y > 0) keyForce2.y = -10;
				if (keyForce.y < 0) keyForce2.y = 10;
				((Player) spriteA).keyForce.set(0, 0);
				((Player) spriteA).getBody().setLinearVelocity(keyForce2.x, keyForce2.y);
				wall = true;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		System.out.println("lool2");
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureA();
		
		RMESprite spriteA = (RMESprite) fixA.getUserData();
		RMESprite spriteB = (RMESprite) fixB.getUserData();
		
		if (spriteA != null && spriteB != null) {
			if ((spriteA instanceof Player && spriteB instanceof Wall)) {
				wall = false;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
