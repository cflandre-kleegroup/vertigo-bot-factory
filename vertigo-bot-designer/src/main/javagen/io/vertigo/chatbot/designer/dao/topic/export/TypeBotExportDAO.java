package io.vertigo.chatbot.designer.dao.topic.export;

import javax.inject.Inject;

import io.vertigo.core.lang.Generated;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.chatbot.designer.domain.topic.export.TypeBotExport;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class TypeBotExportDAO extends DAO<TypeBotExport, java.lang.String> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 * @param smartTypeManager SmartTypeManager
	 */
	@Inject
	public TypeBotExportDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(TypeBotExport.class, entityStoreManager, taskManager, smartTypeManager);
	}

}