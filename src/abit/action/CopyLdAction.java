package abit.action;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Locale;
import java.util.ArrayList;
import org.apache.struts.action.*;
import javax.naming.*;
import javax.sql.*;
import abit.bean.*;
import abit.Constants;
import abit.util.*;
import abit.sql.*; 

public class CopyLdAction extends Action {

    public ActionForward perform ( ActionMapping        mapping,
                                   ActionForm           actionForm,
                                   HttpServletRequest   request,
                                   HttpServletResponse  response )

    throws IOException, ServletException
    {   
        HttpSession        session       = request.getSession();
        Connection         conn          = null;
        PreparedStatement  stmt          = null;
        ResultSet          rs            = null;
        PreparedStatement  stmt2         = null;
        ResultSet          rs2           = null;
        ActionErrors       errors        = new ActionErrors();
        ActionError        msg           = null;
        CopyLdForm         form          = (CopyLdForm) actionForm;
        AbiturientBean     abit_Gr       = form.getBean(request, errors);
        boolean            copyld_f      = false;
        boolean            error         = false;
        ActionForward      f             = null;
        int                kGr           = 1;
        boolean            zaoch         = false;
        ArrayList          messages_good = new ArrayList();
        ArrayList          messages_bad  = new ArrayList();
        UserBean           user          = (UserBean)session.getAttribute("user");

        if (user==null || user.getGroup()==null || user.getGroup().getTypeId()!=1) {
            msg = new ActionError( "logon.must" );
            errors.add( "logon.login", msg );
        }

        if ( errors.empty() ) {

        request.setAttribute( "copyldAction", new Boolean(true) );
        Locale locale = new Locale("ru","RU");
        session.setAttribute( Action.LOCALE_KEY, locale );

        try {

/**********************************************************************/
/*********  ��������� ���������� � �� � ������� ����������  ***********/

          UserConn us = new UserConn(request, mapping);
          conn = us.getConn(user.getSid());
          request.setAttribute( "copyldForm", form );

/*****************  ������� � ���������� ��������   *******************/
          if(us.quit("exit")) return mapping.findForward("back");
/**********************************************************************/

            if ( form.getAction() == null ) {
             
                 form.setAction(us.getClientIntName("new","init"));
                 session.removeAttribute("kFak");

            } else if(form.getAction().equals("doit")) {

// ������ ������ ������ ������� ������ ��� � �������: ������-�����

            int cur_delo=0;

            int por_num1=0;
            int por_num2=0;
while(true){
            int tmp=0;
            boolean go_break = false;
            String KodAbiturienta = new String();
            String oldKodAbiturienta = new String();
            String newNomerDela = new String();
            String NomerDela = new String();
            String str;
            str = abit_Gr.getSpecial3();
            tmp = str.indexOf('<',cur_delo)-1;
            if( tmp == -2 ) break;
            NomerDela=str.substring(str.indexOf('>',cur_delo)+1,tmp+1);
            cur_delo=tmp;
            tmp = str.indexOf('>',cur_delo)-1;
            newNomerDela=str.substring(str.indexOf('<',cur_delo)+1,tmp+1);
            cur_delo=tmp;
            stmt = conn.prepareStatement("SELECT MAX(KodAbiturienta) FROM Abiturient");
            rs = stmt.executeQuery();
            if(rs.next()) {
              KodAbiturienta = ""+(rs.getInt(1)+1);

// ����������� � ����� ��� � �����_����������� ����������� ������� ����������
// ����� ����������� �� ��
              stmt = conn.prepareStatement("SELECT KodVuza,DokumentyHranjatsja,KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,TipDokumenta,NomerDokumenta,DataRojdenija,Pol,SrokObuchenija,NomerSertifikata,KopijaSertifikata,Grajdanstvo,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,TipOkonchennogoZavedenija,NomerShkoly,KodZavedenija,KodMedali,TrudovajaDejatelnost,NapravlenieOtPredprijatija,KodLgot,NomerPlatnogoDogovora,KodKursov,InostrannyjJazyk,NujdaetsjaVObschejitii,KodTselevogoPriema,KodOblasti,KodRajona,KodPunkta,KodAbiturienta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,NomerPotoka,Sobesedovanie,Prinjat,GruppaOplativshego,Ball,KodFormyOb,KodOsnovyOb,SeriaAtt,NomerAtt FROM Abiturient WHERE NomerLichnogoDela LIKE ? AND (Prinjat LIKE '�' OR Prinjat IS NULL)");
              stmt.setObject(1, NomerDela, Types.VARCHAR);
              rs = stmt.executeQuery();
              if(rs.next()) {
// �������� ������ ������ ������� ���� �� ������������
                 stmt2 = conn.prepareStatement("SELECT * FROM Abiturient WHERE KodVuza LIKE ? AND NomerLichnogoDela LIKE ?");
                 stmt2.setObject(1, session.getAttribute("kVuza"),Types.VARCHAR);
                 stmt2.setObject(2, newNomerDela,Types.VARCHAR);
                 rs2=stmt2.executeQuery();
                 if(rs2.next()) {
// ������! ��������� ����� ������� ���� ����������
                 abit_Gr.setSpecial5("yes");
                 MessageBean msge = new MessageBean();
                 msge.setStatus("������!");
                 msge.setMessage("���� � �������: "+newNomerDela+" ��� ����������.");
                 msge.setId(++por_num2);
                 messages_bad.add(msge);
                 continue;
                 }

                 MessageBean message = new MessageBean();

//������� �.�. �����������
                 message.setDescr(rs.getString(5)+" "+rs.getString(6).substring(0,1).toUpperCase()+"."+rs.getString(7).substring(0,1).toUpperCase()+".");

//����� ������� ����
                 message.setStatus(rs.getString(4));

//����� ������ ����
                 message.setMessage(newNomerDela);

                 oldKodAbiturienta = rs.getString(33);

                 int index=0;
                 for(int i=0;i<newNomerDela.length();i++) {
                   for(int j=0;j<=9;j++) {
                     if((newNomerDela.substring(i,i+1)).equals(""+j)) { 
                       index = i; 
                       break;
                     }
                   }
                   if(index!=0) break;
                   
                 }

                 String kodSpec = newNomerDela.substring(0,index);

                 stmt2 = conn.prepareStatement("SELECT KodSpetsialnosti FROM Spetsialnosti,Fakultety WHERE Spetsialnosti.KodFakulteta=Fakultety.KodFakulteta AND KodVuza LIKE ? AND Abbreviatura LIKE ?");
                 stmt2.setObject(1, session.getAttribute("kVuza"),Types.VARCHAR);
                 stmt2.setObject(2, kodSpec,Types.VARCHAR);
                 rs2=stmt2.executeQuery();
                 if(rs2.next()) kodSpec = rs2.getString(1);
                 else {

// ������! ������������� ���������� ������ ������� ���� �� ����������
                 abit_Gr.setSpecial5("yes");
                 MessageBean msge = new MessageBean();
                 msge.setStatus("������!");
                 msge.setMessage("������ �������� ���� � �������: "+newNomerDela+", �.�. ����-�� "+kodSpec+" �� ����������!");
                 msge.setId(++por_num2);
                 messages_bad.add(msge);
                 continue;
                 }

//��� �� = ��� - � ������ ������ (���� ������������ ����� '��������� ��������� �� ������ ����-��: ���')
                 if(abit_Gr.getSpecial4().equals("no")){
                   stmt2 = conn.prepareStatement("UPDATE Abiturient SET DokumentyHranjatsja='�' WHERE KodAbiturienta LIKE ?");
                   stmt2.setObject(1, oldKodAbiturienta,Types.VARCHAR);
                   stmt2.executeUpdate();
                 }
                
// �����������

                 stmt2 = conn.prepareStatement("INSERT INTO Abiturient(KodVuza,DokumentyHranjatsja,KodSpetsialnosti,NomerLichnogoDela,Familija,Imja,Otchestvo,TipDokumenta,NomerDokumenta,DataRojdenija,Pol,SrokObuchenija,NomerSertifikata,KopijaSertifikata,Grajdanstvo,GodOkonchanijaSrObrazovanija,GdePoluchilSrObrazovanie,TipOkonchennogoZavedenija,NomerShkoly,KodZavedenija,KodMedali,TrudovajaDejatelnost,NapravlenieOtPredprijatija,KodLgot,NomerPlatnogoDogovora,KodKursov,InostrannyjJazyk,NujdaetsjaVObschejitii,KodTselevogoPriema,KodOblasti,KodRajona,KodPunkta,KodAbiturienta,SeriaDokumenta,DataVydDokumenta,KemVydDokument,TipDokSredObraz,NomerPotoka,Sobesedovanie,Prinjat,GruppaOplativshego,Ball,KodFormyOb,KodOsnovyOb,SeriaAtt,NomerAtt,KodGruppy) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                 stmt2.setObject(1,  rs.getString(1),Types.INTEGER);
                 stmt2.setObject(2,  "�",Types.VARCHAR);
                 stmt2.setObject(3,  kodSpec,Types.VARCHAR);
                 stmt2.setObject(4,  newNomerDela,Types.VARCHAR);
                 stmt2.setObject(5,  rs.getString(5),Types.VARCHAR);
                 stmt2.setObject(6,  rs.getString(6),Types.VARCHAR);
                 stmt2.setObject(7,  rs.getString(7),Types.VARCHAR);
                 stmt2.setObject(8,  rs.getString(8),Types.VARCHAR);
                 stmt2.setObject(9,  rs.getString(9),Types.VARCHAR);
                 stmt2.setObject(10, rs.getString(10),Types.VARCHAR);
                 stmt2.setObject(11, rs.getString(11),Types.VARCHAR);
                 stmt2.setObject(12, rs.getString(12),Types.VARCHAR);
                 stmt2.setObject(13, rs.getString(13),Types.VARCHAR);
                 stmt2.setObject(14, rs.getString(14),Types.VARCHAR);
                 stmt2.setObject(15, rs.getString(15),Types.VARCHAR);
                 stmt2.setObject(16, rs.getString(16),Types.VARCHAR);
                 stmt2.setObject(17, rs.getString(17),Types.VARCHAR);
                 stmt2.setObject(18, rs.getString(18),Types.VARCHAR);
                 stmt2.setObject(19, rs.getString(19),Types.VARCHAR);
                 stmt2.setObject(20, rs.getString(20),Types.INTEGER);
                 stmt2.setObject(21, rs.getString(21),Types.INTEGER);
                 stmt2.setObject(22, rs.getString(22),Types.VARCHAR);
                 stmt2.setObject(23, rs.getString(23),Types.VARCHAR);
                 stmt2.setObject(24, rs.getString(24),Types.INTEGER);
                 stmt2.setObject(25, rs.getString(25),Types.VARCHAR);
                 stmt2.setObject(26, rs.getString(26),Types.INTEGER);
                 stmt2.setObject(27, rs.getString(27),Types.VARCHAR);
                 stmt2.setObject(28, rs.getString(28),Types.VARCHAR);
                 stmt2.setObject(29, rs.getString(29),Types.INTEGER);
                 stmt2.setObject(30, rs.getString(30),Types.INTEGER);
                 stmt2.setObject(31, rs.getString(31),Types.INTEGER);
                 stmt2.setObject(32, rs.getString(32),Types.INTEGER);
                 stmt2.setObject(33, KodAbiturienta  ,Types.INTEGER);
                 stmt2.setObject(34, rs.getString(34),Types.VARCHAR);
                 stmt2.setObject(35, rs.getString(35),Types.VARCHAR);
                 stmt2.setObject(36, rs.getString(36),Types.VARCHAR);
                 stmt2.setObject(37, rs.getString(37),Types.VARCHAR);
                 stmt2.setObject(38, rs.getString(38),Types.INTEGER);
                 stmt2.setObject(39, rs.getString(39),Types.VARCHAR);
                 stmt2.setObject(40, rs.getString(40),Types.VARCHAR);
                 stmt2.setObject(41, rs.getString(41),Types.VARCHAR);
                 stmt2.setObject(42, rs.getString(42),Types.INTEGER);
                 stmt2.setObject(43, rs.getString(43),Types.INTEGER);
                 stmt2.setObject(44, rs.getString(44),Types.INTEGER);
                 stmt2.setObject(45, rs.getString(45),Types.VARCHAR);
                 stmt2.setObject(46, rs.getString(46),Types.VARCHAR);
                 stmt2.setObject(47, "1",Types.VARCHAR);              // KodGruppy
                 message.setInitiator(KodAbiturienta);
                 stmt2.executeUpdate();

// ��������� ����� ��������, ���� ��� ������

                 if(abit_Gr.getSpecial7()!=null){
                   stmt = conn.prepareStatement("UPDATE Abiturient SET SrokObuchenija='"+abit_Gr.getSpecial6()+"' WHERE KodAbiturienta LIKE ?");
                   stmt.setObject(1, KodAbiturienta,Types.VARCHAR);
                   stmt.executeUpdate();
                 }

// ����������� ��������������� ������
                 stmt = conn.prepareStatement("SELECT KodPredmeta,Otsenka,Data,Apelljatsija,From_Ege FROM Otsenki WHERE KodAbiturienta LIKE ?");
                 stmt.setObject(1, oldKodAbiturienta,Types.VARCHAR);
                 rs = stmt.executeQuery();
                 while(rs.next()){
                   stmt2 = conn.prepareStatement("INSERT INTO Otsenki(KodPredmeta,Otsenka,Data,Apelljatsija,From_Ege,KodAbiturienta) VALUES(?,?,?,?,?,?)");
                   stmt2.setObject(1, rs.getString(1),Types.INTEGER);
                   stmt2.setObject(2, rs.getString(2),Types.VARCHAR);
                   stmt2.setObject(3, rs.getString(3),Types.VARCHAR);
                   stmt2.setObject(4, rs.getString(4),Types.VARCHAR);
                   stmt2.setObject(5, rs.getString(5),Types.VARCHAR);
                   stmt2.setObject(6, KodAbiturienta, Types.INTEGER);
                   stmt2.executeUpdate();
                 }

// ����������� ���������� �������� ������
                 stmt = conn.prepareStatement("SELECT KodPredmeta,OtsenkaZajavl,OtsenkaEge,OtsenkaAtt FROM ZajavlennyeShkolnyeOtsenki WHERE KodAbiturienta LIKE ?");
                 stmt.setObject(1, oldKodAbiturienta,Types.VARCHAR);
                 rs = stmt.executeQuery();
                 while(rs.next()){
                   stmt2 = conn.prepareStatement("INSERT INTO ZajavlennyeShkolnyeOtsenki(KodPredmeta,OtsenkaZajavl,OtsenkaEge,OtsenkaAtt,KodAbiturienta) VALUES(?,?,?,?,?)");
                   stmt2.setObject(1, rs.getString(1),Types.INTEGER);
                   stmt2.setObject(2, rs.getString(2),Types.VARCHAR);
                   stmt2.setObject(3, rs.getString(3),Types.VARCHAR);
                   stmt2.setObject(4, rs.getString(4),Types.VARCHAR);
                   stmt2.setObject(5, KodAbiturienta ,Types.INTEGER);
                   stmt2.executeUpdate();
                 }

                message.setId(++por_num1);
                messages_good.add(message);

              } else {
                abit_Gr.setSpecial5("yes");

                MessageBean message = new MessageBean();
                message.setStatus("������!");

// ����� ����������� �� ��
                stmt = conn.prepareStatement("SELECT KodAbiturienta FROM Abiturient WHERE NomerLichnogoDela LIKE ? AND Prinjat NOT LIKE '�' AND Prinjat IS NOT NULL");
                stmt.setObject(1, NomerDela, Types.VARCHAR);
                rs = stmt.executeQuery();
                if(rs.next()) {
                  message.setMessage("���������� � �������: "+NomerDela+" ����� ������������� ������� ����������.");
                  messages_bad.add(message);
                  message = new MessageBean();
                  message.setId(++por_num2);
                  message.setStatus("��������!");
                  message.setMessage("����������� ������ ��� ����������� ������������ ����������, ������� �������!");

                } else {
                  message.setMessage("���������� � �������: "+NomerDela+" �� ������ � ��.");
                }

                message.setId(++por_num2);
                messages_bad.add(message);
                 }
           }//if

  if(go_break) break;

  }//while
              form.setAction(us.getClientIntName("full","act-&-view"));
        }
}
        catch ( SQLException e ) {
          request.setAttribute("SQLException", e);
          return mapping.findForward("error");
        }
        catch ( java.lang.Exception e ) {
          request.setAttribute("JAVAexception", e);
          return mapping.findForward("error");
        }
        finally {
          if ( rs != null ) {
               try {
                     rs.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( stmt != null ) {
               try {
                     stmt.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
          if ( conn != null ) {
               try {
                     conn.close();
                   } catch (Exception e) {
                    ;
                                         }
          }
        }
        request.setAttribute("abit_Gr", abit_Gr);
        request.setAttribute("messages_good", messages_good);
        request.setAttribute("messages_bad", messages_bad);
     }
        if(f!=null) return f;
        if(error) return mapping.findForward("error");
        if(copyld_f) return mapping.findForward("copyld_f");
        return mapping.findForward("success");
    }
}