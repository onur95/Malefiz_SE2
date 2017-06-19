package com.mygdx.malefiz.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Klaus on 09.06.2017.
 */

public class SoundManager {
    private final Sound yourTurn;
    private final Sound playerKicked;
    private final Sound placeBlock;
    private final Sound moveFigure;
    private final Sound winner;
    private final Sound loser;

    public SoundManager(){
        yourTurn= Gdx.audio.newSound(Gdx.files.internal("soundeffects/your-turn.wav"));
        playerKicked = Gdx.audio.newSound(Gdx.files.internal("soundeffects/kicked-player-move-back.wav"));
        placeBlock = Gdx.audio.newSound(Gdx.files.internal("soundeffects/place-block.wav"));
        moveFigure = Gdx.audio.newSound(Gdx.files.internal("soundeffects/move-figure2.wav"));
        winner=Gdx.audio.newSound(Gdx.files.internal("soundeffects/winner.wav"));
        loser=Gdx.audio.newSound(Gdx.files.internal("soundeffects/loser.mp3"));
    }


    public void playSound(Sounds sound){
        switch (sound){
            case MOVE:
                moveFigure.play();
                break;

            case PLAYERTURN:
                yourTurn.play();
                break;

            case PLAYERKICKED:
                playerKicked.play();
                break;

            case WINNER:
                winner.play();
                break;

            case LOSER:
                loser.play();
                break;

            default:
                placeBlock.play();
                break;
        }
    }
}
