package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Pub;
import ro.bg.model.PubGameSales;
import ro.bg.service.PubGameSalesService;

import java.util.List;

@Controller
public class PubGameSalesController {

    @Autowired
    PubGameSalesService pubGameSalesService;

    @RequestMapping(value = "/createPubGameSale", method = RequestMethod.POST)
    public String createPubGameSale(@RequestBody PubGameSales pubGameSales) {
        pubGameSalesService.createPubGameSale(pubGameSales);
        return "";
    }

    @RequestMapping(value = "/updatePubGameSale", method = RequestMethod.POST)
    public String updatePubGameSale(@RequestBody PubGameSales pubGameSales) {
        pubGameSalesService.updatePubGameSale(pubGameSales);
        return "";
    }

    @RequestMapping(value = "/deletePubGameSale", method = RequestMethod.POST)
    public String deletePubGameSale(@RequestBody PubGameSales pubGameSales) {
        pubGameSalesService.deletePubGameSale(pubGameSales);
        return "";
    }

    @RequestMapping(value = "/getPubGameSale", method = RequestMethod.POST)
    public String getGames(@RequestBody Pub pub) {
        List<PubGameSales> pubGameSalesList = pubGameSalesService.getAllGames(pub);
        for(PubGameSales pubGameSales : pubGameSalesList){
            System.out.println(pubGameSales);
        }
        return "";
    }
}
