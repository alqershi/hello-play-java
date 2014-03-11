package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
	
	

}
