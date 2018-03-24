package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.model.Pub;
import ro.bg.model.PubPicture;
import ro.bg.service.PubPictureService;
import ro.bg.service.PubService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PubPictureController {

    @Autowired
    PubPictureService pubPictureService;
    @Autowired
    PubService pubService;

    @RequestMapping(value = "/addPhoto", method = RequestMethod.POST, headers = "content-type=multipart/*")
    public String addPhoto(@RequestParam("file") MultipartFile file,
                           @ModelAttribute("email") String email,
                           Model model) throws BoardGameServiceException {
        Pub pub = pubService.getPubByEmail(email);
        try {
            pubPictureService.addPicture(new PubPicture(pub, file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("pub", pub);
        return "edit";
    }

    @RequestMapping(value = "/deletePicture", method = RequestMethod.POST)
    public String deletePicture(@RequestBody PubPicture pubPicture) {
        pubPictureService.deletePicture(pubPicture);
        return "";
    }

    @RequestMapping(value = "/pubPicture", method = RequestMethod.GET)
    public void showImage(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException {
        PubPicture pubPicture = pubPictureService.getPicture(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(pubPicture.getPicture());
        response.getOutputStream().close();
    }
}
