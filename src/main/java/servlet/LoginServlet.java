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

public class LoginServlet extends HttpServlet {

    UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = new User(email, password);

        boolean validated = userService.authUser(user);
        if (validated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            out.printf("User logged in!");

            // console info
            System.out.println("\nLogged in users:\n");
            userService.getAllAuth()
                    .forEach(usr -> System.out.printf(
                            "User ID: %d\nUser Name: %s \nUser password: %s\n\n",
                            usr.getId(), usr.getEmail(), usr.getPassword()));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<h2> The user with this combination [login: password] does not exist! </h2>");
        }
    }
}
