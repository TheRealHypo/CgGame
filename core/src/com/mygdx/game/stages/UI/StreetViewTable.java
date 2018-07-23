package com.mygdx.game.stages.UI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.Street;
import com.mygdx.game.components.Street.StreetType;
import com.mygdx.game.controller.GameStates;
import com.mygdx.game.screens.GameScreen;

public class StreetViewTable extends Dialog implements GameUiElement {

    GameScreen screen;
    RentTableBuilder.RentTable rt;
    TextButton buyBtn, auctBtn;

    private SpriteDrawable[] icons = new SpriteDrawable[5];

    public StreetViewTable(Skin skin, GameScreen screen) {
        super("Buy Street ?", skin);
        this.screen = screen;
    }

    private void initIcons() {
        for (int i = 0; i < 5; i++) {
            Texture texture = new Texture(Gdx.files.internal("UI/streetViewer/House" + (i + 1) + ".png"));
            Sprite sprite = new Sprite(texture);
            sprite.setSize(32f, 32f);
            SpriteDrawable icon = new SpriteDrawable(sprite);
            icons[i] = icon;
        }
    }

    @Override
    public StreetViewTable create() {
        this.initIcons();
        Pixmap backgroundColor = new Pixmap(300, 400, Pixmap.Format.RGB888);
        backgroundColor.setColor(Color.WHITE);
        backgroundColor.fill();
        this.setBackground(new Image(new Texture(backgroundColor)).getDrawable());
        this.setFillParent(false);
        this.setPosition(400, 400);
        this.setSize(300, 400);
        this.setVisible(true);
        this.setTouchable(Touchable.enabled);
        Dialog current = this;
        current.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                current.moveBy(x - current.getWidth() / 2, y - current.getHeight() / 2);
            }
        });

        buyBtn = new TextButton("Buy!", getSkin());
        buyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getGameController().getGameStateMachine().changeState(GameStates.BUY);
            }
        });
        auctBtn = new TextButton("Auction!", getSkin());
        this.getButtonTable().add(buyBtn);
        this.getButtonTable().add(auctBtn);
        this.getContentTable().add(rt);

        return this;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setVisible(screen.getGameController().getGameStateMachine().getCurrentState() == GameStates.FIELD);
        if (screen.getGameController().getGameStateMachine().getCurrentState() == GameStates.FIELD) {
            Entity entity = screen.getGameController().getCurrentPlayer();
            PlayerComponent player = entity.getComponent(PlayerComponent.class);
            Street street = player.getCurrentStreet();
            if (street.getCost() > player.getMoney()) {
                buyBtn.setTouchable(Touchable.disabled);
                buyBtn.setText("Funds insufficient");
            }
            if (street.getType() == StreetType.PROPERTY) {
                this.getContentTable().clearChildren();
                rt = new RentTableBuilder(street, icons).createPropertyTable();
                this.getContentTable().add(rt);
                this.pack();
            }
        }
    }
}