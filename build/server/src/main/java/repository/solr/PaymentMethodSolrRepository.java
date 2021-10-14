package repository.solr;

@org.springframework.stereotype.Repository
public interface PaymentMethodSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<
        models.PaymentMethod, Long> {}
