package controllers;



import models.Proposal;
//Handle Forms
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
    
	static final Form<Proposal> proposalForm = Form.form(Proposal.class);
	
    public static Result index() {
        return ok(views.html.index.render("Hello Play Framework"));
    }
    
    public static Result newProposal() {    	
    	return ok(views.html.newProposal.render(proposalForm));
    }
    
    public static Result submitProposal() {
    	Form<Proposal> submittedForm = proposalForm.bindFromRequest();
    	if(submittedForm.hasErrors()) {
    		return ok(views.html.newProposal.render(submittedForm));
    	} else {
    		Proposal proposal = submittedForm.get();
    		proposal.save();
    		flash("message", "Thank you for submitting the proposal");
    		return redirect(routes.Application.index());
    		
    	}
    }
    
}
