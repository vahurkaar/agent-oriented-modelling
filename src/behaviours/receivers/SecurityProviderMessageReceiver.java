package behaviours.receivers;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.Incident;

/**
 * Created by Vahur Kaar on 2.05.2015.
 */
public class SecurityProviderMessageReceiver extends TickerBehaviour {

    public SecurityProviderMessageReceiver(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {

            if (messageOntologyEquals(msg, "request-help")) {
                System.out.println(myAgent.getLocalName() + ": RECEIVED REQUEST FOR HELP FROM " + msg.getSender().getLocalName());
                System.out.println(myAgent.getLocalName() + ": ASK FOR INCIDENT DETAILS!");

                ACLMessage reply = new ACLMessage(ACLMessage.REQUEST);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                reply.setOntology("request-incident-details");
                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "send-incident-details")) {
                ContentElement ce = null;
                try {
                    ce = myAgent.getContentManager().extractContent(msg);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                Incident incident = (Incident) ce;
                System.out.printf("%s: RECEIVED INCIDENT DETAILS! (Source: %s; Details: %s; Author: %s)%n", myAgent.getLocalName(), incident.getSource(), incident.getDetails(), incident.getCreator());

                ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                reply.setContent("Help is on the way!");
                reply.setOntology("confirm-help");
                myAgent.send(reply);

                reply = new ACLMessage(ACLMessage.AGREE);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                reply.setOntology("confirm-incident-resolved");
                myAgent.send(reply);
            }

            else {
                System.out.println(myAgent.getLocalName()+": didn't understand the message!");
            }
        }
    }

    private boolean messageOntologyEquals(ACLMessage msg, String ontology) {
        return msg.getOntology() != null && msg.getOntology().contentEquals(ontology);
    }
}
