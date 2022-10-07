package src.examples.poll;

public enum PollAction {

    SHOW_POLL, CAST_VOTE, SHOW_RESULTS, ERROR;

    public static PollAction calculateAction(String parameterChoice, String previouseSavedChoice) {
        if (parameterChoice == null && previouseSavedChoice == null) {
		//no user choice and no cookie choice
            //this means the user is accessing the servlet for the first time
            //display the poll input
            return SHOW_POLL;
        } else if (parameterChoice != null && previouseSavedChoice == null) {
		//user choice and no cookie choice
            //this means the user has selected an option and submitted the form for the first time
            //add the users choice to the poll, save it as a cookie and display the poll results
            return CAST_VOTE;
        } else if (parameterChoice == null && previouseSavedChoice != null) {
		//no user choice and cookie choice
            //this means the user accessed the servlet after he submitted a choice
            //display the user previous choice and display the poll results
            return SHOW_RESULTS;
        } else if (parameterChoice != null && previouseSavedChoice != null) {
		//user choice and cookie choice - this is not a valid state
            //this means the user accessed the servlet after he submitted a choice
            //and tried to answer the poll more than once
            //display an error message and display the poll results
            return ERROR;
        }
        return null;
    }
}
