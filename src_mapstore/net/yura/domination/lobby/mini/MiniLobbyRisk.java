package net.yura.domination.lobby.mini;

import java.util.List;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import net.yura.domination.engine.OnlineRisk;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.mapstore.Map;
import net.yura.domination.mapstore.MapChooser;
import net.yura.lobby.mini.MiniLobbyClient;
import net.yura.lobby.mini.MiniLobbyGame;
import net.yura.lobby.model.Game;
import net.yura.lobby.model.GameType;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.util.Properties;
import net.yura.swingme.core.CoreUtil;

/**
 * @author Yura Mamyrin
 */
public abstract class MiniLobbyRisk implements MiniLobbyGame,OnlineRisk {

    private static final Logger logger = Logger.getLogger( MiniLobbyRisk.class.getName() );

    private Risk myrisk;
    protected MiniLobbyClient lobby;

    public MiniLobbyRisk(Risk risk) {
        myrisk = risk;
    }

    public void addLobbyGameMoveListener(MiniLobbyClient lgl) {
        lobby = lgl;
    }

    public Properties getProperties() {
        return CoreUtil.wrap( TranslationBundle.getBundle() );
    }

    public boolean isMyGameType(GameType gametype) {
        return RiskUtil.GAME_NAME.equals( gametype.getName() );
    }

    private boolean openGame;

    public void objectForGame(Object object) {
        if (object instanceof RiskGame) {
            RiskGame thegame = (RiskGame)object;
            Player player = thegame.getPlayer(lobby.whoAmI());
            String address = player==null?"_watch_":player.getAddress();
            myrisk.setOnlinePlay(this);
            myrisk.setAddress(address);
            myrisk.setGame(thegame);
            openGame = true;
        }
// TODO remove this legacy message system
        else if (object instanceof java.util.Map) {
            java.util.Map map = (java.util.Map)object;

            String command = (String)map.get("command");
            if ("game".equals(command)) {
                String address = (String)map.get("playerId");
                RiskGame thegame = (RiskGame)map.get("game");
                myrisk.setOnlinePlay(this);
                myrisk.setAddress(address);
                myrisk.setGame(thegame);
                openGame = true;
            }
            else {
                System.out.println("unknown command "+command+" "+map);
            }
        }
// END TODO
        else {
            System.out.println("unknown object "+object);
        }
    }

    public void stringForGame(String message) {
        if (openGame) {
            myrisk.parserFromNetwork(message);
        }
        else {
            logger.info("GAME NOT OPEN SO IGNORING: "+message);
        }
    }

    public void disconnected() {
        myrisk.disconnected();
    }




    WeakHashMap mapping = new WeakHashMap();

    public Icon getIconForGame(Game game) {

        // if local map
        Map map = MapChooser.createMap( RiskUtil.getMapNameFromLobbyStartGameOption(game.getOptions()) );
        // TODO what if this is not a local map??

        mapping.put(game, map); // keep a strong ref to the map as long as we have a strong ref to the game

        return MapChooser.getLocalIconForMap(map);
    }

    public String getGameDescription(Game game) {
        return RiskUtil.getGameDescriptionFromLobbyStartGameOption( game.getOptions() );
    }



    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMW OnlineRisk MWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
    // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW

    public void sendUserCommand(final String messagefromgui) {
        lobby.sendGameMessage(messagefromgui);
    }
    public void sendGameCommand(String mtemp) {
	// this happens for game commands on my go
        logger.info("ignore GameCommand "+mtemp );
    }
    public void closeGame() {
        openGame = false;
        lobby.closeGame();
    }

    public void playerRenamed(String oldName, String newName, String newAddress, int newType) {
        if (oldName.equals(lobby.whoAmI())) {
            myrisk.setAddress("_watch_");
        }
        if (newName.equals(lobby.whoAmI())) {
            myrisk.setAddress(newAddress);
        }
    }
}
