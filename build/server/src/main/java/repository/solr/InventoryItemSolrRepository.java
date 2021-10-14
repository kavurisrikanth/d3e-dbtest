package repository.solr;

@org.springframework.stereotype.Repository
public interface InventoryItemSolrRepository
    extends org.springframework.data.solr.repository.SolrCrudRepository<
        models.InventoryItem, Long> {}
