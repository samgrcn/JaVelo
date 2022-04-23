package ch.epfl.javelo.gui;

import ch.epfl.javelo.Preconditions;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.image.Image;

/**
 * Represents an OSM tile manager.
 *
 * @author Quentin Chappuis (339517)
 */
public final class TileManager {

    private final Path path;
    private final String name;
    private final Map<TileId, Image> tiles = new LinkedHashMap(100, 0.75F, true);

    /**
     * @param path the path to the directory containing the disk cache
     * @param name the name of the tile server
     */
    public TileManager(Path path, String name) {
        this.path = path;
        this.name = name;
    }

    /**
     * Takes as argument the identity of a tile (of type TileId) and returns its image (of type Image from the JavaFX library).
     *
     * @param tileId the tile identity
     * @return the image of the tile
     * @throws IOException ignals that an I/O exception of some sort has occurred
     * while creating a folder or by transferring the flow
     */
    public Image imageForTileAt(TileId tileId) throws IOException {
        Preconditions.checkArgument(TileId.isValid(tileId.zoomAt, tileId.x, tileId.y));
        if (tiles.containsKey(tileId)) return tiles.get(tileId);
        Path imagePath = Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt))
                .resolve(String.valueOf(tileId.x)).resolve(tileId.y + ".png");
        if (Files.exists(imagePath)) {
            Image image = new Image("file:" + imagePath);
            tiles.put(tileId, image);
            return image;
        }

        if (Files.notExists(Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt)))) {
            Files.createDirectories(Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt)));
        }
        if (Files.notExists(Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt)).resolve(String.valueOf(tileId.x)))) {
            Files.createDirectories(Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt))
                    .resolve(String.valueOf(tileId.x)));
        }

        OutputStream out = new FileOutputStream(imagePath.toFile());
        try (InputStream input = tileDownloader(tileId)) {
            input.transferTo(out);
            Image image = new Image(input);
            tiles.put(tileId, image);
            return image;
        }
    }

    private URL URLBuilder(TileId tileId) throws MalformedURLException {
        int zoomAt = tileId.zoomAt;
        int x = tileId.x;
        int y = tileId.y;
        return new URL("https://" + name + "/" + zoomAt + "/" + x + "/" + y + ".png");
    }

    private InputStream tileDownloader(TileId tileId) throws IOException {
        URL u = URLBuilder(tileId);
        URLConnection c = u.openConnection();
        c.setRequestProperty("User-Agent", "JaVelo");
        return c.getInputStream();
    }

    /**
     * Represents the identity of an OSM tile.
     *
     * @author Quentin Chappuis (339517)
     */
    public record TileId(int zoomAt, int x, int y) {

        public TileId {
            Preconditions.checkArgument(isValid(zoomAt, x, y));
        }

        public static boolean isValid(int zoomAt, int x, int y) {
            return zoomAt >= 0 && x >= 0 && x < 1 << zoomAt
                    && y >= 0 && y < 1 << zoomAt;
        }
    }
}
