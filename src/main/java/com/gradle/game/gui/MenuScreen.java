package com.gradle.game.gui;

import com.gradle.game.GameManager;
import com.gradle.game.GameType;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.GamepadEvents;
import de.gurkenlabs.litiengine.input.IKeyboard;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuScreen extends Screen {
    protected Menu menu;
    protected String[] menuOptions = {"Play", "Exit"};
    protected int options;
    protected boolean mouseEnabled = true;

    protected IKeyboard.KeyReleasedListener keyListener;
    protected GamepadEvents.GamepadPollListener gamepadListener;

    public MenuScreen() {
        this("MENU-MAIN");
    }

    protected MenuScreen(String screenName) {
        super(screenName);

        //used for keyboard input. added to Input by prepare.
        keyListener = event -> {
            if (isSuspended()) {
                return;
            }

            initMenuKeyboardNav(event.getKeyCode());

            if ((event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_SPACE) && menu.isEnabled()) {
                //Game.audio().playSound("confirm.wav");
                menuOptionSelect();
                removeListeners();
            }
        };
    }

    // taken from
    // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java

    // runs when menu is opened
    @Override
    public void prepare() {
        this.menu.setEnabled(true);
        super.prepare();
        //Game.loop().attach(this);
        Game.window().getRenderComponent().setBackground(Color.BLACK);

        initMenu();
        this.menu.getCellComponents().get(0).setSelected(true);

        //keyboard control. must be initialized here so that it does not mess with player navigation.
        setListeners();
    }

    // runs before constructor. Why? I don't know.
    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        final double buttonWidth = 450;

        this.options = getMenuOptions().length-1;
        this.menu = new Menu(centerX - buttonWidth / 2, centerY, buttonWidth, centerY / 4 * (options+1), getMenuOptions());

        //TODO: move to override-able function
        GuiComponent title = new GuiComponent(centerX - 450/2.0, centerY/2) {
            @Override
            protected void initializeComponents() {
                super.initializeComponents();
                this.setFont(FontTypes.TITLE);
                this.getAppearance().setForeColor(new Color(255,255,255));
                this.setText("Gamer Zone");
                this.setDimension(450, FontTypes.TITLE.getSize());
                this.getAppearanceHovered().update(GuiProperties.getDefaultAppearance());
            }
        };

        // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java

        this.getComponents().add(title);
        this.getComponents().add(this.menu);
    }

    protected void setListeners() {
        //must be delayed one tick so that it doesn't run immediately, should the screen switch be
        //activated by another listener.
        Game.loop().perform(1, ()-> Input.keyboard().onKeyReleased(keyListener));
    }

    protected void removeListeners() {
        Input.keyboard().removeKeyReleasedListener(keyListener);
    }

    // override to change appearances
    protected void initMenu() {
        this.menu.getCellComponents().forEach(comp -> {
            // initialize individual components
            comp.setFont(FontTypes.MENU);
            comp.getAppearance().setForeColor(new Color(255,255,255));
            //comp.setWidth(comp.getText().length()*33);
            comp.setHeight(FontTypes.MENU.getSize());
            comp.getAppearance().setBorderColor(new Color(255,255,255));
            comp.getAppearance().setBorderRadius(2);
            if (mouseEnabled) {
                comp.onClicked(e -> {
                    menuOptionSelect();
                    removeListeners();
                });
            }
        });
    }

    // override to change menu items
    protected String[] getMenuOptions() {
        return new String[]{"Start", "Co-op", "Exit"};
    }

    //override for inputs
    protected void menuOptionSelect() {
        switch (this.menu.getCurrentSelection()) {
            case 0 -> GameManager.loadLevel();
            case 1 -> {
                GameManager.setCurrentGameType(GameType.COOP);
                GameManager.loadLevel();
            }
            case 2 -> System.exit(0);
        }
        this.menu.setEnabled(false); //disables all buttons, so they can't be entered multiple times
    }

    // don't override. this makes life easy
    private void initMenuKeyboardNav(int keycode) {
        if (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
            if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
                this.menu.setCurrentSelection(Math.min(this.options, this.menu.getCurrentSelection() + 1));
            } else {
                this.menu.setCurrentSelection(Math.max(0, this.menu.getCurrentSelection() - 1));
            }
            for (ImageComponent comp : this.menu.getCellComponents()) {
                comp.setHovered(false);
            }
            this.menu.getCellComponents().get(this.menu.getCurrentSelection()).setHovered(true);
            //Game.audio().playSound("select.wav");
        }
    }

    @Override
    public void suspend() {
        super.suspend();
        this.removeListeners();
    }
}
