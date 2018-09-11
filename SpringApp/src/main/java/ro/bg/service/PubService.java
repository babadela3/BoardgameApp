package ro.bg.service;

import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;
import ro.bg.model.PubPicture;
import ro.bg.model.dto.PubDTO;

import java.util.List;

public interface PubService {

    Pub getPub(Account account) throws BoardGameServiceException;

    Pub getPubByEmail(String email);

    void createPub(Pub pub) throws BoardGameServiceException;

    void updatePubInformation(Pub pub);

    void changePassword(Pub pub) throws BoardGameServiceException;

    void deletePub(Pub pub);

    void resetPassword(String mail) throws BoardGameServiceException;

    List<Pub> getPubs();

    List<Pub> getPubsByName(String name);

    PubDTO getPubInfo(int id);

    PubPicture getPubPicture(int id);

    List<BoardGame> getPubGames(int pubId);
}
