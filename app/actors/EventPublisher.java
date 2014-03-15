package actors;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import actors.messages.CloseConnectionEvent;
import actors.messages.NewConnectionEvent;
import actors.messages.UserEvent;
import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.Logger;
import play.libs.Akka;
import play.mvc.WebSocket.Out;

public class EventPublisher extends UntypedActor {
	
	public static ActorRef ref = Akka.system().actorOf(Props.create(EventPublisher.class));
	
	private Map<String, Out<JsonNode>> connections = new HashMap<String, Out<JsonNode>>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO add event handling object
		if(message instanceof NewConnectionEvent) {
			final NewConnectionEvent nce = (NewConnectionEvent) message;
			connections.put(nce.uuid(), nce.out());
			Logger.info("New Conncetion connected {}", nce.uuid());
		} else if (message instanceof CloseConnectionEvent) {
			final CloseConnectionEvent cce = (CloseConnectionEvent) message;
			final String uuid = cce.uuid();
			Logger.info("Browser {} is dissconnected", uuid);
		} else if (message instanceof UserEvent) { 
			brodcastEvent((UserEvent) message);
		} else {
			unhandled(message);
		}
		
		
	}

	private void brodcastEvent(UserEvent message) {
		for(Out<JsonNode> out: connections.values()) {
			out.write(message.json());
		}
		
	}

}
