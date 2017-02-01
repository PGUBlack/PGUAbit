package abiturient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import abiturient.dao.AppDao;
import abiturient.model.App;


@Service("appService")
@Transactional
public class AppServiceImpl implements AppService{

	@Autowired
	private AppDao dao;

	
	public App findById(int id) {
		return dao.findById(id);
	}

	public void saveApp(App app) {
		dao.save(app);
	}

	public List<App> findAllApps() {
		return dao.findAllApps();
	}
}
