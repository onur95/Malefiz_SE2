package com.mygdx.malefiz.cheats;

import com.badlogic.gdx.Gdx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CheatEngineObserver{
    private String code = "";
    private PropertyChangeSupport pcs;
    private CheatEngine ce;

    public CheatEngineObserver(CheatEngine ce){
        this.ce = ce;
        this.pcs = new PropertyChangeSupport(this);
    }

    public void setListener(CheatEngineObserver ceo) {
        CheatListener ecl = new CheatListener();
        ceo.addPropertyChangeListener(ecl);

        Gdx.app.log("CheatEngineObserver.setListener on ceo", "Executed.");
    }

    public void setCheat(String codeEntry){
        String oldCode = this.code;
        this.code = codeEntry;
        pcs.firePropertyChange("Enabled Cheat = true", oldCode, code); // Flag, Old-Param, New-Param

        Gdx.app.log("CEO.setCheat", "Flag shot.");

    }

    private void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    /** Subclassed Eventlistener
     */
    private class CheatListener implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent event){
            Gdx.app.log("CEO>>CheatListener", "Listening for incomming Cheats.");

            if(event.getPropertyName().equals("Enabled Cheat = true")){
                Gdx.app.log("CEO>>CheatListener", "Flag received & executed.");

                // Todo: Alert went off. Inform other players of cheating player
                // #1 Register Message in Network
                // #2 Create Message & Send it


                // Finally execute the CheatCode
                ce.cheatCaller(code);
            }
        }
    }
}