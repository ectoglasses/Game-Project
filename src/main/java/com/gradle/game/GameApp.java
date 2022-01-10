package com.gradle.game;
import com.gradle.game.gui.ControllerScreen;
import com.gradle.game.gui.IngameScreen;
import com.gradle.game.gui.MenuScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;

public class GameApp {
    public static void main(String[] args) {
        Game.info().setName("New Game");
        Game.info().setSubTitle("");
        Game.info().setVersion("v0.0.1");
        Game.info().setWebsite("https://github.com/gurkenlabs/litiengine-gurk-nukem");
        Game.info().setDescription("An example 2D platformer with shooter elements made in the LITIENGINE");

        Game.init(args);

        Game.graphics().setBaseRenderScale(4f);

        //import resources here
        Resources.load("game.litidata");

        // add menu screen first, so it can display first. If that doesn't work,
        // add Game.screens().display("MENU");
        Game.screens().setChangeCooldown(200);
        Game.screens().add(new MenuScreen());
        Game.screens().add(new IngameScreen());
        Game.screens().add(new ControllerScreen());

        GameManager.init();

        //load evironment, then spawn player. contained for the sake of memory.
//        {
//            Environment e = Game.world().loadEnvironment("mansion");
//            GameManager.spawn(e, "enter");
//        }

        Game.start();
    }
}
