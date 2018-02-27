package ro.bg.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @RequestMapping(value = "/accounts/createAccount")
    public String createAccount() {return "createAccount";}

    @RequestMapping(value = "/pub/resetPassword", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendResetEmail(@RequestParam("email") String email) throws MailException, BoardGameServiceException {
        pubService.resetPassword(email);
    }

    @RequestMapping(value = "/getPub", method = RequestMethod.POST)
    public String getPub(@ModelAttribute("username") String username,
                         @ModelAttribute("password") String password,
                         RedirectAttributes rm) throws BoardGameServiceException {
        Pub pub = pubService.getPub(new Account(username,password));
        rm.addFlashAttribute("pub", pub);
        Gson gson = new Gson();
        String personString = gson.toJson(pub);
        rm.addFlashAttribute("pubString", personString);
        return "redirect:/store";
    }


    @RequestMapping(value="/store")
    public String getStore(Model model){
        return "store";
    }


    @RequestMapping(value = "/createPub", method = RequestMethod.POST)
    public String createAccount(@RequestBody Pub pub) {
        pubService.createPub(pub);
        return "resetPassword";
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

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public String editProfile(@ModelAttribute("pub") String pubString,Model model) {
        System.out.println(pubString);
        Gson gson = new Gson();
        Pub pub = gson.fromJson(pubString, Pub.class);
        System.out.println(pub);
        model.addAttribute("pub", pub);
        return "editProfile";
    }

}