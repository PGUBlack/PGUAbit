package abiturient.dao;

/**
 * Created by black on 03.02.17.
 */
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import abiturient.model.FisSpec;



@Repository("fisSpecDao")
public class FisSpecDaoImpl extends AbstractDao<Integer, FisSpec> implements FisSpecDao {

    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public FisSpec findById(int id) {
        FisSpec spec = getByKey(id);
        return spec;
    }

    public FisSpec findByName(String name) {
        logger.info("Name : {}", name);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("name", name));
        FisSpec spec = (FisSpec)crit.uniqueResult();
        return spec;
    }

    @SuppressWarnings("unchecked")
    public List<FisSpec> findAllFisSpecs() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<FisSpec> fisSpecs = (List<FisSpec>) criteria.list();

        return fisSpecs;
    }

    @SuppressWarnings("unchecked")
    public List<FisSpec> findByLevel(String level) {
        logger.info("Level : {}", level);
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.add(Restrictions.eq("level", level));
        List<FisSpec> fisSpecs = (List<FisSpec>) criteria.list();

        return fisSpecs;
    }

    public void save(FisSpec spec) {
        persist(spec);
    }

    public void deleteById(int id) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("id", id));
        FisSpec fisSpec = (FisSpec)crit.uniqueResult();
        delete(fisSpec);
    }

}