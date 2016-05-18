package abit.servlet;

import javax.servlet.*;
import javax.servlet.http.*;

public class shtml2jsp extends HttpServlet {

    void forwarding(HttpServletRequest request, HttpServletResponse response) {
        String src = request.getServletPath();
        int    dot = src.lastIndexOf('.');
        src = src.substring(0,dot+1) + "jsp";
        try {
            getServletConfig().
                getServletContext().
                    getRequestDispatcher(src).
                        forward(request, response);
        }
        catch (Exception e){
            ;//nothing
        }
    }

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        forwarding(request,response);
    }
    public void doPost (HttpServletRequest request, HttpServletResponse response) {
        forwarding(request,response);
    }
}