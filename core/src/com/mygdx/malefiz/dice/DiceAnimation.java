package com.mygdx.malefiz.dice;

/**
 * Created by Lilibeth on 26.04.2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Lilibeth on 25.04.2017.
 */

public class DiceAnimation  {

    // Zeilen & Spalten des SPrite sheets
    private static final int COLS = 12;
    private static final int ROW = 2;

    // Deklarierung
    private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
    private Texture diceSheet;
    private SpriteBatch spriteBatch;

    // timer für animation
    private float time;

    //Initialisieren, von Texture, TextureRegions, Animation & umwandlung eines 2D TextureRegion zu 1D Textureregion
    public void create(String img) {

        diceSheet = new Texture(Gdx.files.internal(img));

        TextureRegion[][] tmp = TextureRegion.split(diceSheet,
                diceSheet.getWidth() / COLS,
                diceSheet.getHeight() / ROW);

        TextureRegion[] walkFrames = new TextureRegion[COLS * ROW];
        int index = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        animation = new Animation<>(0.1f, walkFrames);
        spriteBatch = new SpriteBatch();
        time = 0f;
    }


    //Animation ohne Loop zu textureRegion casten & batch zeichnen
    public void render() {
        time += Gdx.graphics.getDeltaTime();

        TextureRegion actDiceAnimation = animation.getKeyFrame(time, false);
        spriteBatch.begin();
        spriteBatch.draw(actDiceAnimation, 100, 500, 350, 180);
        spriteBatch.end();
    }



    public void dispose() {
        spriteBatch.dispose();
        diceSheet.dispose();
    }

    public boolean renderFinished(){
        return animation.isAnimationFinished(time);
    }
}
