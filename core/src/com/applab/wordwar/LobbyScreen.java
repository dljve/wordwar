package com.applab.wordwar;

import com.applab.wordwar.model.GameModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;


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
    private Table scrollPaneTable;



    public LobbyScreen(MainClass app) {
        this.app = app;
    }

    @Override
    public void show() {


        //skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        // TODO: receive games information from server


        MainClass.HEIGHT_DISTANCE_UNIT = MainClass.deviceHeight / 18;

        initializeRootTable();

        initializeScrollPane();

        initializeCreateButton();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                app.getClient().getGames();
                boolean gamesNotFound = true;
                ArrayList<GameModel> games = new ArrayList<GameModel>();
                while(gamesNotFound) {
                    games = app.getClient().getGamesToJoin();
                    gamesNotFound = (games == null);
                }
                for(GameModel game: games){
                    newGameToList(game);
                }
            }
        });



        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
        //Gdx.app.log("Device size", app.deviceHeight + "  width :" + app.deviceWidth);

    }

    public void initializeRootTable() {
        rootTable = new Table();

        // debug highlights the table cells(green) and widgets(red)
        // rootTable.setDebug(true);

        rootTable.pad(30f);
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("bgScreens.png"))));

        //adding the top label of this screen
        Texture lobbyLabelTexture = new Texture("topLabelLobby.png");
        TextureRegion lobbyLabelRegion = new TextureRegion(lobbyLabelTexture);
        Image lobbyLabelImage = new Image(lobbyLabelRegion);
        rootTable.add(lobbyLabelImage).width(MainClass.deviceWidth).height(2 * MainClass.HEIGHT_DISTANCE_UNIT).padBottom(MainClass.HEIGHT_DISTANCE_UNIT);
        rootTable.row();

    }

    public void initializeScrollPane() {
        scrollPaneTable = new Table();
        scrollPaneTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("bgScreens.png"))));
        scrollPaneTable.defaults().expandX();
        scrollPaneTable.center().top();
        //scrollPaneTable.setDebug(true);

        scrollPane = new ScrollPane(scrollPaneTable, skin);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollingDisabled(true, false);


        Gdx.app.log("columns:", " " + scrollPaneTable.getColumns());

        //Gdx.app.log("Rows: ", rootTable.getRows() + " " + rootTable.);
        scrollPaneTable.pad(10);
        rootTable.add(scrollPane).width(6 * MainClass.deviceWidth / 7).height(12 * MainClass.HEIGHT_DISTANCE_UNIT).padBottom(MainClass.HEIGHT_DISTANCE_UNIT);
        rootTable.row();
    }


    private GameTableDataStructure newGameToList(GameModel game) {
        GameTableDataStructure newGameTableDataStructure = new GameTableDataStructure(scrollPaneTable, game, app.getGillsansFont(), app, this);


        Table gameTable = newGameTableDataStructure.parentTable;
        float gameTableWidth = scrollPaneTable.getWidth() - 200;
        float gameTableHeight = MainClass.HEIGHT_DISTANCE_UNIT * 2;

        scrollPaneTable.add(gameTable).width(gameTableWidth).height(gameTableHeight).padBottom(MainClass.HEIGHT_DISTANCE_UNIT).center();
        //scrollPaneTable.add(new Table());
        scrollPaneTable.row();

        return newGameTableDataStructure;

    }


    private void initializeCreateButton() {
        createGameButton = new TextButton("Create new game", skin, "default");

        createGameButton.getLabel().setFontScale(2f);

        rootTable.add(createGameButton).align(Align.bottom).width(2 * MainClass.deviceWidth / 3).height(2 * MainClass.HEIGHT_DISTANCE_UNIT).padBottom(20);

        boolean experiment = true;

        if (!experiment) {
            createGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int timeSlept = 0;
                    while(app.getClient() == null && timeSlept < 100000){
                        try {
                            Thread.sleep(1000);
                            timeSlept += 1000;
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    app.getClient().createGame(System.currentTimeMillis());

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            boolean gameNotCreated = true;
                            GameModel gm;
                            while(gameNotCreated) {
                                gm = app.getClient().getGameModel();
                                gameNotCreated = (gm == null);
                            }
                            // TODO Go to waiting room instead
                            app.setScreen(new Game(app));
                            dispose();
                        }
                    });
                }
            });
        }
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