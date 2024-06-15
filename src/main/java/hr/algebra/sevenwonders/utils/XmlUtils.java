package hr.algebra.sevenwonders.utils;

import hr.algebra.sevenwonders.model.Card;
import hr.algebra.sevenwonders.model.CardType;
import hr.algebra.sevenwonders.model.GameMove;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

    private static final String FILENAME = "xml/game_moves.xml";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void saveGameMove(GameMove gameMove) {

        List<GameMove> gameMoveList = readGameMoves();
        gameMoveList.add(gameMove);

        try {
            Document document = createDocument("gameMoves");

            for(GameMove gameMoveXmlElement : gameMoveList) {

                Element gameMoveElement = document.createElement("gameMove");
                document.getDocumentElement().appendChild(gameMoveElement);

                Element playerHandElement = document.createElement("hand");
                for (Card card : gameMoveXmlElement.getPlayerHandState()){

                    playerHandElement.appendChild(serializeCard(card, playerHandElement));
                }

                gameMoveElement.appendChild(createElement(document, "player",
                        gameMoveXmlElement.getPlayerName()));
                gameMoveElement.appendChild(serializeCard(gameMoveXmlElement.getCard(), gameMoveElement));
                gameMoveElement.appendChild(playerHandElement);
                gameMoveElement.appendChild(serializeScoreboard(gameMoveXmlElement.getScoreBoardState(), gameMoveElement));
                gameMoveElement.appendChild(createElement(document, "dateTime",
                        gameMoveXmlElement.getDateTime().format(formatter)));
            }

            saveDocument(document, FILENAME);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    private static Node serializeScoreboard(String[] scoreBoardState, Element parentElement) {
        Element scoreElement = parentElement.getOwnerDocument().createElement("scoreboard");
        for (String score : scoreBoardState){
            scoreElement.appendChild(createElement(parentElement.getOwnerDocument(), "score", score));
        }
        return  scoreElement;
    }

    private static Node serializeCard(Card card, Element parentElement) {
        Element cardElement = parentElement.getOwnerDocument().createElement("card");
        cardElement.appendChild(createElement(parentElement.getOwnerDocument(), "cardName", card.name()));
        return  cardElement;
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, element, null);
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    public static List<GameMove> readGameMoves() {

        List<GameMove> gameMoveList = new ArrayList<>();

        File xmlFile = new File(FILENAME);

        if (xmlFile.exists()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new File(FILENAME));
                gameMoveList.addAll(
                        processGameMoveNodes(document.getDocumentElement(), ""));
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                ex.printStackTrace();
            }
        }

        return gameMoveList;
    }

    private static List<GameMove> processGameMoveNodes(Node node, String indent) {

        List<GameMove> gameMoveList = new ArrayList<>();

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element nodeElement = (Element) node;

            NodeList nodeList = nodeElement.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {

                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    Element childElement = (Element) nodeList.item(i);

                    GameMove gameMove = new GameMove();

                    if ("gameMove".equals(childElement.getTagName())) {
                        NodeList gameMoveNodeList = childElement.getChildNodes();

                        for (int j = 0; j < gameMoveNodeList.getLength(); j++) {
                            if (gameMoveNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element gameMoveChildElement = (Element) gameMoveNodeList.item(j);
                                switch (gameMoveChildElement.getTagName()) {
                                    case "player" -> gameMove.setPlayerName(gameMoveChildElement.getTextContent());
                                    case "card" -> gameMove.setCard(deserializeCard(gameMoveChildElement));
                                    case "hand" -> gameMove.setPlayerHandState(deserializeHand(gameMoveChildElement));
                                    case "scoreboard" -> gameMove.setScoreBoardState(deserializeScoreboard(gameMoveChildElement));
                                    case "dateTime" -> gameMove.setDateTime(LocalDateTime.parse(gameMoveChildElement.getTextContent(), formatter));
                                }
                            }
                        }
                    }

                    gameMoveList.add(gameMove);
                }
            }
        }

        return gameMoveList;
    }

    private static String[] deserializeScoreboard(Element element) {
        ArrayList<String> scoreboard = new ArrayList<>();
        NodeList items = element.getChildNodes();
        for (int i=0; i < items.getLength(); i++){
            if(items.item(i).getNodeType() == Node.ELEMENT_NODE){
                scoreboard.add((items.item(i)).getTextContent());
            }
        }
        return scoreboard.toArray(String[]::new);
    }

    private static Card deserializeCard(Element element) {

        String cardName = (element.getChildNodes()).item(1).getTextContent();
        return Card.valueOf(cardName);
    }

    private static Card[] deserializeHand(Element element) {
        ArrayList<Card> hand = new ArrayList<>();
        NodeList items = element.getChildNodes();
        for (int i=1; i < items.getLength(); i++){
            if(items.item(i).getNodeType() == Node.ELEMENT_NODE){
                hand.add(deserializeCard((Element) items.item(i)));
            }
        }
        return hand.toArray(Card[]::new);
    }

    public static void createNewReplayFile() {
        try {
            Document document = createDocument("gameMoves");
            saveDocument(document, FILENAME);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

}
