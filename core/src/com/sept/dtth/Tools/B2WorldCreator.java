package com.sept.dtth.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sept.dtth.DownTo;
import com.sept.dtth.Screens.PlayScreen;
import com.sept.dtth.Sprites.GroundEnemy;

public class B2WorldCreator {

    private Array<GroundEnemy> groundEnemyArray;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / DownTo.PPM, (rect.getY() + rect.getHeight() / 2) / DownTo.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / DownTo.PPM, rect.getHeight() / 2 / DownTo.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }
}
