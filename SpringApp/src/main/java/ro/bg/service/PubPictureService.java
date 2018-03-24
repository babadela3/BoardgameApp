package ro.bg.service;

import ro.bg.model.PubPicture;

import java.util.List;

public interface PubPictureService {

    void addPicture(PubPicture pubPicture);

    void deletePicture(PubPicture pubPicture);

    PubPicture getPicture(int id);

    List<PubPicture> getPictures(int pubId);
}
