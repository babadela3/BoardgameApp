package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.bg.dao.PubDAO;
import ro.bg.dao.PubPictureDAO;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.exception.ExceptionMessage;
import ro.bg.model.Account;
import ro.bg.model.BoardGame;
import ro.bg.model.Pub;
import org.apache.commons.validator.routines.EmailValidator;
import ro.bg.model.PubPicture;
import ro.bg.model.dto.PubDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PubServiceImpl implements PubService{

    @Autowired
    public JavaMailSender javaMailSender;

    @Autowired
    PubDAO pubDAO;

    @Autowired
    PubPictureDAO pubPictureDAO;

    @Override
    public Pub getPub(Account account) throws BoardGameServiceException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Pub pub = pubDAO.findByEmail(account.getUsername());
        if(pub != null){
            if(!passwordEncoder.matches(account.getPassword(),pub.getPassword())){
                throw new BoardGameServiceException(ExceptionMessage.MISSING_PUB);
            }
        }
        else {
            throw new BoardGameServiceException(ExceptionMessage.MISSING_PUB);
        }

        pub.setPubPictures(null);
        pub.setPassword(null);
        pub.setReservations(null);
        pub.setBoardGames(null);
        return pub;
    }

    @Override
    public Pub getPubByEmail(String email) {
        Pub pub = pubDAO.findByEmail(email);
        pub.setPubPictures(null);
        pub.setPassword(null);
        pub.setReservations(null);
        pub.setBoardGames(null);
        return pub;
    }

    @Override
    public void createPub(Pub pub) {
        if(EmailValidator.getInstance().isValid(pub.getEmail()) && pubDAO.findByEmail(pub.getEmail()) == null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            pub.setPassword(passwordEncoder.encode(pub.getPassword()));
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

    @Override
    public void resetPassword(String mail) throws MailException, BoardGameServiceException {
        Pub pub = pubDAO.findByEmail(mail);
        if(pub != null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setFrom("bgboardgame@gmail.com");
            message.setSubject("Reset Password");
            message.setText("Hi,\n\nWe got a request to reset your BoardGameAndroidDTO password\n\nYour password is: " + pub.getPassword() + "\n\nIf you want to change the password, " +
                    "click to follow link to change your password: http://localhost:8081/accounts/password/change/" + pub.getEmail() +"\n\nIf you ignore this message, " +
                    "your password will not be change");
            javaMailSender.send(message);
        }
        else{
            throw new BoardGameServiceException(ExceptionMessage.EMAIL_NOT_EXISTING);
        }
    }

    @Override
    public List<Pub> getPubs() {
        List<Pub> pubs = pubDAO.findAll();
        for(Pub pub : pubs) {
            pub.setBoardGames(null);
            pub.setGameReservations(null);
            pub.setPubPictures(null);
            pub.setPassword(null);
            pub.setReservations(null);
            pub.setPicture(null);
        }
        return pubs;
    }

    @Override
    public List<Pub> getPubsByName(String name) {
        List<Pub> pubsDB = pubDAO.findByNameContaining(name);
        for(Pub pub : pubsDB) {
            pub.setBoardGames(null);
            pub.setGameReservations(null);
            pub.setPubPictures(null);
            pub.setPassword(null);
            pub.setReservations(null);
            pub.setPicture(null);
        }
        return pubsDB;
    }

    @Override
    public PubDTO getPubInfo(int id) {
        PubDTO pubDTO = new PubDTO();
        Pub pub = pubDAO.findOne(id);
        pubDTO.setId(pub.getId());
        pubDTO.setName(pub.getName());
        pubDTO.setEmail(pub.getEmail());
        pubDTO.setAddress(pub.getAddress());
        pubDTO.setDescription(pub.getDescription());
        pubDTO.setPicture(pub.getPicture());
        List<Integer> picturesId = new ArrayList<>();
        for(PubPicture pubPicture : pub.getPubPictures()){
            picturesId.add(pubPicture.getId());
        }
        pubDTO.setPhotoIds(picturesId);
        return pubDTO;
    }

    @Override
    public PubPicture getPubPicture(int id) {
        PubPicture pubPicture = pubPictureDAO.findOne(id);
        pubPicture.setPub(null);
        return pubPicture;
    }

}
