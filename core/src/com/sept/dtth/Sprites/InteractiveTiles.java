package com.sept.dtth.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.sept.dtth.Screens.PlayScreen;

public abstract class InteractiveTiles {
    private World world;
    private TiledMap map;
    private TiledMapTile tile;
    private Rectangle bounds;
    private Body body;

    public InteractiveTiles(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
    }
}
