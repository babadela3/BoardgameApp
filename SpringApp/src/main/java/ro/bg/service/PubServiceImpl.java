package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.bg.dao.PubDAO;
import ro.bg.dao.PubPictureDAO;
import ro.bg.exception.BoardGameServiceException;
import ro.bg.exception.ExceptionMessage;
import ro.bg.model.Account;
import ro.bg.model.Pub;
import org.apache.commons.validator.routines.EmailValidator;
import ro.bg.model.PubPicture;

import java.util.HashSet;
import java.util.List;

@Service
public class PubServiceImpl implements PubService{

    @Autowired
    PubDAO pubDAO;

    @Autowired
    PubPictureDAO pubPictureDAO;

    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public Pub getPub(Account account) throws BoardGameServiceException {
        Pub pub = pubDAO.findByEmailAndPassword(account.getUsername(),account.getPassword());
        if(pub == null){
            throw new BoardGameServiceException(ExceptionMessage.MISSING_PUB);
        }

        pub.setPubPictures(null);
        pub.setPassword(null);
        pub.setReservations(null);
        return pub;
    }

    @Override
    public Pub getPubByEmail(String email) {
        return pubDAO.findByEmail(email);
    }

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

    @Override
    public void resetPassword(String mail) throws MailException, BoardGameServiceException {
        Pub pub = pubDAO.findByEmail(mail);
        if(pub != null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail);
            message.setFrom("bgboardgame@gmail.com");
            message.setSubject("Reset Password");
            message.setText("Hi,\n\nWe got a request to reset your BoardGame password\n\nYour password is: " + pub.getPassword() + "\n\nIf you want to change the password, " +
                    "click to follow link to change your password: http://localhost:8081/accounts/password/change/" + pub.getEmail() +"\n\nIf you ignore this message, " +
                    "your password will not be change");
            javaMailSender.send(message);
        }
        else{
            throw new BoardGameServiceException(ExceptionMessage.EMAIL_NOT_EXISTING);
        }
    }
}
