package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.controller.GameController;
import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.CardType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CardLoaderUtils {

    public static List<Button> loadFourteenCards(){
        List<Button> cards = loadAllCards();
        Collections.shuffle(cards);

        return cards.subList(0, 14);
    }

    public static List<Button> loadAllCards(){
        List<Button> cards = new ArrayList<>();
        for (Card card : Card.values())
        {
            cards.add(new Button(){{
                setText(String.format("%s\nCOST: %d\nPOINTS: %d", card.name, card.cost, card.score));
                setStyle(String.format(
                        "-fx-background-color: %s;" +
                        "-fx-font-family: Bookman Old Style;" +
                                "-fx-font-size: 10pt;", card.cardType.hexColor));
                setPrefSize(110, 130);
                FlowPane.setMargin(this,  new Insets(5));
                setUserData(card);
            }});
        }
        return cards;
    }
}
