package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ro.bg.model.User;
import ro.bg.service.UserService;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(@RequestBody User user) {
        userService.createUser(user);
        return "";
    }

    @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public String updateAccount(@RequestBody User user) {
        userService.updatePersonalInformation(user);
        return "";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestBody User user) {
        userService.changePassword(user);
        return "";
    }

    @RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
    public String deleteAccount(@RequestBody User user) {
        userService.deleteUser(user);
        return "";
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public void getPub(@ModelAttribute("email") String email,
                         @ModelAttribute("password") String password){
        System.out.println(email + " " + password);
    }
}
