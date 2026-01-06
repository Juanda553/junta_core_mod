package co.juanxxo.junta.overlay;

public enum AnimationSize {
    SUPER_SMALL("super_small", 512, 120),
    SMALL("small", 512, 200),
    NORMAL("normal", 512, 300),
    ORIGINAL("original", 512, 512);

    private final String name;
    private final int textureSize;  // Tamaño de la textura (siempre 512x512)
    private final int renderSize;   // Tamaño de renderizado en pantalla

    AnimationSize(String name, int textureSize, int renderSize) {
        this.name = name;
        this.textureSize = textureSize;
        this.renderSize = renderSize;
    }

    public String getName() {
        return name;
    }

    public int getTextureSize() {
        return textureSize;
    }

    public int getRenderSize() {
        return renderSize;
    }

    public static AnimationSize fromString(String name) {
        for (AnimationSize size : values()) {
            if (size.name.equalsIgnoreCase(name)) {
                return size;
            }
        }
        return ORIGINAL; // Default
    }
}
