package agents;

import behaviours.receivers.PersonMessageReceiver;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import behaviours.*;
import ontology.AlarmOntology;
import ontology.Location;
import ontology.LocationOntology;


@SuppressWarnings("serial")
public class PersonAgent extends Agent {

    //Initializing FIPA-SL (Semantic Language)
    private Codec codec = new SLCodec();
    //Initializing ontology class AlarmOntology
    private Ontology alarmOntology = AlarmOntology.getInstance();
    private Ontology sendLocationOntology = LocationOntology.getSendLocationInstance();
    private Ontology safeAreaLocationOntology = LocationOntology.getSafeAreaLocationInstance();

    private Location location;

    private String action;

    protected void setup() {
        // Printout a welcome message
        System.out.println("PERSON-AGENT " + getAID().getLocalName() + " IS READY.");

        Object[] args = getArguments();

        //registering ontology and codec language (FIPA-SL)
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(alarmOntology);
        getContentManager().registerOntology(sendLocationOntology);
        getContentManager().registerOntology(safeAreaLocationOntology);

        registerServices();

        // Add alert behaviour to send an alert message to the seciruty provider agent
        if (args != null && args.length > 0){
            action = (String) args[0];
            if (args.length == 3) {
                location = new Location((String) args[1], (String) args[2]);
            }
        } else {
            action = "ok";
        }

        if (location == null) {
            location = new Location("II", "100");
        }

        if (action.equals("alarm")) {
            addBehaviour(new RaiseAlarmBehaviour(this, "Registry booth", "Intruder alarm!"));
        }

        //Add a ticker behavior to receive message from peer agents
        addBehaviour(new PersonMessageReceiver(this, 3000));
    }

    private void registerServices() {
        //Register provided services to the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("PERSON");
        sd.setName("PEOPLE SERVICE");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println("PERSON-AGENT " + getAID().getLocalName() + " HAS BEEN TERMINATED!");
    }

    public Location getLocation() {
        return location;
    }
}
