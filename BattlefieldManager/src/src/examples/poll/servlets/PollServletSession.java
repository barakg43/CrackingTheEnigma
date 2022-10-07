package src.examples.poll.servlets;

import examples.poll.PollAction;
import examples.poll.PollLogic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "PollServletSession", urlPatterns = {"/poll-session"})
public class PollServletSession extends HttpServlet {

    private static final String CHOICE_PARAMETER_NAME = "choice";
    private PollLogic poll;

    @Override
    public void init() throws ServletException {
        super.init();
        poll = new PollLogic(new String[]{"John", "George", "Paul", "Ringo"});
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        String parameterChoice = request.getParameter(CHOICE_PARAMETER_NAME);
        String previousSavedChoice = getSavedChoice(request);

        try (PrintWriter out = response.getWriter()) {

            PollAction action = PollAction.calculateAction(parameterChoice, previousSavedChoice);
            switch (action) {
                case SHOW_POLL:
                    out.println("I see that you DID not vote yet. Cast your vote:");
                    poll.getPollData().keySet().forEach(out::println);
                    break;
                case CAST_VOTE:
                    saveChoice(request, parameterChoice);
                    writeUserChoice(out, parameterChoice);
                    addChoiceToPoll(parameterChoice);
                    writePollResults(out);
                    break;
                case SHOW_RESULTS:
                    writeUserChoice(out, previousSavedChoice);
                    writePollResults(out);
                    break;
                case ERROR:
                    writeErrorMessage(out, previousSavedChoice);
                    writePollResults(out);
                    break;
            }
        }
    }

    // use session:
    private String getSavedChoice(HttpServletRequest request) {

        // means don't create the session if not existed. bring back whatever you have or null...
        HttpSession session = request.getSession(false);
        // if the session exists - then fetch the attribute out of it...
        String savedChoice = session == null ? null : (String)session.getAttribute(CHOICE_PARAMETER_NAME);

        return savedChoice;
    }

    private void saveChoice(HttpServletRequest request, String choice) {

        // use session:
        // if the session does not exist, create it, and set an attribute on it
        request.getSession(true).setAttribute(CHOICE_PARAMETER_NAME, choice);
    }


    private void writePollResults(PrintWriter out) {
        out.println("Poll Results:");
        for (Map.Entry<String, Integer> choiceEntry : poll.getPollData().entrySet()) {
            out.println(choiceEntry.getKey() + " --> " + formatVotes(choiceEntry.getValue()));
        }
    }

    private String formatVotes(int votesCount) {
        return votesCount == 0 ? "no votes" : votesCount == 1 ? "1 vote" : votesCount + " votes";
    }

    private void writeUserChoice(PrintWriter out, String choice) {
        out.println("Your choice was " + choice);
    }

    private void writeErrorMessage(PrintWriter out, String choice) {
        out.println("You cannot fill the poll more than 1 time!");
        out.println("Your choice was " + choice);
    }

    private void addChoiceToPoll(String choice) {
        poll.addChoice(choice);
    }
}
