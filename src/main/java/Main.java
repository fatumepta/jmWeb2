import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.ApiServlet;
import servlet.LoginServlet;
import servlet.RegistrationServlet;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ApiServlet()), "/api/*");
        context.addServlet(new ServletHolder(new RegistrationServlet()), "/register");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
