package com.example.reeco;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ServerRepository {
    private ServerDao serverDao;
    private LiveData<List<Server>> allServers;

    public ServerRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        serverDao = db.serverDao();
        allServers = serverDao.getServers();
    }

    LiveData<List<Server>> getAllServers() {
        return serverDao.getServers();
    }

    void insert(Server server) {
        serverDao.insert(server);
    }

    void delete(Server server) {
        serverDao.delete(server);
    }

    Server findServerByName(String name) {
        return serverDao.findServerByName(name);
    }
}
