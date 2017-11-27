package ro.bg.service;

import ro.bg.model.Pub;

public interface PubService {

    void createPub(Pub pub);

    void updatePubInformation(Pub pub);

    void changePassword(Pub pub);

    void deletePub(Pub pub);
}
