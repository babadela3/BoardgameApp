package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.PubGameSalesDAO;
import ro.bg.model.Pub;
import ro.bg.model.PubGameSales;

import java.util.List;

@Service
public class PubGameSalesServiceImpl implements PubGameSalesService{

    @Autowired
    PubGameSalesDAO pubGameSalesDAO;

    @Override
    public void createPubGameSale(PubGameSales pubGameSales) {
        pubGameSalesDAO.saveAndFlush(pubGameSales);
    }

    @Override
    public void updatePubGameSale(PubGameSales pubGameSales) {
        PubGameSales oldPubGameSales = pubGameSalesDAO.getPubGameSale(pubGameSales.getPk().getPub().getId(),pubGameSales.getPk().getBoardGame().getId());
        oldPubGameSales.setPrice(pubGameSales.getPrice());
        oldPubGameSales.setCurrencyEnum(oldPubGameSales.getCurrencyEnum());
        oldPubGameSales.setStock(pubGameSales.getStock());
        pubGameSalesDAO.save(oldPubGameSales);
    }

    @Override
    public void deletePubGameSale(PubGameSales pubGameSales) {
        PubGameSales oldPubGameSales = pubGameSalesDAO.getPubGameSale(pubGameSales.getPk().getPub().getId(),pubGameSales.getPk().getBoardGame().getId());
        pubGameSalesDAO.delete(oldPubGameSales);
    }

    @Override
    public List<PubGameSales> getAllGames(Pub pub) {
        return pubGameSalesDAO.getAllGames(pub.getId());
    }
}
