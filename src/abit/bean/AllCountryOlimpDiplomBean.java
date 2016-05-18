package abit.bean;


//Каждая функция типа:
//getXxx() предназначена для инициализации в Bean-компонента,
//setXxx() предназначена для получения данных из Bean-компонента.

public class AllCountryOlimpDiplomBean extends IDBean{
	/****************************************************************************/
    public String getFamily() {
       return family;
    }
    public void setFamily(String value) {
       family = value;
    }
/****************************************************************************/
    public String getName() {
       return name;
    }
    public void setName(String value) {
       name = value;
    }
/****************************************************************************/

}
