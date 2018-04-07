package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.PubPictureDAO;
import ro.bg.model.PubPicture;

import java.util.List;

@Service
public class PubPictureServiceImpl implements PubPictureService{

    @Autowired
    PubPictureDAO pubPictureDAO;

    @Override
    public void addPicture(PubPicture pubPicture) {
        pubPictureDAO.saveAndFlush(pubPicture);
    }

    @Override
    public void deletePictures(List<Integer> ids) {
        for(int id : ids){
            pubPictureDAO.delete(id);
        }
    }

    @Override
    public PubPicture getPicture(int id) {
        return pubPictureDAO.findOne(id);
    }

    @Override
    public List<PubPicture> getPictures(int pubId) {
        List<PubPicture> pubPictures = pubPictureDAO.findByPubId(pubId);
        for(PubPicture pubPicture : pubPictures){
            pubPicture.setPub(null);
        }
        return pubPictures;
    }
}
