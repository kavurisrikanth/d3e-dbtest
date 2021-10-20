package d3e.core;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.UnexpectedRollbackException;

import store.D3EEntityManagerProvider;
import store.DataStoreEvent;

@Component
public class TransactionWrapper {

	@Autowired
	private D3ESubscription subscription;

	@Autowired
	private TransactionDeligate deligate;

	@Autowired
	private D3EEntityManagerProvider managerProvider;

	public void doInTransaction(TransactionDeligate.ToRun run) throws ServletException, IOException {
		boolean created = createTransactionManager();
		boolean success = false;
		try {
			if (created) {
				managerProvider.create();
			}
			deligate.run(run);
			if (created) {
				publishEvents();
			}
			success = true;
		} catch (UnexpectedRollbackException e) {
			D3ELogger.info("Transaction failed");
		} catch (Exception e) {
			D3ELogger.printStackTrace(e);
			throw new RuntimeException(e);
		} finally {
			if (created) {
				if (!success) {
					TransactionManager manager = TransactionManager.get();
					if (manager != null) {
						manager.clearChanges();
					}
				}
				TransactionManager.remove();
			}
		}
		if (created) {
			managerProvider.clear();
		}
	}

	private void publishEvents() throws ServletException, IOException {
		deligate.readOnly(() -> {
			TransactionManager manager = TransactionManager.get();
			TransactionManager.remove();
			createTransactionManager();
			manager.commit((type, entity) -> {
				try {
					DataStoreEvent event = new DataStoreEvent(entity);
					event.setType(type);
					subscription.handleContextStart(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			manager.clearChanges();
		});
		if (!TransactionManager.get().isEmpty()) {
			publishEvents();
		}
	}

	private boolean createTransactionManager() {
		TransactionManager manager = TransactionManager.get();
		if (manager == null) {
			manager = new TransactionManager();
			TransactionManager.set(manager);
			return true;
		}
		return false;
	}
}
