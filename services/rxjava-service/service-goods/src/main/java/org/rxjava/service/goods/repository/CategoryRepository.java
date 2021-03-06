package org.rxjava.service.goods.repository;

import org.apache.commons.lang3.StringUtils;
import org.rxjava.service.goods.entity.Category;
import org.rxjava.service.goods.status.CategoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author happy 2019-03-23 00:17
 */
@Repository
public interface CategoryRepository extends ReactiveSortingRepository<Category, String>, SpecialCategoryRepository {

}

interface SpecialCategoryRepository {
    Flux<Category> getList(String parentId);
}

class SpecialCategoryRepositoryImpl implements SpecialCategoryRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Category> getList(String parentId) {
        if (StringUtils.isEmpty(parentId)) {
            parentId = null;
        }
        Criteria where = Criteria.where("parentId").is(parentId)
                .and("status").is(CategoryStatus.NORMAL.name());
        Query query = Query.query(where);
        return reactiveMongoTemplate
                .find(query, Category.class);
    }
}
