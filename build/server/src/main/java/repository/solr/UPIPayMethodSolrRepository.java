package repository.solr;

@org.springframework.stereotype.Repository
public interface UPIPayMethodSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<
        models.UPIPayMethod, Long> {}
