package abit.servlet;

import java.util.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import abit.Constants;
import abit.bean.*;

public final class UserSessions extends GenericServlet  {

    private        ArrayList    sessions  = null;
    private        int          debug     = 0;
    public  static UserSessions manager   = null;

// Функция генерации отчета
    public void doLog(int level, String msg) {
        if (level<=debug)        
            log(msg);
    }

// Функция разрушения сессии
    public void destroy() {
        doLog(1,"Finalizing.");
        getServletContext().removeAttribute(Constants.SESSIONS);
        manager = null;
    }

// Функция удаления сеансов
    public void removeSession(HttpSession session) {
        synchronized(sessions) {        
            int index = sessions.indexOf(session);
            if (index!=-1)
                sessions.remove(index);
        }
    }

// Функция обработки исключений сервлета
    public void init() throws ServletException {
        // Process our servlet initialization parameters
        String value;
        value = getServletConfig().getInitParameter("debug");
        try {
            debug = Integer.parseInt(value);
        } catch (Throwable t) {
            debug = 0;
        }
        doLog(1,"Initializing.");
        sessions = new ArrayList();
        getServletContext().setAttribute(Constants.SESSIONS, this);
        manager = this;
    }

// Сервисная функция
    public void service(ServletRequest request, ServletResponse response)
    throws IOException, ServletException
    {
        doLog(1,"checking");
        long current = System.currentTimeMillis();
        long ms;
        long maxPeriod = Constants.SESSION_TIME;
        synchronized(sessions) {        
            Object array[] = sessions.toArray();
            HttpSession session;
            UserBean user;
            int i;
            for (i=0; i<array.length; i++) {
                session = (HttpSession)array[i];
                user = null;
                try {
                    user = (UserBean)session.getAttribute("user");
                } catch (Exception e) {
                    ;
                }
                if (user==null)
                    sessions.remove(i);
                else {
                    ms = session.getLastAccessedTime();
                    if ( current-ms > maxPeriod ) {
                        session.removeAttribute("user");
                        sessions.remove(i);
                    }
                }
            }
        }
    }

// Функция отладки
    public int getDebug() {
        return debug;
    }

// Функция добавления новых сеансов
    public void addSession(HttpSession session) {
        synchronized(sessions) {        
            if (sessions.indexOf(session)==-1)
                sessions.add(session);
        }
    }

// Параметр i: 0 - все, 1 - всех, 2 - в определенной группе
    public ArrayList getUsers(boolean all) {
        ArrayList list = new ArrayList();
        synchronized(sessions) {        
            Object array[] = sessions.toArray();
            HttpSession session;
            UserBean user;
            for (int i=0; i<array.length; i++) {
                session = (HttpSession)array[i];
                user = null;
                try {
                    user = (UserBean)session.getAttribute("user");
                    user.setUip(user.getUip());
                } catch (Exception e) {
                    ;
                }
                if (user==null)
                    sessions.remove(i);
                else {
                    if (!all && ((user.getGroup()).getTypeId() == 2) || all) list.add(user);
                }
            }
        }
        return list;
    }
}
