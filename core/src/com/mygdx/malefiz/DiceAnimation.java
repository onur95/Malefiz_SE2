package com.mygdx.malefiz;

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
    private final int cols = 12, rows = 2;

    // Deklarierung
    private Animation<TextureRegion> diceAnimation; // Must declare frame type (TextureRegion)
    private Texture diceSheet;
    private SpriteBatch spriteBatch;

    // timer für animation
    private float time;

    //Initialisieren, von Texture, TextureRegions, Animation & umwandlung eines 2D TextureRegion zu 1D Textureregion
    public void create(String img) {

        diceSheet = new Texture(Gdx.files.internal(img));

        TextureRegion[][] tmp = TextureRegion.split(diceSheet,
                diceSheet.getWidth() / cols,
                diceSheet.getHeight() / rows);

        TextureRegion[] walkFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        diceAnimation = new Animation<TextureRegion>(0.2f, walkFrames);
        spriteBatch = new SpriteBatch();
        time = 0f;
    }


    //Animation ohne Loop zu textureRegion casten & batch zeichnen
    public void render() {
        // Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time += Gdx.graphics.getDeltaTime();

        TextureRegion actDiceAnimation = diceAnimation.getKeyFrame(time, false);
        spriteBatch.begin();
        spriteBatch.draw(actDiceAnimation, 100, 500, 350, 180);
        spriteBatch.end();
    }



    public void dispose() {
        spriteBatch.dispose();
        diceSheet.dispose();
    }
}
