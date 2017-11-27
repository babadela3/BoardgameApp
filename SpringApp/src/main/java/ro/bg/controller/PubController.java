package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ro.bg.model.Pub;
import ro.bg.service.PubService;

@Controller
public class PubController {

    @Autowired
    PubService pubService;

    @RequestMapping(value = "/createPub", method = RequestMethod.POST)
    public String createAccount(@RequestBody Pub pub) {
        pubService.createPub(pub);
        return "";
    }

    @RequestMapping(value = "/updatePub", method = RequestMethod.POST)
    public String updateAccount(@RequestBody Pub pub) {
        pubService.updatePubInformation(pub);
        return "";
    }

    @RequestMapping(value = "/changePasswordPub", method = RequestMethod.POST)
    public String changePassword(@RequestBody Pub pub) {
        pubService.changePassword(pub);
        return "";
    }

    @RequestMapping(value = "/deletePub", method = RequestMethod.POST)
    public String deleteAccount(@RequestBody Pub pub) {
        pubService.deletePub(pub);
        return "";
    }
}
