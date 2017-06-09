package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Klaus on 09.06.2017.
 */

public class SoundManager {
    private final Sound yourTurn;
    private final Sound kickPlayer;
    private final Sound kickedPlayerMove;
    private final Sound placeBlock;
    private final Sound kickedOwnFigure;
    private final Sound moveFigure;


    public SoundManager(){
        yourTurn= Gdx.audio.newSound(Gdx.files.internal("soundeffects/your-turn.wav"));
        kickPlayer = Gdx.audio.newSound(Gdx.files.internal("soundeffects/kick-player.wav"));
        kickedPlayerMove = Gdx.audio.newSound(Gdx.files.internal("soundeffects/kicked-player-move-back.wav"));
        placeBlock = Gdx.audio.newSound(Gdx.files.internal("soundeffects/place-block.wav"));
        kickedOwnFigure = Gdx.audio.newSound(Gdx.files.internal("soundeffects/own-figure-kicked.wav"));
        moveFigure = Gdx.audio.newSound(Gdx.files.internal("soundeffects/move-figure2.wav"));
    }


    public void playSound(Sounds sound){
        switch (sound){
            case MOVE:
                moveFigure.play();
                break;

            case PLAYERTURN:
                yourTurn.play();
                break;
        }
    }
}
