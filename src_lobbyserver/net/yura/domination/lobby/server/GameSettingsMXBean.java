package net.yura.domination.lobby.server;

/**
 * @author Yura Mamyrin
 */
public interface GameSettingsMXBean {

    void setAIWait(int a);
    int getAIWait();

    void saveGame(int id) throws Exception;
}
