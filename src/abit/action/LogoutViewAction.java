package abit.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import java.io.IOException;
import javax.servlet.ServletException;
import java.sql.SQLException;

import java.util.Locale;

public class LogoutViewAction extends Action {

    public ActionForward perform
    (
        ActionMapping       mapping,
        ActionForm          form,
        HttpServletRequest  request,
        HttpServletResponse response
    )
    throws IOException,
           ServletException
    {
        HttpSession session = request.getSession();
        if(request.getParameter("back")!=null) return mapping.findForward("back");
        request.setAttribute("logoutViewAction", new Boolean(true));
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );
        Object userBean = session.getAttribute("user");
        if (userBean==null)
           return mapping.findForward("index");
        return mapping.findForward("success");
    }
}
