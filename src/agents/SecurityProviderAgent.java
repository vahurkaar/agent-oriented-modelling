package agents;

import behaviours.receivers.SecurityProviderMessageReceiver;
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

/**
 * Created by Vahur Kaar on 2.05.2015.
 */
public class SecurityProviderAgent extends Agent {

    //Initializing FIPA-SL (Semantic Language)
    private Codec codec = new SLCodec();
    //Initializing ontology classes
    private Ontology incidentOntology = IncidentOntology.getInstance();

    @Override
    protected void setup() {
        // Printout a welcome message
        System.out.println("SECURITY-PROVIDER-AGENT " + getAID().getLocalName() + " IS READY.");

        Object[] args = getArguments();

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(incidentOntology);

        //Register provided service to the DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("SECURITY PROVIDER");
        sd.setName("HELPER SERVICE");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new SecurityProviderMessageReceiver(this, 500));
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println("SECURITY-PROVIDER-AGENT " + getAID().getLocalName() + " HAS BEEN TERMINATED!");
    }
}
