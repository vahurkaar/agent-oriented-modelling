package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

@SuppressWarnings("serial")
public class IncidentOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "send-incident-details";


    public static final String INCIDENT = "incident";
    public static final String INCIDENT_SOURCE = "source";
    public static final String INCIDENT_DETAILS = "details";
    public static final String INCIDENT_DATE = "createdDate";
    public static final String INCIDENT_CREATOR = "creator";

    private static Ontology theInstance = new IncidentOntology();

    public static Ontology getInstance(){
        return theInstance;
    }

    private IncidentOntology(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {

            add(new PredicateSchema(INCIDENT), Incident.class);

            // Structure of the schema for the Share action agent
            PredicateSchema as = (PredicateSchema) getSchema(INCIDENT);
            as.add(INCIDENT_SOURCE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            as.add(INCIDENT_DETAILS, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            as.add(INCIDENT_CREATOR, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            as.add(INCIDENT_DATE, (PrimitiveSchema) getSchema(BasicOntology.DATE));

        }
        catch (OntologyException oe){
            oe.printStackTrace();
        }

    }

}