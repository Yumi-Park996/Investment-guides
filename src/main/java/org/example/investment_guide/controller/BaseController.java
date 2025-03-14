package org.example.investment_guide.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    protected void view(HttpServletRequest req, HttpServletResponse resp, String viewName) throws ServletException, IOException {
        String path;
        path = "/" + viewName + ".jsp";  // 루트 경로에서 찾음
        req.getRequestDispatcher(path).forward(req, resp);
    }

}