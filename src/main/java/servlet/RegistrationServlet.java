package servlet;

import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class RegistrationServlet extends HttpServlet {

    UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registerPage.html", null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = new User(email, password);

        boolean validated = userService.addUser(user);
        if (validated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.printf("Added new user {%d : [%s , %s] }", user.getId(), user.getEmail(), user.getPassword());


            // console info
            System.out.println("\nUsers in DB:\n");
            userService.getAllUsers()
                    .forEach(usr -> System.out.printf(
                            "User ID: %d\nUser Name: %s \nUser password: %s\n\n",
                            usr.getId(), usr.getEmail(), usr.getPassword()));
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println("<h2> User with e-amil " + email + " already exists! ID: ");
            out.println(userService.getAllUsers().stream()
                    .filter(usr -> usr.getEmail().equals(email))
                    .findFirst().get().getId() + "</h2>");
        }
    }
}
