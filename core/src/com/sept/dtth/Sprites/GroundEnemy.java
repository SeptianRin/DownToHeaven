package com.sept.dtth.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sept.dtth.DownTo;
import com.sept.dtth.Screens.PlayScreen;

public class GroundEnemy extends Enemy {
    private  float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    public GroundEnemy(PlayScreen screen, float x ,float y) {
        super(screen,x,y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i<3 ; i++)
            frames.add(new TextureRegion(screen.getAtlasEnemy().findRegion("b"),i*16,0,16,16));
        walkAnimation = new Animation(0.4f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),16/DownTo.PPM,16/DownTo.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        moveGE(dt);
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(walkAnimation.getKeyFrame(stateTime,true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(250/ DownTo.PPM,200/DownTo.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/DownTo.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.setGravityScale(1);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/DownTo.PPM,6/DownTo.PPM),new Vector2(2/DownTo.PPM,6/DownTo.PPM));
        fdef.shape = head;

        b2body.createFixture(fdef).setUserData("head");
        fdef.isSensor = true;
    }
    private void moveGE(float dt){
        //simple pathfinding to player
        float targetX = screen.player.b2body.getPosition().x; //Player's X
        float targetY = screen.player.b2body.getPosition().y; //Player's Y
        float spriteX = this.b2body.getPosition().x; //Enemy's X
        float spriteY = this.b2body.getPosition().y; //Enemy's Y
        float x2 = this.b2body.getPosition().x; //Enemy's new X
        float y2 = this.b2body.getPosition().y; //Enemy's new Y
        float angle; // We use a triangle to calculate the new trajectory
        angle = (float) Math
                .atan2(targetY - spriteY, targetX - spriteX);
        x2 += (float) Math.cos(angle) * 40/DownTo.PPM
                * dt;
        y2 += (float) Math.sin(angle) * 40/DownTo.PPM
                * dt;
        b2body.setTransform(x2,y2,angle);
        //this.setPosition(x2, y2); //Set enemy's new positions.
    }
}
