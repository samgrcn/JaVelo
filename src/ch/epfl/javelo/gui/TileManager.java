package ch.epfl.javelo.gui;

import ch.epfl.javelo.MemoryCacheHashMap;
import ch.epfl.javelo.Preconditions;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.StringJoiner;

import javafx.scene.image.Image;

/**
 * Represents an OSM tile manager.
 *
 * @author Quentin Chappuis (339517)
 */
public final class TileManager {

    private final Path path;
    private final String name;
    private final Map<TileId, Image> tiles = new MemoryCacheHashMap<>();
    private static final int POWER_TWO_BIT_SHIFT = 1;

    /**
     * @param path the path to the directory containing the disk cache
     * @param name the name of the tile server
     */
    public TileManager(Path path, String name) {
        this.path = path;
        this.name = name;
    }

    /**
     * Takes as argument the identity of a tile (of type TileId) and
     * returns its image (of type Image from the JavaFX library).
     *
     * @param tileId the tile identity
     * @return the image of the tile
     * @throws IOException signals that an I/O exception of some sort has occurred
     * while creating a folder or by transferring the flow
     */
    public Image imageForTileAt(TileId tileId) throws IOException {
        Preconditions.checkArgument(TileId.isValid(tileId.zoomAt, tileId.x, tileId.y));

        if (tiles.containsKey(tileId)) return tiles.get(tileId);

        Path imagePath = Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt))
                .resolve(String.valueOf(tileId.x)).resolve(tileId.y + ".png");

        if(tiles.get(tileId) != null) return tiles.get(tileId);
        else if (Files.exists(imagePath)) {
            try {
                InputStream stream = Files.newInputStream(imagePath);
                Image image = new Image(stream);
                tiles.put(tileId, image);
                return image;
            } catch (InvalidPathException e) {
                throw new IOException(e);
            }
        }

        Files.createDirectories(Path.of(path.toString()).resolve(String.valueOf(tileId.zoomAt))
                .resolve(String.valueOf(tileId.x)));

        try (InputStream input = tileDownloader(tileId);
             OutputStream out = new FileOutputStream(imagePath.toFile())) {
            input.transferTo(out);
        }
        Image image = new Image("file:" + imagePath);
        tiles.put(tileId, image);
        return image;
    }

    /**
     * Builds an URL for a given tile identity.
     *
     * @param tileId the tile identity
     * @return the URL
     * @throws MalformedURLException if an error occured while creating the URL
     */
    private URL URLBuilder(TileId tileId) throws MalformedURLException {
        int zoomAt = tileId.zoomAt();
        int x = tileId.x();
        int y = tileId.y();
        String url = String.format("https://%s/%s/%s/%s.png", name, zoomAt, x, y);
        return new URL(url);
    }

    /**
     * Download the tile from the server and return an input flow.
     * @param tileId the tile identity
     * @return the image input flow
     * @throws IOException signals that an I/O exception to some sort has occurred
     */
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

        /**
         * @throws IllegalArgumentException if the zoom is negative, x or y is negative or x or y is greater than
         * two power the zoom
         */
        public TileId {
            Preconditions.checkArgument(isValid(zoomAt, x, y));
        }

        /**
         * Takes as argument three attributes (zoom and X/Y index) and returns true if - and only if -
         * they constitute a valid tile identity.
         * @param zoomAt the zoom
         * @param x the x-coordinate
         * @param y the y-coordinate
         * @return true or false
         */
        public static boolean isValid(int zoomAt, int x, int y) {
            return zoomAt >= 0 && x >= 0 && x < POWER_TWO_BIT_SHIFT << zoomAt
                    && y >= 0 && y < POWER_TWO_BIT_SHIFT << zoomAt;
        }
    }
}
