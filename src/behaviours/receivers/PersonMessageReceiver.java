package behaviours.receivers;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

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
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                reply.setOntology("send-location");
                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "send-safe-area-recommendation")) {
                System.out.println(myAgent.getLocalName() + ": RECOMMENDED SAFE AREA - " + "");

                ACLMessage reply = new ACLMessage(ACLMessage.REQUEST);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                reply.setOntology("");
                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "notify-incident-solved")) {
                System.out.println(myAgent.getLocalName() + ": RECEIVED NOTIFICATION OF SURPRESSED ALARM");
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
