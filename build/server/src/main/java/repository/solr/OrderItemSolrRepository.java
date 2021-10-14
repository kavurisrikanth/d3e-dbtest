package repository.solr;

@org.springframework.stereotype.Repository
public interface OrderItemSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<models.OrderItem, Long> {}
