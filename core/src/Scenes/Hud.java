package Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sept.dtth.DownTo;
import com.sept.dtth.Screens.PlayScreen;
import com.sept.dtth.Sprites.Player;


public class Hud implements Disposable {

    public Stage stage;
    public Viewport viewport;
    int score;
    int demonVal;

    Label scoreLabel;
    private ProgressBar.ProgressBarStyle demonbarStyle;
    private ProgressBar demonbar;

    public Hud(SpriteBatch sb){

        score = 0;
        demonVal=100;


        viewport = new FitViewport(DownTo.V_WIDTH,DownTo.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.left().top();
        table.setFillParent(true);

        demonbarStyle = new ProgressBar.ProgressBarStyle(setSkin());
        demonbar = new ProgressBar(0,100,1f,false,demonbarStyle);
        demonbar.setSize(50,50);
        demonbar.setValue(50);


        scoreLabel = new Label(String.format("Score : %06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(demonbar).pad(5,5,5,5);
        table.row();
        table.add(scoreLabel);

        stage.addActor(table);
    }

    public void update(float dt){
        demonbar.setValue(demonVal);
    }

    public Stage getStage() { return stage; }

    public void substactDemon(int value){
        demonVal -= value;
        demonbar.setValue(demonVal);
    }
    public void addDemon(int value){
        demonVal += value;
        demonbar.setValue(demonVal);
    }

    public void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("Score : %06d", score));
    }

    public void dispose(){
        stage.dispose();
    }

    private ProgressBar.ProgressBarStyle setSkin() {
        Color bgColor = new Color(100/256f, 100/256f,100/256f,1f);
        Skin skin;
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(1,0,0,1);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        ProgressBar.ProgressBarStyle barStyle;
        barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", bgColor), skin.newDrawable("white", Color.RED));
        barStyle.knobAfter= barStyle.knob;
        return barStyle;
    }
}
