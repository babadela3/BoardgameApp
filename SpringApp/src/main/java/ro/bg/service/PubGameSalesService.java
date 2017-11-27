package ro.bg.service;

import ro.bg.model.Pub;
import ro.bg.model.PubGameSales;

import java.util.List;

public interface PubGameSalesService {

    void createPubGameSale(PubGameSales pubGameSales);

    void updatePubGameSale(PubGameSales pubGameSales);

    void deletePubGameSale(PubGameSales pubGameSales);

    List<PubGameSales> getAllGames(Pub pub);
}
