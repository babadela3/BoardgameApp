package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.Pub;
import ro.bg.model.User;
import ro.bg.model.constants.AccountTypeEnum;
import ro.bg.model.dto.EventDTO;
import ro.bg.model.dto.UserDTO;
import ro.bg.service.UserService;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

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
    public ResponseEntity<Object> getUser(@ModelAttribute("email") String email,
                                         @ModelAttribute("password") String password) {
        User user = null;
        try {
            user = userService.getUser(new Account(email,password));
        } catch (BoardGameServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @RequestMapping(value = "/createBgUser", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@ModelAttribute("email") String email,
                                             @ModelAttribute("password") String password,
                                             @ModelAttribute("name") String name,
                                             @ModelAttribute("town") String town,
                                             @ModelAttribute("photo") String photo) {
        User user = null;
        user = new User(email, password, name, town, AccountTypeEnum.BGACCOUNT, Base64.decodeBase64(photo));
        try {
            userService.createUser(user);
        } catch (BoardGameServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/user/sendPassword", method = RequestMethod.POST)
    public ResponseEntity<Object> getPassword(@ModelAttribute("email") String email) {
        try {
            userService.sendPassword(email);
        } catch (BoardGameServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/user/getProfileImage", method = RequestMethod.POST)
    public ResponseEntity<Object> getProfileImage(@ModelAttribute("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfileImage(email));
    }

    @RequestMapping(value = "/getUsersByName", method = RequestMethod.POST)
    public ResponseEntity<Object> getUsersByName(@ModelAttribute("name") String name){
        List<User> users = userService.getUsersByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @RequestMapping(value = "/profilePhotoUser", method = RequestMethod.GET)
    public void showProfilePhoto(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {
        User user = userService.getUser(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(user.getProfilePicture());

        response.getOutputStream().close();
    }

    @RequestMapping(value = "/searchUser", method = RequestMethod.POST)
    public ResponseEntity<Object> searchUser(@ModelAttribute("searchUserId") String searchUserId,
                                             @ModelAttribute("userId") String userId) {
        UserDTO userDTO = userService.getSearchUser(Integer.parseInt(searchUserId),Integer.parseInt(userId));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
