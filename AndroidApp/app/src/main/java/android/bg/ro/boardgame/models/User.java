package android.bg.ro.boardgame.models;

import android.bg.ro.boardgame.models.constrants.AccountTypeEnum;

import java.util.Arrays;
import java.util.Set;

public class User {

    private int id;

    private String email;

    private String password;

    private String name;

    private String town;

    private AccountTypeEnum accountType;

    private byte[] profilePicture;

    private Set<BoardGame> boardGames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<BoardGame> getBoardGames() {
        return boardGames;
    }

    public void setBoardGames(Set<BoardGame> boardGames) {
        this.boardGames = boardGames;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", town='" + town + '\'' +
                ", accountType=" + accountType +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                ", boardGames=" + boardGames +
                '}';
    }
}
