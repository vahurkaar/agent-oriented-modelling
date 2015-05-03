package agents;

import behaviours.receivers.SecurityAssistantMessageReceiver;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ontology.AlarmOntology;
import ontology.IncidentOntology;
import ontology.LocationOntology;

/**
 * Created by Vahur Kaar on 2.05.2015.
 */
public class SecurityAssistantAgent extends Agent {

    //Initializing FIPA-SL (Semantic Language)
    private Codec codec = new SLCodec();
    //Initializing ontology classes
    private Ontology alarmOntology = AlarmOntology.getInstance();
    private Ontology incidentOntology = IncidentOntology.getInstance();
    private Ontology sendLocationOntology = LocationOntology.getSendLocationInstance();
    private Ontology safeAreaLocationOntology = LocationOntology.getSafeAreaLocationInstance();

    @Override
    protected void setup() {
        // Printout a welcome message
        System.out.println("SECURITY-ASSISTANT-AGENT " + getAID().getLocalName() + " IS READY.");

        Object[] args = getArguments();

        //registering ontology and codec language (FIPA-SL)
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(alarmOntology);
        getContentManager().registerOntology(incidentOntology);
        getContentManager().registerOntology(sendLocationOntology);
        getContentManager().registerOntology(safeAreaLocationOntology);
        registerServices();

        addBehaviour(new SecurityAssistantMessageReceiver(this, 500));
    }

    private void registerServices() {
        //Register provided services to the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("SECURITY");
        sd.setName("ALARM SERVICE");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println("SECURITY-ASSISTANT-AGENT " + getAID().getLocalName() + " HAS BEEN TERMINATED!");
    }
}
