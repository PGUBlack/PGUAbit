package abit.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import abit.servlet.UserSessions;
import java.io.IOException;
import javax.servlet.ServletException;

public class LogoutAction extends Action {

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
        if(request.getParameter("back")!=null) return mapping.findForward("back");
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.removeAttribute("kVuza");
        UserSessions.manager.removeSession(session);
        return (mapping.findForward("success"));
    }

}
