package repository.solr;

@org.springframework.stereotype.Repository
public interface CardPayMethodSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<
        models.CardPayMethod, Long> {}
