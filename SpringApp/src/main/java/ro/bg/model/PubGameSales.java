package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ro.bg.model.constants.CurrencyEnum;

import javax.persistence.*;

@Entity
@Table(name = "pub_game_sales")
@AssociationOverrides({
        @AssociationOverride(name = "pk.pub",
                joinColumns = @JoinColumn(name = "PK_PUB_ID")),
        @AssociationOverride(name = "pk.boardGame",
                joinColumns = @JoinColumn(name = "PK_BOARD_GAME_ID")) })
public class PubGameSales {

    @EmbeddedId
    @JsonUnwrapped
    private PubGameId pk = new PubGameId();

    @Column(name = "price")
    private float price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyEnum currencyEnum;

    @Column(name = "stock")
    private int stock;

    public PubGameId getPk() {
        return pk;
    }

    public void setPk(PubGameId pk) {
        this.pk = pk;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public CurrencyEnum getCurrencyEnum() {
        return currencyEnum;
    }

    public void setCurrencyEnum(CurrencyEnum currencyEnum) {
        this.currencyEnum = currencyEnum;
    }

    @Override
    public String toString() {
        return "PubGameSales{" +
                "pub=" + pk.getPub().getId() +
                ", boardGame=" + pk.getBoardGame().getId() +
                ", price=" + price +
                ", currencyEnum=" + currencyEnum +
                ", stock=" + stock +
                '}';
    }
}
