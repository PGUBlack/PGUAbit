package abiturient.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import abiturient.model.App;


@Repository("appDao")
public class AppDaoImpl extends AbstractDao<Integer, App> implements AppDao {

	public App findById(int id) {
		App app = getByKey(id);
		return app;
	}

	@SuppressWarnings("unchecked")
	public List<App> findAllApps() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstName"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<App> apps = (List<App>) criteria.list();

		return apps;
	}

	public void save(App app) {
		persist(app);
	}

}
