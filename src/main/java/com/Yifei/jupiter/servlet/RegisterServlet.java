package com.Yifei.jupiter.servlet;

import com.Yifei.jupiter.db.MySQLConnection;
import com.Yifei.jupiter.db.MySQLException;
import com.Yifei.jupiter.entity.LoginRequestBody;
import com.Yifei.jupiter.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read user data from the request body
        User user = ServletUtil.readRequestBody(User.class, request);
        if (user == null) {
            System.err.println("User Information Incorrect");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isUserAdded = false;
        try (MySQLConnection conn = new MySQLConnection()) {
            user.setPassword(ServletUtil.encryptPassword(
                    user.getUserId(), user.getPassword())
            );

            isUserAdded = conn.addUser(user);
        } catch (MySQLException e) {
            throw new ServletException(e);
        }
        if (!isUserAdded) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}
