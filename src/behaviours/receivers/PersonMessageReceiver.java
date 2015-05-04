package behaviours.receivers;

import agents.PersonAgent;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.Location;

/**
 * Created by Vahur Kaar on 2.05.2015.
 */
public class PersonMessageReceiver extends TickerBehaviour {

    public PersonMessageReceiver(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {

            if (messageOntologyEquals(msg, "order-to-exit")) {
                System.out.println(myAgent.getLocalName() + ": " + msg.getSender().getLocalName() + " HAS ORDERED TO EVACUATE!");
            }

            else if (messageOntologyEquals(msg, "request-location")) {
                System.out.println(myAgent.getLocalName() + ": SENDING LOCATION INFO");

                ACLMessage reply = new ACLMessage(ACLMessage.REQUEST);
                //Set the FIPA-SL content language
                reply.setLanguage(myAgent.getContentManager().getLanguageNames()[0]);
                //Set the ontology which provides knowledge sharing
                reply.setOntology(myAgent.getContentManager().getOntologyNames()[0]);
                reply.addReceiver(new AID(msg.getSender().getName(), true));

                try {
                    if (myAgent instanceof PersonAgent) {
                        PersonAgent pa = (PersonAgent) myAgent;
                        myAgent.getContentManager().fillContent(reply, pa.getLocation());
                    }
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "send-safe-area-recommendation")) {
                ContentElement ce = null;
                try {
                    ce = myAgent.getContentManager().extractContent(msg);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                Location location = (Location) ce;

                System.out.println(myAgent.getLocalName() + ": RECOMMENDED SAFE AREA - " + location.getLevel() + " - " + location.getRoom());

//                ACLMessage reply = new ACLMessage(ACLMessage.REQUEST);
//                reply.addReceiver(new AID(msg.getSender().getName(), true));
//                reply.setOntology("");
//                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "notify-incident-solved")) {
                System.out.println(myAgent.getLocalName() + ": RECEIVED NOTIFICATION OF SURPRESSED ALARM");
            }

            else if (messageOntologyEquals(msg, "visitor-registered")) {
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    System.out.println(myAgent.getLocalName() + ": VISITOR WAS SUCCESSFULLY REGISTERED!");
                } else {
                    System.out.println(myAgent.getLocalName() + ": VISITOR WAS NOT SUCCESSFULLY REGISTERED!");
                }
            }

            else if (messageOntologyEquals(msg, "visitor-exit-registered")) {
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    System.out.println(myAgent.getLocalName() + ": VISITOR EXIT WAS SUCCESSFULLY REGISTERED!");
                } else {
                    System.out.println(myAgent.getLocalName() + ": VISITOR EXIT WAS NOT SUCCESSFULLY REGISTERED!");
                }
            }

            else {
                System.out.println(msg.getOntology());
                System.out.println(myAgent.getLocalName()+": didn't understand the message!");
            }
        }
    }

    private boolean messageOntologyEquals(ACLMessage msg, String ontology) {
        return msg.getOntology() != null && msg.getOntology().contentEquals(ontology);
    }
}
