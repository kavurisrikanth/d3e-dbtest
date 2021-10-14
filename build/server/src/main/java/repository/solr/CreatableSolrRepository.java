package repository.solr;

@org.springframework.stereotype.Repository
public interface CreatableSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<models.Creatable, Long> {}
