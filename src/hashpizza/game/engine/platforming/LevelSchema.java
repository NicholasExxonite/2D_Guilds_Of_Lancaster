package hashpizza.game.engine.platforming;

import hashpizza.game.engine.ui.DialogueEntry;
import org.jsfml.graphics.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Class for holding all of the data about a level, as parsed from the file xml
 */
public class LevelSchema {

    /**
     * Level title etc.
     */
    public LevelMetadata meta;

    /**
     * Content of each screen of the level
     */
    public LevelScreenData[] screens;

    /**
     * Dialogue to display before the level starts
     */
    public DialogueEntry[] dialogue;

    /**
     * Holds metadata about the level as displayed in the level selector
     */
    public static class LevelMetadata {

        /**
         * Title of the level to be displayed in level selector
         */
        public String title;

        /**
         * The raw file name of this level
         */
        public String filename;

        /**
         * The x position of the level in the level selector
         */
        public int mapX;

        /**
         * The y position of the level in the level selector
         */
        public int mapY;

        /**
         * The colour to use, e.g. for colouring bricks etc.
         */
        public Color foreground;
    }

    /**
     * Holds the level data as the raw string within the xml file
     */
    public static class LevelScreenData {

        /**
         * The string tile map. Each character in each string represents
         * a tile in the x direction, whilst each string itself represents a row in the y direction
         */
        public String[] tileMap;
    }

    /**
     * Loads and parses a level schema from the specified file name
     *
     * @param fileName the file name to read the file from
     * @return the parsed level schema for the file name, or null if there is a parsing error
     */
    public static LevelSchema loadFromFile(String fileName) {

        File file = new File(fileName);

        try {
            //xml parsing:
            DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = builder.newDocumentBuilder();

            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            LevelSchema schema = new LevelSchema();

            //<meta> root eleement
            Element meta = (Element) doc.getDocumentElement().getElementsByTagName("meta").item(0);

            schema.meta = new LevelMetadata();

            schema.meta.mapX = Integer.parseInt(meta.getElementsByTagName("mapX").item(0).getTextContent());
            schema.meta.mapY = Integer.parseInt(meta.getElementsByTagName("mapY").item(0).getTextContent());
            schema.meta.title = meta.getElementsByTagName("title").item(0).getTextContent();
            schema.meta.filename = schema.meta.title.toLowerCase();

            //level colour
            if (meta.getElementsByTagName("foreground").getLength() == 0) {
                schema.meta.foreground = new Color(115, 115, 115);
            } else {
                String hex = meta.getElementsByTagName("foreground").item(0).getTextContent();

                //parsing hex colour string (e.g. #ff0000 for red)
                schema.meta.foreground = new Color(Integer.parseInt(hex.substring(1, 3), 16), Integer.parseInt(hex.substring(3, 5), 16), Integer.parseInt(hex.substring(5, 7), 16));
            }

            //<screens> ...
            Element screenContainer = (Element) doc.getDocumentElement().getElementsByTagName("screens").item(0);
            //each <screen>
            NodeList screens = screenContainer.getElementsByTagName("screen");

            schema.screens = new LevelScreenData[screens.getLength()];

            //parsing each screen data
            for (int i = 0; i < screens.getLength(); i++) {
                Element screen = (Element) screens.item(i);

                Element tileMap = (Element) screen.getElementsByTagName("tileMap").item(0);
                //each <row>
                NodeList rows = tileMap.getElementsByTagName("row");

                LevelScreenData sd = new LevelScreenData();
                sd.tileMap = new String[rows.getLength()];

                //for each <row>, read its content and put it in the tile map
                for (int j = 0; j < rows.getLength(); j++) {
                    sd.tileMap[j] = rows.item(j).getTextContent();
                }

                schema.screens[i] = sd;
            }

            //<dialogue> parsing
            NodeList dialoguesContainer = doc.getDocumentElement().getElementsByTagName("dialogue");
            if (dialoguesContainer.getLength() == 0) {
                schema.dialogue = new DialogueEntry[0]; //no dialogue!
            } else {
                Element dialogueContainer = (Element) dialoguesContainer.item(0);
                //each <line>
                NodeList dialogues = dialogueContainer.getElementsByTagName("line");

                schema.dialogue = new DialogueEntry[dialogues.getLength()];

                //for each line, read its text and add to dialogue array
                for (int i = 0; i < dialogues.getLength(); i++) {
                    Element dialogue = (Element) dialogues.item(i);

                    String from = dialogue.getAttribute("from");
                    //replace all double spaces with a single space, since xml format will count all the blank space
                    //as a space
                    String text = dialogue.getTextContent().strip().replaceAll("  +", "");

                    schema.dialogue[i] = new DialogueEntry(from, text);
                }
            }

            return schema;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
