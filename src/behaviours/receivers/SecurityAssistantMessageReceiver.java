package behaviours.receivers;

import behaviours.CallSecurityProviderBehaviour;
import behaviours.NotifyPeopleBehaviour;
import behaviours.OrderToExitBehaviour;
import behaviours.RecommendSafeAreaBehaviour;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ontology.AlarmActivity;
import ontology.Incident;
import ontology.Location;

/**
 * Created by Vahur Kaar on 2.05.2015.
 */
public class SecurityAssistantMessageReceiver extends TickerBehaviour {

    private boolean alarmIsRaised;

    private Incident incident;

    public SecurityAssistantMessageReceiver(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {

            if (messageOntologyEquals(msg, "raise-alarm")) {
                if (!alarmIsRaised) {
                    alarmIsRaised = true;

                    ContentElement ce = null;
                    try {
                        ce = myAgent.getContentManager().extractContent(msg);
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();
                    } catch (OntologyException e) {
                        e.printStackTrace();
                    }

                    AlarmActivity alarmActivity = (AlarmActivity) ce;
                    incident = new Incident();
                    incident.setCreator(myAgent.getLocalName());
                    incident.setSource(alarmActivity.getSource());
                    incident.setDetails(alarmActivity.getDetails());

                    System.out.printf("%s: %s RAISED THE ALARM! (Source: %s; Details: %s)%n", myAgent.getLocalName(), msg.getSender().getLocalName(), incident.getSource(), incident.getDetails());
                    System.out.printf("%s: INSTRUCT PEOPLE TO SAFE AREAS AND CALL FOR HELP!%n", myAgent.getLocalName());

                    myAgent.addBehaviour(new CallSecurityProviderBehaviour(myAgent));
                    myAgent.addBehaviour(new OrderToExitBehaviour(myAgent));
                    myAgent.addBehaviour(new RecommendSafeAreaBehaviour(myAgent));
                } else {
                    System.out.println(myAgent.getLocalName() + ": ALARM HAS ALREADY BEEN RAISED!");
                }
            }

            else if (messageOntologyEquals(msg, "request-incident-details")) {
                System.out.println(myAgent.getLocalName() + ": " + msg.getSender().getLocalName() + " REQUESTED FOR INCIDENT DETAILS!");

                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                //Set the FIPA-SL content language
                reply.setLanguage(myAgent.getContentManager().getLanguageNames()[0]);
                //Set the ontology which provides knowledge sharing
                reply.setOntology(myAgent.getContentManager().getOntologyNames()[1]);
                try {
                    myAgent.getContentManager().fillContent(reply, incident);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "confirm-help")) {
                System.out.printf("%s: %s IS SENDING HELP! (Msg: %s)%n", myAgent.getLocalName(), msg.getSender().getLocalName(), msg.getContent());
            }

            else if (messageOntologyEquals(msg, "confirm-incident-resolved")) {
                System.out.println(myAgent.getLocalName() + ": " + msg.getSender().getLocalName() + " HAS RESOLVED THE INCIDENT!");

                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(new AID(myAgent.getName(), true));
                reply.setOntology("suppress-alarm");
                myAgent.send(reply);
            }

            else if (messageOntologyEquals(msg, "suppress-alarm")) {
                if (alarmIsRaised) {
                    System.out.println(myAgent.getLocalName() + ": LOWERING THE ALARM!");
                    alarmIsRaised = false;
                    myAgent.addBehaviour(new NotifyPeopleBehaviour(myAgent));
                } else {
                    System.out.println(myAgent.getLocalName() + ": ALARM HAS NOT BEEN RAISED!");
                }
            }

            else if (messageOntologyEquals(msg, "send-location")) {
                System.out.println(myAgent.getLocalName() + ": RECOMMEND SAFE AREA LOCATION");

                ContentElement ce = null;
                try {
                    ce = myAgent.getContentManager().extractContent(msg);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }

                Location location = (Location) ce;
                Location safeArea;
                if (location.getLevel().equals("II") && location.getRoom().equals("100")) {
                    safeArea = new Location("III", "Attic");
                } else {
                    safeArea = new Location("0", "Basement");
                }

                ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
                reply.addReceiver(new AID(msg.getSender().getName(), true));
                //Set the FIPA-SL content language
                reply.setLanguage(myAgent.getContentManager().getLanguageNames()[0]);
                reply.setOntology("send-safe-area-recommendation");

                if (alarmIsRaised) {
                    try {
                        myAgent.getContentManager().fillContent(reply, safeArea);
                    } catch (Codec.CodecException e) {
                        e.printStackTrace();
                    } catch (OntologyException e) {
                        e.printStackTrace();
                    }

                    myAgent.send(reply);
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
