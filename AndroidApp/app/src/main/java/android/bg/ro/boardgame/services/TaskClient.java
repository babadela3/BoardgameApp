package android.bg.ro.boardgame.services;

import android.bg.ro.boardgame.models.Client;

public interface TaskClient {
    void TaskClientResult(Client client);
    void processMessage(String message);
}
