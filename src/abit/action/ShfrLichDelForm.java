package abit.action;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import abit.bean.AbiturientBean;
import java.util.ArrayList; 
import abit.util.StringUtil;

public class ShfrLichDelForm extends ActionForm {

    private String    special1               = null;
    private String    special2               = null;
    private String    special3               = null;
    private String    special4               = null;
    private String    special5               = null;
    private String    special6               = null;
    private String    special7               = null;
    private String    special8               = null;
    private String    abbreviatura           = null;
    private String    shifrFakulteta         = null;
    private String    action                 = null;
    private Integer   kodFakulteta           = null;
    private Integer   kodSpetsialnosti       = null;
    private String  dataRojdenija                = null;
    
    public Integer   getKodSpetsialnosti()      { return kodSpetsialnosti;      }
    public Integer   getKodFakulteta()          { return kodFakulteta;          }
    public String    getShifrFakulteta()        { return shifrFakulteta;        }
    public String    getSpecial1()              { return special1;              }
    public String    getSpecial2()              { return special2;              }
    public String    getSpecial3()              { return special3;              }
    public String    getSpecial4()              { return special4;              }
    public String    getSpecial5()              { return special5;              }
    public String    getSpecial6()              { return special6;              }
    public String    getSpecial7()              { return special7;              }
    public String    getSpecial8()              { return special8;              }
    public String    getAction()                { return action;                }
    public String  getDataRojdenija()                { return dataRojdenija;                }
    
    public void setKodSpetsialnosti(Integer value)   { kodSpetsialnosti       = value;        }
    public void setKodFakulteta(Integer value)       { kodFakulteta           = value;        }
    public void setShifrFakulteta(String value)      { shifrFakulteta         = value.trim(); }
    public void setSpecial1(String value)            { special1               = value.trim(); }
    public void setSpecial2(String value)            { special2               = value.trim(); }
    public void setSpecial3(String value)            { special3               = value.trim(); }
    public void setSpecial4(String value)            { special4               = value.trim(); }
    public void setSpecial5(String value)            { special5               = value.trim(); }
    public void setSpecial6(String value)            { special6               = value.trim(); }
    public void setSpecial7(String value)            { special7               = value.trim(); }
    public void setSpecial8(String value)   	     { special8               = value.trim(); }
    public void setAction(String value)              { action                 = value.trim(); }
    public void setDataRojdenija(String value)                 { dataRojdenija                = value.trim(); }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        shifrFakulteta        = null;
        kodSpetsialnosti      = null;
        kodFakulteta          = null;
        special1              = null;
        special2              = null;
        special3              = null;
        special4              = null;
        special5              = null;
        special6              = null;
        special7              = null;
        special8              = null;
        action                = null;
        dataRojdenija = null;
    }

    public void setBean( AbiturientBean bean, HttpServletRequest request, ActionErrors errors ) throws ServletException {

        if ( bean != null) {

            if ( bean.getShifrFakulteta() != null ) {
                shifrFakulteta = bean.getShifrFakulteta();
            }

            kodSpetsialnosti = bean.getKodSpetsialnosti();

            kodFakulteta = bean.getKodFakulteta();

            if ( bean.getSpecial1() != null ) {
                special1 = bean.getSpecial1();
            }

            if ( bean.getSpecial2() != null ) {
                special2 = bean.getSpecial2();
            }

            if ( bean.getSpecial3() != null ) {
                special3 = bean.getSpecial3();
            }

            if ( bean.getSpecial4() != null ) {
                special4 = bean.getSpecial4();
            }

            if ( bean.getSpecial5() != null ) {
                special5 = bean.getSpecial5();
            }

            if ( bean.getSpecial6() != null ) {
                special6 = bean.getSpecial6();
            }

            if ( bean.getSpecial7() != null ) {
                special7 = bean.getSpecial7();
            }

            if ( bean.getSpecial8() != null ) {
                special8 = bean.getSpecial8();
            }
            if ( bean.getDataRojdenija() != null ) {
                dataRojdenija = bean.getDataRojdenija();
            }
        }
    }

    public AbiturientBean getBean( HttpServletRequest request, ActionErrors errors ) throws ServletException {

        AbiturientBean bean = new AbiturientBean();           

        if ( shifrFakulteta!=null && !shifrFakulteta.equals("") ) {
            bean.setShifrFakulteta(StringUtil.toDB(shifrFakulteta));
        }

        bean.setKodSpetsialnosti(kodSpetsialnosti);

        bean.setKodFakulteta(kodFakulteta);

        if ( special1!=null && !special1.equals("") ) {
            bean.setSpecial1(StringUtil.toDB(special1));
        }

        if ( special2!=null && !special2.equals("") ) {
            bean.setSpecial2(StringUtil.toDB(special2));
        }

        if ( special3!=null && !special3.equals("") ) {
            bean.setSpecial3(StringUtil.toDB(special3));
        }

        if ( special4!=null && !special4.equals("") ) {
            bean.setSpecial4(StringUtil.toDB(special4));
        }

        if ( special5!=null && !special5.equals("") ) {
            bean.setSpecial5(StringUtil.toDB(special5));
        }

        if ( special6!=null && !special6.equals("") ) {
            bean.setSpecial6(StringUtil.toDB(special6));
        }

        if ( special7!=null && !special7.equals("") ) {
            bean.setSpecial7(StringUtil.toDB(special7));
        }

        if ( special8!=null && !special8.equals("") ) {
            bean.setSpecial8(StringUtil.toDB(special8));
        }
        if ( dataRojdenija!=null && !dataRojdenija.equals("") ) {
            bean.setDataRojdenija(StringUtil.toDB(dataRojdenija));
        }
        return bean;
    }      
}