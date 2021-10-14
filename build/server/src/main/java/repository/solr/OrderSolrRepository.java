package repository.solr;

@org.springframework.stereotype.Repository
public interface OrderSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<models.Order, Long> {}
