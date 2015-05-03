package ontology;

import jade.content.onto.*;
import jade.content.schema.*;

@SuppressWarnings("serial")
public class AlarmOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "raise-alarm";


    public static final String ALARM_ACTIVITY = "alarm-activity";
    public static final String ALARM_SOURCE = "source";
    public static final String ALARM_DETAILS = "details";

    private static Ontology theInstance = new AlarmOntology();

    public static Ontology getInstance(){
        return theInstance;
    }

    private AlarmOntology(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {

            add(new PredicateSchema(ALARM_ACTIVITY), AlarmActivity.class);

//            // Structure of the schema for the Test concept
//            ConceptSchema cs = (ConceptSchema) getSchema(TEST);
//            cs.add(NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
//            cs.add(VALUE, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
//            cs.add(UNITS, (PrimitiveSchema) getSchema(BasicOntology.STRING));



            // Structure of the schema for the Share action agent
            PredicateSchema as = (PredicateSchema) getSchema(ALARM_ACTIVITY);
            as.add(ALARM_SOURCE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            as.add(ALARM_DETAILS, (PrimitiveSchema) getSchema(BasicOntology.STRING));

        }
        catch (OntologyException oe){
            oe.printStackTrace();
        }

    }

}