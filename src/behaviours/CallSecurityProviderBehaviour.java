package behaviours;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CallSecurityProviderBehaviour extends OneShotBehaviour {

    private AID [] securityProviders;

    public CallSecurityProviderBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        //Searching in Directory Facilitator (DF) for a given service from a list available physician agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SECURITY PROVIDER");
        sd.setName("HELPER SERVICE");
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            securityProviders = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                securityProviders[i] = result[i].getName();
            }

            //Prepare message to be sent to the list of available physicians
            ACLMessage cfp= new ACLMessage(ACLMessage.REQUEST);
            for (int i = 0; i < securityProviders.length; i++) {
                cfp.addReceiver(securityProviders[i]);
            }

            cfp.setOntology("request-help");
            cfp.setReplyWith("cfp"+System.currentTimeMillis());  //Unique value

            // Send the alarm notification to security assistants
            myAgent.send(cfp);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}