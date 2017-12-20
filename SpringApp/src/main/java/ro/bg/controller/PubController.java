package ro.bg.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.Pub;
import ro.bg.service.PubService;

@Controller
public class PubController {

    @Autowired
    PubService pubService;

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/accounts/password/reset")
    public String resetPassword() {return "resetPassword";}

    @RequestMapping(value = "/pub/resetPassword", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendResetEmail(@RequestParam("email") String email) throws MailException, BoardGameServiceException {
        pubService.resetPassword(email);
    }

    @RequestMapping(value = "/getPub", method = RequestMethod.POST)
    @ResponseBody
    public String getPub(@RequestBody Account account) throws BoardGameServiceException {
        Pub pub = pubService.getPub(account);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(pub);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        return json;
    }

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
