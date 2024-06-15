package hr.algebra.sevenwonders.model;

public enum Card {
    //BROWN
    LUMBER_YARD("LUMBER YARD", CardType.BROWN_RESOURCE, 0, 1),
    STONE_PIT("STONE PIT", CardType.BROWN_RESOURCE, 0, 1),
    CLAY_POOL("CLAY POOL", CardType.BROWN_RESOURCE, 0, 1),
    ORE_VEIN("ORE VEIN", CardType.BROWN_RESOURCE, 0, 1),
    TREE_FARM("TREE FARM", CardType.BROWN_RESOURCE, 1, 2),
    EXCAVATION("EXCAVATION", CardType.BROWN_RESOURCE, 1, 2),
    CLAY_PIT("CLAY PIT", CardType.BROWN_RESOURCE, 1, 2),
    TIMBER_YARD("TIMBER YARD", CardType.BROWN_RESOURCE, 1, 2),
    FOREST_CAVE("FOREST CAVE", CardType.BROWN_RESOURCE, 1, 2),
    MINE("MINE", CardType.BROWN_RESOURCE, 1, 2),

    //GRAY
    LOOM("LOOM", CardType.GREY_TRADE, 0, 1),
    GLASSWORKS("GLASSWORKS", CardType.GREY_TRADE, 0, 1),
    PRESS("PRESS", CardType.GREY_TRADE, 0, 1),

    //BLUE
    PAWNSHOP("PAWNSHOP", CardType.BLUE_CIVIL, 0, 3),
    BATHS("BATHS", CardType.BLUE_CIVIL, 0, 3),
    ALTAR("ALTAR", CardType.BLUE_CIVIL, 0, 2),
    THEATER("THEATER", CardType.BLUE_CIVIL, 0, 2),

    //GOLD
    TAVERN("TAVERN", CardType.YELLOW_GOLD, 0, 5),
    EAST_TRADING_POST("EAST TRADING POST", CardType.YELLOW_GOLD, 0, 1),
    WEST_TRADING_POST("WEST TRADING POST", CardType.YELLOW_GOLD, 0, 1),
    MARKETPLACE("MARKETPLACE", CardType.YELLOW_GOLD, 0, 1),
    DISCARD_CARD("DISCARD", CardType.YELLOW_GOLD, 0, 1),

    //RED
    STOCKADE("STOCKADE", CardType.RED_MILITARY, 0, 2),
    BARRACKS("BARRACKS", CardType.RED_MILITARY, 0, 2),
    GUARD_TOWER("GUARD TOWER", CardType.RED_MILITARY, 0, 2),


    //GREEN
    APOTHECARY("APOTHECARY", CardType.GREEN_SCIENCE, 0, 2),
    WORKSHOP("WORKSHOP", CardType.GREEN_SCIENCE, 0, 2),
    SCRIPTORIUM("SCRIPTORIUM", CardType.GREEN_SCIENCE, 0, 2);

    public final String name;
    public final CardType cardType;
    public final int cost;
    public final int score;
    Card(String name, CardType cardType, int cost, int score) {
        this.name = name;
        this.cardType = cardType;
        this.cost = cost;
        this.score = score;
    }
}
