package ontology;

import jade.content.Concept;
import jade.content.Predicate;

/**
 * Created by Vahur Kaar on 3.05.2015.
 */
public class AlarmActivity implements Predicate {

    private String source;

    private String details;


    public AlarmActivity() {}

    public AlarmActivity(String source, String details) {
        this.source = source;
        this.details = details;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
