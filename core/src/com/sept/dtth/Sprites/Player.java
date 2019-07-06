package com.sept.dtth.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sept.dtth.DownTo;
import com.sept.dtth.Screens.PlayScreen;

import Scenes.Controller;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING , STANDING, RUNNING, SLASHING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerSlash;
    private float stateTimer;
    private Boolean runningRight;
    private PlayScreen screen;



    public Player(PlayScreen screen){
        super(screen.getAtlasPlayer().findRegion("a"));
        this.world = screen.getWorld();
        this.screen = screen;
        currentState =  State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=1; i<7;i++)
            frames.add(new TextureRegion(getTexture(),i*50 ,37,50,37));
        playerRun = new Animation(0.1f,frames);

        frames.clear();

        for(int i=0; i<6;i++)
            frames.add(new TextureRegion(getTexture(),i*50 ,37*2,50,37));
        for(int i=0;i<3;i++)
            frames.add(new TextureRegion(getTexture(),i*50,37*3,50,37));
        playerJump = new Animation(0.05f,frames);
        frames.clear();

        for(int i=4; i<7;i++)
            frames.add(new TextureRegion(getTexture(),i*50 ,37*7,50,37));
        for(int i=0; i<3;i++)
            frames.add(new TextureRegion(getTexture(),i*50 ,37*8,50,37));
        playerSlash = new Animation(0.1f,frames);

        frames.clear();

        definePlayer();
        playerStand = new TextureRegion(getTexture(),0,0,50,37);
        setBounds(0,0,50/DownTo.PPM,37/DownTo.PPM);
        setRegion(playerStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth()/2,b2body.getPosition().y - getHeight()/2 -2/DownTo.PPM);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer,true);
                break;
            case SLASHING:
                region = playerSlash.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x >0 || runningRight)&& region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer+dt :0;
        previousState= currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y >0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y <0)
            return State.FALLING;
        else if( screen.controller.isDownPressed())
            return State.SLASHING;
        else if(b2body.getLinearVelocity().x !=0)
            return State.RUNNING;
        else
            return State.STANDING;
    }


    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(100/ DownTo.PPM,100/DownTo.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/DownTo.PPM);
        fdef.filter.categoryBits = DownTo.PLAYER_BIT;
        fdef.filter.maskBits = DownTo.DEFAULT_BIT |
                DownTo.ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.setPosition(new Vector2(0,-14/DownTo.PPM));
        b2body.createFixture(fdef);


        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/DownTo.PPM,6/DownTo.PPM),new Vector2(2/DownTo.PPM,6/DownTo.PPM));
        fdef.shape = head;

        b2body.createFixture(fdef).setUserData("head");
        fdef.isSensor = true;
    }


}
