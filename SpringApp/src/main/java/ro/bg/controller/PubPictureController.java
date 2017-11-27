package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.PubPicture;
import ro.bg.service.PubPictureService;

@Controller
public class PubPictureController {

    @Autowired
    PubPictureService pubPictureService;

    @RequestMapping(value = "/addPicture", method = RequestMethod.POST)
    public String addPicture(@RequestBody PubPicture pubPicture) {
        pubPictureService.addPicture(pubPicture);
        return "";
    }

    @RequestMapping(value = "/deletePicture", method = RequestMethod.POST)
    public String deletePicture(@RequestBody PubPicture pubPicture) {
        pubPictureService.deletePicture(pubPicture);
        return "";
    }
}
