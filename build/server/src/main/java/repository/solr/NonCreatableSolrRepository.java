package repository.solr;

@org.springframework.stereotype.Repository
public interface NonCreatableSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<
        models.NonCreatable, Long> {}
