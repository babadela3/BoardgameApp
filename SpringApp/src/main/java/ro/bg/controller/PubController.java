package ro.bg.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Account;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;
import ro.bg.model.PubPicture;
import ro.bg.service.BoardGameService;
import ro.bg.service.PubPictureService;
import ro.bg.service.PubService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class PubController {

    @Autowired
    PubService pubService;

    @Autowired
    PubPictureService pubPictureService;

    @Autowired
    BoardGameService boardGameService;

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/accounts/password/reset")
    public String resetPassword() {
        return "resetPassword";
    }

    @RequestMapping(value = "/accounts/createAccount")
    public String createAccount() {
        return "createAccount";
    }

    @RequestMapping(value = "/pub/resetPassword", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendResetEmail(@RequestParam("email") String email) throws MailException, BoardGameServiceException {
        pubService.resetPassword(email);
    }

    @RequestMapping(value = "/getPub", method = RequestMethod.POST)
    public String getPub(@ModelAttribute("username") String username,
                         @ModelAttribute("password") String password,
                         RedirectAttributes rm) throws BoardGameServiceException, UnsupportedEncodingException {
        Pub pub = pubService.getPub(new Account(username, password));
        rm.addFlashAttribute("pub", pub);
        Gson gson = new Gson();
        String personString = gson.toJson(pub);
        List<PubPicture> pubPictureList = pubPictureService.getPictures(pub.getId());
        List<BoardGame> boardGames = boardGameService.getAllById(pub.getId());
        rm.addFlashAttribute("pubString", personString);
        rm.addFlashAttribute("pictures", pubPictureList);
        rm.addFlashAttribute("boardgames",boardGames);
        return "redirect:/profile";
    }


        @RequestMapping(value = "/profile")
    public String getStore(Model model) {
        return "profile";
    }


    @RequestMapping(value = "/createPub", method = RequestMethod.POST)
    public String createAccount(@RequestBody Pub pub) {
        pubService.createPub(pub);
        return "resetPassword";
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
    public String editProfile(@ModelAttribute("pub") String pubString, Model model) {
        Gson gson = new Gson();
        Pub pub = gson.fromJson(pubString, Pub.class);
        List<BoardGame> boardGames = boardGameService.getAllById(pub.getId());
        List<PubPicture> pubPictureList = pubPictureService.getPictures(pub.getId());
        model.addAttribute("boardgames",boardGames);
        model.addAttribute("pub", pub);
        model.addAttribute("pictures", pubPictureList);
        return "edit";
    }

    @RequestMapping(value = "/updatePub", method = RequestMethod.POST, headers = "content-type=multipart/*")
    public String updatePub(@RequestParam("file") MultipartFile file,
                            @ModelAttribute("id") int id,
                            @ModelAttribute("email") String email,
                            @ModelAttribute("name") String name,
                            @ModelAttribute("address") String address,
                            @ModelAttribute("description") String description,
                            Model model) throws BoardGameServiceException {
        try {
            pubService.updatePubInformation(new Pub(id,email,name,address,description,file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pub pub = pubService.getPubByEmail(email);
        model.addAttribute("pub", pub);
        return "edit";
    }

    @RequestMapping(value = "/profilePhoto", method = RequestMethod.GET)
    public void showProfilePhoto(@RequestParam("email") String email, HttpServletResponse response,HttpServletRequest request)
            throws ServletException, IOException {
        Pub pub = pubService.getPubByEmail(email);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(pub.getPicture());

        response.getOutputStream().close();
    }

    @RequestMapping(value = "/redirectProfile", method = RequestMethod.POST)
    public String redirectProfile(@ModelAttribute("email") String email, RedirectAttributes rm) {
        Pub pub = pubService.getPubByEmail(email);
        List<BoardGame> boardGames = boardGameService.getAllById(pub.getId());
        List<PubPicture> pubPictureList = pubPictureService.getPictures(pub.getId());
        Gson gson = new Gson();
        String personString = gson.toJson(pub);
        rm.addFlashAttribute("pub", pub);
        rm.addFlashAttribute("pubString", personString);
        rm.addFlashAttribute("pictures", pubPictureList);
        rm.addFlashAttribute("boardgames",boardGames);
        return "redirect:/profile";
    }
}