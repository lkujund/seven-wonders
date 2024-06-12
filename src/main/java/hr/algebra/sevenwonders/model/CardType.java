package hr.algebra.sevenwonders.model;

public enum CardType {
    BROWN_RESOURCE("#663318"), GREY_TRADE("#92918d"), BLUE_CIVIL("#0179b5"), GREEN_SCIENCE("#01944d"), YELLOW_GOLD("#fcae16"), RED_MILITARY("#ea1d24");

    /*
    Brown cards (raw materials) provide one or two of the four raw material resources used in the game (wood, ore, clay brick, and stone).
Grey cards (manufactured goods) provide one of the three manufactured goods used in the game (glass, papyrus, and textiles).
Blue cards (civic structures [mistranslated as "civilian" in the game rules]): all grant a fixed number of victory points.
Green cards (scientific structures): each card has one of three symbols. Combinations of the symbols are worth victory points.
Yellow cards (commercial structures) have several effects: they can grant coins, resources, and/or victory points or decrease the cost of buying resources from neighbors.
Red cards (military structures) contain "shield" symbols; these are added together to give a player's military strength, which is used in conflict resolution at the end of each age.
    */

    public final String hexColor;
    CardType(String hexColor) {
        this.hexColor = hexColor;
    }
}
