package com.Yifei.jupiter.servlet;

import com.Yifei.jupiter.db.MySQLConnection;
import com.Yifei.jupiter.db.MySQLException;
import com.Yifei.jupiter.entity.LoginRequestBody;
import com.Yifei.jupiter.entity.LoginResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read user data from the request body
        LoginRequestBody body = ServletUtil.readRequestBody(LoginRequestBody.class, request);
        if (body == null) {
            System.err.println("User Information Incorrect");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username = "";
        try (MySQLConnection conn = new MySQLConnection()) {
            String userId = body.getUserId();
            String password = ServletUtil.encryptPassword(
                    userId, body.getPassword()
            );
            username = conn.verifyLogin(userId, password);
        } catch (MySQLException e) {
            throw new ServletException(e);
        }

        // Create a new session for the user if user ID and password are correct, otherwise return Unauthorized error.
        if (!username.isEmpty()) {
            // Create a new session, put user ID as an attribute into the session object, and set the expiration time to 600 seconds.
            HttpSession session = request.getSession();
            session.setAttribute("user_id", body.getUserId());
            session.setMaxInactiveInterval(600);

            LoginResponseBody loginResponseBody = new LoginResponseBody(body.getUserId(), username);
            ServletUtil.writeData(response, loginResponseBody);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
