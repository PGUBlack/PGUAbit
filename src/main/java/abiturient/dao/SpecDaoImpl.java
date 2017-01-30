package abiturient.dao;

/**
 * Created by black on 27.01.17.
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import abiturient.model.Spec;



@Repository("specDao")
public class SpecDaoImpl extends AbstractDao<Integer, Spec> implements SpecDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public Spec findById(int id) {
        Spec spec = getByKey(id);
        return spec;
    }

    public Spec findByName(String name) {
        logger.info("Name : {}", name);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("name", name));
        Spec spec = (Spec)crit.uniqueResult();
        return spec;
    }

    @SuppressWarnings("unchecked")
    public List<Spec> findAllSpecs() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<Spec> specs = (List<Spec>) criteria.list();

        // No need to fetch userProfiles since we are not showing them on list page. Let them lazy load.
        // Uncomment below lines for eagerly fetching of userProfiles if you want.
		/*
		for(User user : users){
			Hibernate.initialize(user.getUserProfiles());
		}*/
        return specs;
    }

    public void save(Spec spec) {
        persist(spec);
    }

    public void deleteById(int id) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("id", id));
        Spec spec = (Spec)crit.uniqueResult();
        delete(spec);
    }

}