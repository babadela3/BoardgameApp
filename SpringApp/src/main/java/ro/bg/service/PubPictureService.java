package ro.bg.service;

import ro.bg.model.PubPicture;

import java.util.List;

public interface PubPictureService {

    void addPicture(PubPicture pubPicture);

    void deletePictures(List<Integer> ids);;

    PubPicture getPicture(int id);

    List<PubPicture> getPictures(int pubId);
}
