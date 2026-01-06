package co.juanxxo.junta.overlay;

public enum AnimationPosition {
    CENTER("center"),
    SCOREBOARD("scoreboard"),
    BOTTOM_CENTER("bottom_center"),
    TOP_CENTER("top_center"),
    TOP_RIGHT("top_right"),
    TOP_LEFT("top_left"),
    BOTTOM_RIGHT("bottom_right"),
    BOTTOM_LEFT("bottom_left");

    private final String name;

    AnimationPosition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AnimationPosition fromString(String input) {
        if (input == null) return CENTER;
        String s = input.trim().toLowerCase();

        // Accept both English and legacy Spanish names for compatibility
        switch (s) {
            case "center":
            case "centro":
                return CENTER;
            case "scoreboard":
                return SCOREBOARD;
            case "bottom_center":
            case "screen":
                return BOTTOM_CENTER;
            case "top_center":
            case "up_centro":
                return TOP_CENTER;
            case "top_right":
            case "up_derecha":
                return TOP_RIGHT;
            case "top_left":
            case "up_izquierda":
                return TOP_LEFT;
            case "bottom_right":
            case "down_derecha":
                return BOTTOM_RIGHT;
            case "bottom_left":
            case "down_izquierda":
                return BOTTOM_LEFT;
            default:
                return CENTER; // default
        }
    }
}
