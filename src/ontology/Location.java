package ontology;

import jade.content.Predicate;

import java.util.Date;

/**
 * Created by Vahur Kaar on 3.05.2015.
 */
public class Location implements Predicate {

    private String level;
    private String room;

    public Location() {}

    public Location(String level, String room) {
        this.level = level;
        this.room = room;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
