package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

@SuppressWarnings("serial")
public class LocationOntology extends Ontology {

    public static final String LOCATION = "location";
    public static final String LOCATION_LEVEL = "level";
    public static final String LOCATION_ROOM = "room";

    private static Ontology sendLocationInstance = new LocationOntology("send-location");
    private static Ontology safeAreaLocationInstance = new LocationOntology("send-safe-area-recommendation");

    public static Ontology getSendLocationInstance() {
        return sendLocationInstance;
    }

    public static Ontology getSafeAreaLocationInstance() {
        return safeAreaLocationInstance;
    }

    private LocationOntology(String name){
        super(name, BasicOntology.getInstance());

        try {

            add(new PredicateSchema(LOCATION), Location.class);

            // Structure of the schema for the Share action agent
            PredicateSchema as = (PredicateSchema) getSchema(LOCATION);
            as.add(LOCATION_LEVEL, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            as.add(LOCATION_ROOM, (PrimitiveSchema) getSchema(BasicOntology.STRING));

        }
        catch (OntologyException oe){
            oe.printStackTrace();
        }

    }

}