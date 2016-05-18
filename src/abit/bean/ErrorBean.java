package abit.bean;

// Класс с описанием атрибутов Bean-компонентов приложения

public class ErrorBean extends IDBean {
  
    protected Integer idRemark  = null;
    protected Integer idStatus  = null;
    protected String  status    = null;
    protected String  abbr      = null;
    protected String  name      = null;
    protected String  descr     = null;
    protected String  tip       = null;
    protected String  comment   = null;
    protected String  data      = null;
    protected String  time      = null;
    protected String  remark    = null;
    protected Integer idDiv     = null;
    protected Integer idUser    = null;
    protected String  special1  = null;

/****************************************************************************/

// Каждая функция типа:
// getXxx() предназначена для инициализации в Bean-компонента,
// setXxx() предназначена для получения данных из Bean-компонента.

/****************************************************************************/

    public Integer getIdRemark() 
		{ return idRemark;}
    public void setIdRemark(Integer value) 
		{ idRemark = value;}

/****************************************************************************/

    public Integer getIdStatus() 
		{ return idStatus;}
    public void setIdStatus(Integer value) 
		{ idStatus = value;}

/****************************************************************************/

    public String getStatus() 
		{ return status; }

    public String getAbbr() 
		{ return abbr; }

    public String getName() 
		{ return name; }

    public String getDescr() 
		{ return descr; }

    public String getTip() 
		{ return tip; }

    public void setStatus(String value) 
		{status = value; }

    public void setAbbr(String value) 
		{abbr = value; }

    public void setName(String value) 
		{name = value; }

    public void setDescr(String value) 
		{descr = value; }

    public void setTip(String value) 
		{tip = value; }

/****************************************************************************/

   public String getComment() 
		{ return comment;}
    public void setComment( String value ) 
		{ comment= value; }

/****************************************************************************/

    public String getData()
		{return data;}
    public void setData(String value) 
		{data = value;}

/****************************************************************************/

    public String getTime() 
		{ return time; }
    public void setTime(String value) 
		{time = value;}

/****************************************************************************/

    public String getRemark()
		{return remark;}
    public void setRemark(String value) 
		{remark = value; }

/****************************************************************************/

    public Integer getIdDiv()
		{return idDiv;}
    public void setIdDiv(Integer value) 
		{ idDiv = value; }

/****************************************************************************/

    public Integer getIdUser()
		{return idUser;}
    public void setIdUser(Integer value) 
		{ idUser = value; }

/****************************************************************************/

    public String getSpecial1() {
        return special1;
    }
    public void setSpecial1(String value) {
        special1 = value;
    }
}