package abiturient.service;

import java.util.List;

import abiturient.model.App;


public interface AppService {
	
	App findById(int id);
	
	void saveApp(App app);

	List<App> findAllApps();

}