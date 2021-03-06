package com.mygdx.game.stages.Menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.settings.GameSettings;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class GameConfigStage extends MenuStage {

    private Integer players=1;
    private float startCash= 2000.0f;
    private float cashOverGo= 200.0f;
    private boolean directGoDoubled = false;

    public GameConfigStage(Game game, MainMenuScreen screen, Skin skin){
        super(game,skin,screen);
        buildStage();
    }

    @Override
    void buildStage() {
        // Title Label
        final Label title = new Label("Game Settings", skin);
        title.setColor(Color.GOLD);

        //Slider for choosing start money
        final Slider cashSlider = new Slider(100.0f,10000.0f,100f,false,skin);
        cashSlider.setValue(startCash);
        final Label cashsliderLabel = new Label("Start Cash:",skin);
        final Label cashIndicatorLabel = new Label(startCash+"M",skin);

        //Slider for choosing amount of money when going over GO!
        final Slider goCashSlider = new Slider(0f,1000.0f,100f,false,skin);
        cashSlider.setValue(startCash);
        Label goCashSliderLabel = new Label("Cash over GO:",skin);
        final Label goCashIndicatorLabel = new Label(cashOverGo+"M",skin);

        cashSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
             startCash =  handleFloatSliderChange(cashSlider,cashIndicatorLabel);
            }
        });

        goCashSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              cashOverGo = handleFloatSliderChange(goCashSlider,goCashIndicatorLabel);
            }
        });

        //Checkbox for toggling double cash on directly hitting GO
        final CheckBox doubleCashBox = new CheckBox(null,skin);
        final Label doubleCashBoxLabel = new Label("Double cash when hitting go:",skin);
        doubleCashBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                directGoDoubled = !directGoDoubled;
            }
        });

        //Play Button
        final TextButton playButton = new TextButton("Start!", skin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                screen.stopBGM();
                GameSettings settings = new GameSettings(players,(int)startCash,directGoDoubled,(int)cashOverGo);
                game.setScreen(new GameScreen(game,settings));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        //Play Button
        final TextButton backButton = new TextButton("Back!", skin);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                screen.setStage(new MainMenuStage(screen,game,skin));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        //Player slider for choosing amount of Players
        final Slider playerSlider = new Slider(1.0f,4.0f,1.0f,false,skin);
        Label sliderLabel = new Label("Players:",skin);
        final Label playerIndicatorLabel = new Label("1",skin);

        playerSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                players = (int)playerSlider.getValue();
                playerIndicatorLabel.setText(players.toString());
            }
        });

        // Create tables
       Table topTable = new Table().top();
       Table bottomTable = new Table().bottom();


        // Set table structure
        topTable.top();
        topTable.add(title).padTop(5f).colspan(3).expandX().center();
        topTable.row().spaceTop(20).expandX();
        topTable.add(sliderLabel);
        topTable.add(playerSlider).fill();
        topTable.add(playerIndicatorLabel).center().expandX();

        topTable.row().space(20,0,0,0);

        topTable.row().expandX();
        topTable.add(cashsliderLabel);
        topTable.add(cashSlider).fill();
        topTable.add(cashIndicatorLabel).width(30).center().expandX();

        topTable.row().expandX();
        topTable.add(goCashSliderLabel);
        topTable.add(goCashSlider).fill();
        topTable.add(goCashIndicatorLabel).width(30).center().expandX();


        topTable.row();
        topTable.add(doubleCashBoxLabel);
        topTable.add(doubleCashBox).fill();

        bottomTable.padBottom(25);
        bottomTable.add(backButton).width(200).expandX().spaceBottom(15);
        bottomTable.row();
        bottomTable.add(playButton).width(200).expandX();

        // Pack table
        topTable.setFillParent(true);
        bottomTable.setFillParent(true);
        topTable.pack();
        bottomTable.pack();

        // Set table's alpha to 0
        topTable.getColor().a = 0f;
        bottomTable.getColor().a = 0f;

        // Adds created table to stage
        this.addActor(topTable);
        this.addActor(bottomTable);

        // To make the table appear smoothly
        topTable.addAction(fadeIn(2f));
        bottomTable.addAction(fadeIn(2f));
    }

    private float handleFloatSliderChange(Slider slider, Label labelToChange){
        DecimalFormat df = new DecimalFormat(".#",new DecimalFormatSymbols(Locale.ENGLISH));
        String value = df.format(slider.getValue());
        labelToChange.setText(value+"M");
        return Float.parseFloat(value);
    }
}
