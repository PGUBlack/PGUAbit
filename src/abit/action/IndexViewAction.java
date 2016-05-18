package abit.action;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import java.sql.SQLException;


import java.util.Locale;
import java.util.ArrayList;

import abit.bean.*;
import abit.Constants;
import abit.util.*;

public class IndexViewAction extends Action {

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
        HttpSession             session         = request.getSession();
        UserBean                user            = (UserBean)session.getAttribute("user");
        Integer                 countOfThemes   = new Integer(0);
        Integer                 countOfDone     = new Integer(0);
        Object                  currentTheme    = null;
        Locale                  locale          = new Locale("ru","RU");
        ArrayList               uGroups         = new ArrayList();

//Установка аттрибутов
        request.setAttribute("indexViewAction", new Boolean(true));
        session.setAttribute( Action.LOCALE_KEY, locale );
        String indexName = "pubIndex";

        if(user!=null) {
          GroupBean userGroup = (GroupBean)user.getGroup();
          request.setAttribute("userGroup",userGroup);

            int groupType = user.getGroup().getTypeId();
            switch (groupType) {
              case 0:
              //****************************** заходит администратор **************
                 indexName = "adminIndex";
                 break;
              //****************************** заходит оператор *******************
              case 1:
                  indexName = "opIndex";
                  break;
              //****************************** заходит наблюдатель *******************
              case 2:
                  indexName = "drIndex";
                  break;
              //****************************** заходит оператор ввода *******************
              case 3:
                  indexName = "inIndex";
                  break;
            }
        }

        request.setAttribute("index", indexName);
        return mapping.findForward("success");
   }
}