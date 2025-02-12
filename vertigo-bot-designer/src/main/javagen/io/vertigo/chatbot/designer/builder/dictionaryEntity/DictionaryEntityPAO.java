package io.vertigo.chatbot.designer.builder.dictionaryEntity;

import javax.inject.Inject;

import io.vertigo.core.node.Node;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datamodel.task.definitions.TaskDefinition;
import io.vertigo.datamodel.task.model.Task;
import io.vertigo.datamodel.task.model.TaskBuilder;
import io.vertigo.datastore.impl.dao.StoreServices;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
 @Generated
public final class DictionaryEntityPAO implements StoreServices {
	private final TaskManager taskManager;

	/**
	 * Constructeur.
	 * @param taskManager Manager des Task
	 */
	@Inject
	public DictionaryEntityPAO(final TaskManager taskManager) {
		Assertion.check().isNotNull(taskManager);
		//-----
		this.taskManager = taskManager;
	}

	/**
	 * Creates a taskBuilder.
	 * @param name  the name of the task
	 * @return the builder 
	 */
	private static TaskBuilder createTaskBuilder(final String name) {
		final TaskDefinition taskDefinition = Node.getNode().getDefinitionSpace().resolve(name, TaskDefinition.class);
		return Task.builder(taskDefinition);
	}

	/**
	 * Execute la tache TkGetDictionaryEntityWrapperByBotId.
	 * @param botId Long
	 * @param separator String
	 * @return DtList de DictionaryEntityWrapper res
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetDictionaryEntityWrapperByBotId",
			request = "select\n" + 
 " 				ent.dic_ent_id as dic_ent_id,\n" + 
 "              	ent.label as dictionary_entity_label,\n" + 
 "              	STRING_AGG (syn.label, #separator#) as synonyms_list\n" + 
 "  				from dictionary_entity ent\n" + 
 "  				left join synonym syn on syn.dic_ent_id = ent.dic_ent_id\n" + 
 "  			\n" + 
 "  				where ent.bot_id = #botId#\n" + 
 "  				group by ent.dic_ent_id",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtDictionaryEntityWrapper")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.chatbot.designer.domain.DictionaryEntityWrapper> getDictionaryEntityWrapperByBotId(@io.vertigo.datamodel.task.proxy.TaskInput(name = "botId", smartType = "STyId") final Long botId, @io.vertigo.datamodel.task.proxy.TaskInput(name = "separator", smartType = "STyLabel") final String separator) {
		final Task task = createTaskBuilder("TkGetDictionaryEntityWrapperByBotId")
				.addValue("botId", botId)
				.addValue("separator", separator)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	/**
	 * Execute la tache TkGetTuplesSynonym.
	 * @param botId Long
	 * @param words List de String
	 * @return DtList de TupleSynonymIhm tupleSynonymIhm
	*/
	@io.vertigo.datamodel.task.proxy.TaskAnnotation(
			name = "TkGetTuplesSynonym",
			request = "SELECT word.label as word, syn.label as synonym_label\n" + 
 " 				from dictionary_entity ent\n" + 
 " 				join synonym word on word.dic_ent_id = ent.dic_ent_id\n" + 
 " 				join synonym syn on syn.dic_ent_id = ent.dic_ent_id\n" + 
 " 				where 1=1\n" + 
 " 				and syn.syn_id != word.syn_id\n" + 
 " 				and word.label in (#words.rownum#)\n" + 
 " 				and ent.bot_id = #botId#",
			taskEngineClass = io.vertigo.basics.task.TaskEngineSelect.class)
	@io.vertigo.datamodel.task.proxy.TaskOutput(smartType = "STyDtTupleSynonymIhm")
	public io.vertigo.datamodel.structure.model.DtList<io.vertigo.chatbot.designer.domain.TupleSynonymIhm> getTuplesSynonym(@io.vertigo.datamodel.task.proxy.TaskInput(name = "botId", smartType = "STyId") final Long botId, @io.vertigo.datamodel.task.proxy.TaskInput(name = "words", smartType = "STyLabel") final java.util.List<String> words) {
		final Task task = createTaskBuilder("TkGetTuplesSynonym")
				.addValue("botId", botId)
				.addValue("words", words)
				.build();
		return getTaskManager()
				.execute(task)
				.getResult();
	}

	private TaskManager getTaskManager() {
		return taskManager;
	}
}
