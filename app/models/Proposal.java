package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import play.Logger;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Akka;
import play.libs.F.Function0;
import play.libs.F.Function;
import play.libs.F.Promise;
import scala.concurrent.ExecutionContext;


@Entity
public class Proposal extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5830171054131211842L;

	@Id
	public Long Id;
	
	@Required
	public String title;
	
	@Required
	@MinLength(value = 10)
	@MaxLength(value = 1000)
	@Column(length = 1000)
	public String proposal;
	
	@Required
	public SessionType type = SessionType.OneHourTalk;
	
	@Required
	public Boolean isAproved = false;
	
	public String keywords;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	public Speaker speaker;

	private static Finder<Long, Proposal> find = new Finder<Long, Proposal>(Long.class, Proposal.class);
	
	private static ExecutionContext db_ctx = Akka.system().dispatchers().lookup("akka.db-dispatcher");
	
	public static Promise<Proposal> findKeynote() {
		
		return Promise.promise(new Function0<Proposal>() {
			@Override
			public Proposal apply() {
				return find.where().eq("type", SessionType.Keynote).findUnique();
			}
		}, db_ctx).recover(new Function<Throwable, Proposal>(){

			@Override
			public Proposal apply(Throwable t) throws Throwable {
				Logger.error("Failed to get keynote from the DB", t.getMessage());
				Proposal coming_soon = new Proposal();
				coming_soon.title = "COMING SOON";
				coming_soon.proposal = "";
				coming_soon.speaker = new Speaker();
				return coming_soon;
			}
			
		});
		 
	}

	public Promise<Void> asyncSave() {
		return Promise.promise(new Function0<Void>(){
			@Override
			public Void apply() throws Throwable {
				save();
				return null;
			}
		}, db_ctx);
		
	}

	public static Promise<Proposal> selectRandomTalk() {
		return Promise.promise(new Function0<Proposal>() {
			@Override
			public Proposal apply() {
				Long randomID = (long) (1 + Math.random() * (2 - 1));
				return Proposal.find.byId(randomID);
			}
		}, db_ctx).recover(new Function<Throwable, Proposal>(){

			@Override
			public Proposal apply(Throwable t) throws Throwable {
				Logger.error("Failed to get keynote from the DB", t.getMessage());
				Proposal coming_soon = new Proposal();
				coming_soon.title = "COMING SOON";
				coming_soon.proposal = "";
				coming_soon.speaker = new Speaker();
				return coming_soon;
			}
			
		});
	}
	
	

}
