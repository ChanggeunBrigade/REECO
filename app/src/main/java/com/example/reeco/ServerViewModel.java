package com.example.reeco;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ServerViewModel extends AndroidViewModel {
    private final LiveData<List<Server>> servers;
    private final ServerRepository serverRepository;

    public ServerViewModel(Application application) {
        super(application);
        serverRepository = new ServerRepository(application);
        servers = serverRepository.getAllServers();
    }

    LiveData<List<Server>> getServers() {
        return servers;
    }

    public void insert(Server server) {
        serverRepository.insert(server);
    }

    public void delete(Server server) {
        serverRepository.delete(server);
    }

    public Server findServerByName(String name) {
        return serverRepository.findServerByName(name);
    }
}
