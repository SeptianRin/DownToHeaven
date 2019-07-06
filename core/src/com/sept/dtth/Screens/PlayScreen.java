package com.sept.dtth.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sept.dtth.DownTo;
import com.sept.dtth.Sprites.Enemy;
import com.sept.dtth.Sprites.FlyingEnemy;
import com.sept.dtth.Sprites.GroundEnemy;
import com.sept.dtth.Sprites.Player;
import com.sept.dtth.Tools.B2WorldCreator;

import com.sept.dtth.Tools.WorldContactListener;

import Scenes.Controller;
import Scenes.Hud;

public class PlayScreen implements Screen {

    private DownTo game;
    private TextureAtlas atlasPlayer;
    private TextureAtlas atlasEnemy;
    private TextureAtlas atlasBoss;


    private OrthographicCamera gamecam;
    private Viewport gameport;

    //hud
    private Hud hud;

    //tilemap
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //box2d
    private World world;
    Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    public Player player;
    public Controller controller;
    private Sound jump;
    private Music bgm;
    private GroundEnemy groundEnemy1;
    private FlyingEnemy flyingEnemy1;

    float demonic = 1;


    int mapPixelWidth=0;

    public PlayScreen(DownTo game) {
        atlasPlayer = new TextureAtlas("player.pack");
        atlasEnemy = new TextureAtlas("enemy.pack");
        atlasBoss = new TextureAtlas("boss.pack");

        this.game = game;
        gamecam = new OrthographicCamera();

        //membuat kamera sesuai dengan aspek rasio yang ditentukan
        gameport = new FitViewport(DownTo.V_WIDTH / DownTo.PPM, DownTo.V_HEIGHT / DownTo.PPM, gamecam);

        //buat game hud yang berisi skor dan devil point
        hud = new Hud(game.batch);

        //load map dari Tiled dan persiapkan renderer
        maploader = new TmxMapLoader();
        map = maploader.load("tilemap1.tmx");
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        renderer = new OrthogonalTiledMapRenderer(map, 1 / DownTo.PPM);

        //buat kamera agar posisinya ditengah
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        player = new Player(this);

        world.setContactListener(new WorldContactListener());
        controller = new Controller();
        jump = Gdx.audio.newSound(Gdx.files.internal("sfx_jump.wav"));
        bgm = Gdx.audio.newMusic(Gdx.files.internal("bogart.ogg"));


        bgm.setVolume(0.2f);
        bgm.setLooping(true);
        bgm.play();

        groundEnemy1 = new GroundEnemy(this,.46f,.46f);
        flyingEnemy1 = new FlyingEnemy(this,.46f,.46f);
    }

    public TextureAtlas getAtlasPlayer(){
        return atlasPlayer;
    }

    public TextureAtlas getAtlasEnemy() {
        return atlasEnemy;
    }

    public TextureAtlas getAtlasBoss() {
        return atlasBoss;
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){

        //input virtual keyboard
        if(controller.isDownPressed() && player.b2body.getLinearVelocity().y == 0){
            hud.addScore(100);
        }
        if(controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0  ){

            long id = jump.play(1.0f);
            jump.setPitch(id,2);
            jump.setLooping(id,false);
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
        }
        if(controller.isRightPressed() && player.b2body.getLinearVelocity().x<= 2){

            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
        }
        if(controller.isLeftPressed() && player.b2body.getLinearVelocity().x<= 2){

            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
        }


        //input keyboard
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y == 0){
            hud.addScore(100);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y == 0 && player.previousState == Player.State.STANDING ){

            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x<= 2){
            player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x<= 2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
        }
    }

    public void update(float dt){
        handleInput(dt);
        world.step(1/60f,6,2);
        player.update(dt);
        groundEnemy1.update(dt);
        hud.update(dt);
        flyingEnemy1.update(dt);

        if(player.b2body.getPosition().x < gamecam.viewportWidth/2){
            gamecam.position.x = gamecam.viewportWidth/2;
        }
        else{
            gamecam.position.x = player.b2body.getPosition().x;
        }

        if (player.b2body.getPosition().y <0 || player.b2body.getPosition().x <0 || player.b2body.getPosition().x > mapPixelWidth ){
            player.b2body.setLinearVelocity(0f,0f);
            player.b2body.setTransform(65/DownTo.PPM,100/DownTo.PPM , 0);
        }
        if (player.currentState == Player.State.STANDING){
            player.b2body.setLinearVelocity(0f,0f);

        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        //b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        groundEnemy1.draw(game.batch);
        flyingEnemy1.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    @Override
    public void resize(int width, int height){
        gameport.update(width,height);
        controller.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        jump.dispose();
        bgm.dispose();
    }
}
