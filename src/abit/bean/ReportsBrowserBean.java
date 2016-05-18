package abit.bean;


public class ReportsBrowserBean {

// Класс с описанием атрибутов Bean-компонентов приложения
    protected String  name = null;
    protected String  sign = null;
    protected String  fileName = null;
    protected String  date = null;
    protected String  time = null;
    protected String  author = null;
    protected String  special1 = null;
    protected String  special2 = null;
    protected String  id = null;
    protected String  viewToAll = null;
    protected Integer number   = null;


/****************************************************************************/

// Каждая функция типа:
// getXxx() предназначена для инициализации в Bean-компонента,
// setXxx() предназначена для получения данных из Bean-компонента.

    public Integer getNumber() {
       return number;
    }

    public void setNumber( Integer value ) {
       number = value;
    }

/****************************************************************************/

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

/****************************************************************************/

    public String getSign() {
        return sign;
    }

    public void setSign(String value) {
        sign = value;
    }

/****************************************************************************/

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String value) {
        fileName = value;
    }

/****************************************************************************/

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        date = value;
    }


/****************************************************************************/

    public String getTime() {
        return time;
    }

    public void setTime(String value) {
        time = value;
    }

/****************************************************************************/

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String value) {
        author = value;
    }

/****************************************************************************/

    public String getSpecial1() {
        return special1;
    }

    public void setSpecial1(String value) {
        special1 = value;
    }

/****************************************************************************/

    public String getSpecial2() {
        return special2;
    }

    public void setSpecial2(String value) {
        special2 = value;
    }

/****************************************************************************/

    public String getId() {
        return id;
    }

    public void setId(String value) {
        id = value;
    }

/****************************************************************************/

    public String getViewToAll() {
        return viewToAll;
    }

    public void setViewToAll(String value) {
        viewToAll = value;
    }

/****************************************************************************/
}