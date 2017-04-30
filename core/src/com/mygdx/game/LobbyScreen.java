package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/**
 * Created by root on 25.04.2017.
 */

public class LobbyScreen implements Screen {
    private Skin skin;
    private Stage stage;
    private MainClass app;
    private TextButton createGameButton;
    private Table rootTable;// occupies the whole device screen
    private ScrollPane scrollPane;
    private  Table scrollPaneTable;
    private Stack stack;
    Cell cell;
    private float heightDistanceUnit;


    public LobbyScreen(MainClass app) {
        this.app = app;
    }

    @Override
    public void show() {

        //skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        heightDistanceUnit = app.deviceHeight / 18;

        initializeRootTable();

        initializeScrollPane();

        initializeCreateButton();

        for (int i=0; i<1; i++){
           newGameToList(i);
        }


        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
        //Gdx.app.log("Device size", app.deviceHeight + "  width :" + app.deviceWidth);

    }
    public void initializeRootTable() {
        rootTable = new Table();

        // debug highlights the table cells(green) and widgets(red)
        //rootTable.setDebug(true);

        rootTable.pad(30f);
        rootTable.setFillParent(true);
        //rootTable.setPosition(0, 0); //puts the table in the bottom-left corner of the device
        rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("bgLobby.png"))));

        Texture lobbyLabelTexture = new Texture("topLabelLobby.png");
        TextureRegion lobbyLabelRegion = new TextureRegion(lobbyLabelTexture);
        Image lobbyLabelImage = new Image(lobbyLabelRegion);
        cell = rootTable.add(lobbyLabelImage).width(app.deviceWidth).height(2 * heightDistanceUnit).padBottom(heightDistanceUnit);
        rootTable.row();

    }

    public void initializeScrollPane(){
        scrollPaneTable = new Table();
        scrollPaneTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("bgLobby.png"))));
       // scrollPaneTable.defaults().expandX().fill();
        scrollPaneTable.center().top();

        scrollPane = new ScrollPane(scrollPaneTable, skin);
        scrollPane.setFadeScrollBars(true);

        Gdx.app.log("first cell height:", " " );

        //Gdx.app.log("Rows: ", rootTable.getRows() + " " + rootTable.);
        scrollPaneTable.pad(10);
        rootTable.add(scrollPane).width(5*app.deviceWidth/7).height(11 * heightDistanceUnit).padBottom(heightDistanceUnit);
        rootTable.row();
    }





    private GameTableDataStructure newGameToList(int id) {
        GameTableDataStructure newGameTableDataStructure = new GameTableDataStructure(scrollPaneTable, id);
        Table gameTable = newGameTableDataStructure.parentTable;
        float prefferedWidth = gameTable.getMinWidth();
        float prefferedHeight = gameTable.getMinHeight();

        scrollPaneTable.add(gameTable).width(prefferedWidth).height(prefferedHeight);
        scrollPaneTable.row();

        return newGameTableDataStructure;

    }


    private void initializeCreateButton() {
        createGameButton = new TextButton("Create new game", skin, "default");

        createGameButton.getLabel().setFontScale(2f);



        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(new GameScreen(app));
                dispose();
            }

        });

        rootTable.add(createGameButton).align(Align.bottom).width(2 * app.deviceWidth / 3).height(2 * heightDistanceUnit).padBottom(heightDistanceUnit);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

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

        skin.dispose();
        stage.dispose();

    }
}