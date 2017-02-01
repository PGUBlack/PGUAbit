package abiturient.dao;

import java.util.List;

import abiturient.model.App;


public interface AppDao {

	App findById(int id);
	
	void save(App app);
	
	List<App> findAllApps();

}

