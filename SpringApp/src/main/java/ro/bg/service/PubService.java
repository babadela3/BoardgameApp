package ro.bg.service;

import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;

import java.util.List;

public interface PubService {

    Pub getPub(Account account) throws BoardGameServiceException;

    Pub getPubByEmail(String email);

    void createPub(Pub pub);

    void updatePubInformation(Pub pub);

    void changePassword(Pub pub);

    void deletePub(Pub pub);

    void resetPassword(String mail) throws BoardGameServiceException;
}
