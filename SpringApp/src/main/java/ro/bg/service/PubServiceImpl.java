package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.PubDAO;
import ro.bg.model.Pub;
import org.apache.commons.validator.routines.EmailValidator;

@Service
public class PubServiceImpl implements PubService{

    @Autowired
    PubDAO pubDAO;

    @Override
    public void createPub(Pub pub) {
        if(EmailValidator.getInstance().isValid(pub.getEmail()) && pubDAO.findByEmail(pub.getEmail()) == null){
            pubDAO.saveAndFlush(pub);
        };
    }

    @Override
    public void updatePubInformation(Pub pub) {
        Pub oldPub = pubDAO.findOne(pub.getId());
        oldPub.setName(pub.getName());
        oldPub.setPicture(pub.getPicture());
        oldPub.setDescription(pub.getDescription());
        oldPub.setAddress(pub.getAddress());
        oldPub.setBoardGames(pub.getBoardGames());
        pubDAO.save(oldPub);
    }

    @Override
    public void changePassword(Pub pub) {
        Pub oldPub = pubDAO.findOne(pub.getId());
        oldPub.setPassword(pub.getPassword());
        pubDAO.save(oldPub);
    }

    @Override
    public void deletePub(Pub pub) {
        pubDAO.delete(pub.getId());
    }
}
