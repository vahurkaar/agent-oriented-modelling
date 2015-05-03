package behaviours;


import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ontology.AlarmActivity;

@SuppressWarnings("serial")
public class RaiseAlarmBehaviour extends OneShotBehaviour {

    private String source;
    private String details;

    public RaiseAlarmBehaviour(Agent a, String source, String details) {
        super(a);
        this.source = source;
        this.details = details;
    }

    @Override
    public void action() {
        //Searching in Directory Facilitator (DF) for a given service from a list available physician agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SECURITY");
        sd.setName("ALARM SERVICE");
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            while (result.length == 0) {
                result = DFService.search(myAgent, template);
            }

            //Prepare message to be sent to the security assistant
            ACLMessage cfp= new ACLMessage(ACLMessage.INFORM);

            //Set the FIPA-SL content language
            cfp.setLanguage(myAgent.getContentManager().getLanguageNames()[0]);
            //Set the ontology which provides knowledge sharing
            cfp.setOntology(myAgent.getContentManager().getOntologyNames()[0]);
            cfp.addReceiver(result[0].getName());
            cfp.setReplyWith("cfp"+System.currentTimeMillis());  //Unique value

            myAgent.getContentManager().fillContent(cfp, new AlarmActivity(source, details));
            // Send the alarm notification to security assistants
            myAgent.send(cfp);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }
}