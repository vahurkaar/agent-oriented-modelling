package ontology;

import jade.content.Concept;
import jade.content.Predicate;

import java.util.Date;

/**
 * Created by Vahur Kaar on 3.05.2015.
 */
public class Incident implements Predicate {

    private String source;
    private String details;
    private String creator;
    private Date createdDate = new Date();


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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
