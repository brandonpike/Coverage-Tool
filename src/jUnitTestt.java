
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.Failure;

public class ourListener extends RunListener {
    public void testStarted(Description representation) {
        System.out.println("Starting Test, " representation.getMethodName() );
    }
    public void testRunStarted(Description representation) throws Exception {
        System.out.println("Beggining, number of test cases is  " + representation.testCount() + "\n");
    }
    public void testRunFinished(Result outcome) {
        System.out.println("Finished, number of test cases is " + outcome.getRunCount());
    }
    public void testFinished(Description representation) {
        System.out.println("Done, " + representation.getMethodName() );
    }
    public void testFailure(Failure notWorking) throws Exception {
        System.out.println("Test did  not pass" + notWorking.getDescription().getMethodName());
    }