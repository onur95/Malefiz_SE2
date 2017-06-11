package com.mygdx.malefiz;

import com.badlogic.gdx.Gdx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheatEngineObserver{
    static String code = "";

    PropertyChangeSupport pcs;
    CheatEngine ce;

    public void setListener(CheatEngineObserver ceo) {
        CheatListener ecl = new CheatListener();
        ceo.addPropertyChangeListener(ecl);

        Gdx.app.log("CheatEngineObserver.setListener on ceo", "Executed.");
    }

    public CheatEngineObserver(){
        pcs = new PropertyChangeSupport(this);
    }

    public void setCheat(String codeEntry){
        String oldCode = this.code;
        this.code = codeEntry;
        pcs.firePropertyChange("Enabled Cheat = true", oldCode, code); // Flag, Old-Param, New-Param

        Gdx.app.log("CEO.setCheat", "Flag shot.");

    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    /** Subclassed Eventlistener
     */
    public class CheatListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent event){
            Gdx.app.log("CEO>>CheatListener", "Listening for incomming Cheats.");

            if(event.getPropertyName().equals("Enabled Cheat = true")){
                Gdx.app.log("CEO>>CheatListener", "Flag received & executed.");

                // Todo: Alert went off. Inform other players of cheating player
                // #1 Register Message in Network
                // #2 Create Message & Send it

                // Finally execute the CheatCode
                ce = new CheatEngine();
                ce.cheatCaller(CheatEngineObserver.code);
            }
        }
    }
}