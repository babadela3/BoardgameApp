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
    public void deletePicture(PubPicture pubPicture) {
        pubPictureDAO.delete(pubPicture.getId());
    }

    @Override
    public PubPicture getPicture(int id) {
        return pubPictureDAO.findOne(id);
    }

    @Override
    public List<PubPicture> getPictures(int pubId) {
        return pubPictureDAO.findByPubId(pubId);
    }
}
