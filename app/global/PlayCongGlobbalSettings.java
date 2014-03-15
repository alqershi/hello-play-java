package global;

import java.util.concurrent.TimeUnit;

import actors.EventPublisher;
import actors.messages.RandomlySelectedTalkEvent;
import akka.actor.ActorRef;
import models.Proposal;
import play.Application;
import play.GlobalSettings;
import play.libs.F;
import play.libs.F.Callback;
import play.libs.F.Promise;
import play.libs.Akka;
import play.mvc.Results;
import play.mvc.SimpleResult;
import play.mvc.Http.RequestHeader;
import scala.concurrent.duration.Duration;

public class PlayCongGlobbalSettings extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		Akka.system().scheduler().schedule(
				Duration.create(1, TimeUnit.SECONDS), 	// start time after a second 
				Duration.create(10, TimeUnit.SECONDS), 	// Refresh every 10 seconds
				selectRandomTalks(), 					// Run the following method
				Akka.system().dispatcher());			// use the default AkkA dispatcher
	}
	
	private Runnable selectRandomTalks() {
		return new Runnable() {
			@Override
			public void run() {
				Promise<Proposal> proposal = Proposal.selectRandomTalk();
				proposal.onRedeem(new Callback<Proposal>() {

					@Override
					public void invoke(Proposal p) throws Throwable {
						EventPublisher.ref.tell(new RandomlySelectedTalkEvent(p), ActorRef.noSender());
					}
				});
			}
		};
	}
	
	@Override
	public Promise<SimpleResult> onHandlerNotFound(RequestHeader arg0) {
		return F.Promise.<SimpleResult>pure(Results.notFound(views.html.error.render()));
	}
	
	@Override
	public Promise<SimpleResult> onError(RequestHeader arg0, Throwable arg1) {
		return F.Promise.<SimpleResult>pure(Results.internalServerError(views.html.error.render()));
	}

}
