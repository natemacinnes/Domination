package net.yura.domination.lobby.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;
import net.yura.domination.engine.ai.AIManager;

/**
 * @author Yura Mamyrin
 */
public class GameSettings implements GameSettingsMXBean {

    public void setAIWait(int a) {
        AIManager.setWait(a);
    }

    public int getAIWait() {
        return AIManager.getWait();
    }

    public void saveGame(int id) throws Exception {

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

        for (Thread thread: threadArray) {
            if (thread instanceof ServerRisk) {
                ServerRisk risk =(ServerRisk)thread;
                if (risk.sgr.getId()==id) {
                    File file = new File("game"+id+".save");
                    FileOutputStream fout = new FileOutputStream(file);
                    risk.getGame().saveGame(fout);
                    fout.close();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("game "+id+" not found");
    }

}
