package io.vertigo.chatbot.domain;

import java.util.Arrays;
import java.util.Iterator;

import io.vertigo.dynamo.domain.metamodel.DtFieldName;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class DtDefinitions implements Iterable<Class<?>> {

	/**
	 * Enumération des DtDefinitions.
	 */
	public enum Definitions {
		/** Objet de données BotExport. */
		BotExport(io.vertigo.chatbot.commons.domain.BotExport.class),
		/** Objet de données Chatbot. */
		Chatbot(io.vertigo.chatbot.commons.domain.Chatbot.class),
		/** Objet de données ChatbotNode. */
		ChatbotNode(io.vertigo.chatbot.commons.domain.ChatbotNode.class),
		/** Objet de données ExecutorConfiguration. */
		ExecutorConfiguration(io.vertigo.chatbot.commons.domain.ExecutorConfiguration.class),
		/** Objet de données ExecutorTrainingCallback. */
		ExecutorTrainingCallback(io.vertigo.chatbot.commons.domain.ExecutorTrainingCallback.class),
		/** Objet de données Groups. */
		Groups(io.vertigo.chatbot.commons.domain.Groups.class),
		/** Objet de données MediaFileInfo. */
		MediaFileInfo(io.vertigo.chatbot.commons.domain.MediaFileInfo.class),
		/** Objet de données NluTrainingSentence. */
		NluTrainingSentence(io.vertigo.chatbot.commons.domain.NluTrainingSentence.class),
		/** Objet de données Person. */
		Person(io.vertigo.chatbot.commons.domain.Person.class),
		/** Objet de données RunnerInfo. */
		RunnerInfo(io.vertigo.chatbot.commons.domain.RunnerInfo.class),
		/** Objet de données SmallTalk. */
		SmallTalk(io.vertigo.chatbot.commons.domain.SmallTalk.class),
		/** Objet de données SmallTalkExport. */
		SmallTalkExport(io.vertigo.chatbot.commons.domain.SmallTalkExport.class),
		/** Objet de données StatCriteria. */
		StatCriteria(io.vertigo.chatbot.designer.domain.StatCriteria.class),
		/** Objet de données TrainerInfo. */
		TrainerInfo(io.vertigo.chatbot.commons.domain.TrainerInfo.class),
		/** Objet de données Training. */
		Training(io.vertigo.chatbot.commons.domain.Training.class),
		/** Objet de données UtterText. */
		UtterText(io.vertigo.chatbot.commons.domain.UtterText.class)		;

		private final Class<?> clazz;

		private Definitions(final Class<?> clazz) {
			this.clazz = clazz;
		}

		/** 
		 * Classe associée.
		 * @return Class d'implémentation de l'objet 
		 */
		public Class<?> getDtClass() {
			return clazz;
		}
	}

	/**
	 * Enumération des champs de BotExport.
	 */
	public enum BotExportFields implements DtFieldName<io.vertigo.chatbot.commons.domain.BotExport> {
		/** Propriété 'chatbot'. */
		bot,
		/** Propriété 'welcome'. */
		defaultText,
		/** Propriété 'welcome'. */
		welcomeText	}

	/**
	 * Enumération des champs de Chatbot.
	 */
	public enum ChatbotFields implements DtFieldName<io.vertigo.chatbot.commons.domain.Chatbot> {
		/** Propriété 'ID'. */
		botId,
		/** Propriété 'Name'. */
		name,
		/** Propriété 'Description'. */
		description,
		/** Propriété 'Creation date'. */
		creationDate,
		/** Propriété 'Status'. */
		status,
		/** Propriété 'Avatar'. */
		filIdAvatar,
		/** Propriété 'Welcome text'. */
		uttIdWelcome,
		/** Propriété 'Default text'. */
		uttIdDefault	}

	/**
	 * Enumération des champs de ChatbotNode.
	 */
	public enum ChatbotNodeFields implements DtFieldName<io.vertigo.chatbot.commons.domain.ChatbotNode> {
		/** Propriété 'ID'. */
		nodId,
		/** Propriété 'Name'. */
		name,
		/** Propriété 'URL'. */
		url,
		/** Propriété 'Dev node'. */
		isDev,
		/** Propriété 'Color'. */
		color,
		/** Propriété 'ApiKey'. */
		apiKey,
		/** Propriété 'Chatbot'. */
		botId,
		/** Propriété 'Loaded model'. */
		traId	}

	/**
	 * Enumération des champs de ExecutorConfiguration.
	 */
	public enum ExecutorConfigurationFields implements DtFieldName<io.vertigo.chatbot.commons.domain.ExecutorConfiguration> {
		/** Propriété 'Bot ID'. */
		botId,
		/** Propriété 'Node ID'. */
		nodId,
		/** Propriété 'Model ID'. */
		traId,
		/** Propriété 'Model name'. */
		modelName	}

	/**
	 * Enumération des champs de ExecutorTrainingCallback.
	 */
	public enum ExecutorTrainingCallbackFields implements DtFieldName<io.vertigo.chatbot.commons.domain.ExecutorTrainingCallback> {
		/** Propriété 'Training ID'. */
		trainingId,
		/** Propriété 'Succes'. */
		success,
		/** Propriété 'Logs'. */
		log,
		/** Propriété 'Informations'. */
		infos,
		/** Propriété 'Warnings'. */
		warnings,
		/** Propriété 'Client Api Key'. */
		apiKey	}

	/**
	 * Enumération des champs de Groups.
	 */
	public enum GroupsFields implements DtFieldName<io.vertigo.chatbot.commons.domain.Groups> {
		/** Propriété 'Id'. */
		grpId,
		/** Propriété 'Name'. */
		name	}

	/**
	 * Enumération des champs de MediaFileInfo.
	 */
	public enum MediaFileInfoFields implements DtFieldName<io.vertigo.chatbot.commons.domain.MediaFileInfo> {
		/** Propriété 'Id'. */
		filId,
		/** Propriété 'Name'. */
		fileName,
		/** Propriété 'MimeType'. */
		mimeType,
		/** Propriété 'Size'. */
		length,
		/** Propriété 'Modification Date'. */
		lastModified,
		/** Propriété 'path'. */
		filePath,
		/** Propriété 'data'. */
		fileData	}

	/**
	 * Enumération des champs de NluTrainingSentence.
	 */
	public enum NluTrainingSentenceFields implements DtFieldName<io.vertigo.chatbot.commons.domain.NluTrainingSentence> {
		/** Propriété 'ID'. */
		ntsId,
		/** Propriété 'Text'. */
		text,
		/** Propriété 'SmallTalk'. */
		smtId	}

	/**
	 * Enumération des champs de Person.
	 */
	public enum PersonFields implements DtFieldName<io.vertigo.chatbot.commons.domain.Person> {
		/** Propriété 'Id'. */
		perId,
		/** Propriété 'Login'. */
		login,
		/** Propriété 'Name'. */
		name,
		/** Propriété 'Password'. */
		password,
		/** Propriété 'Group'. */
		grpId	}

	/**
	 * Enumération des champs de RunnerInfo.
	 */
	public enum RunnerInfoFields implements DtFieldName<io.vertigo.chatbot.commons.domain.RunnerInfo> {
		/** Propriété 'Name'. */
		name,
		/** Propriété 'Node state'. */
		state,
		/** Propriété 'Component version'. */
		agentVersion,
		/** Propriété 'Model version'. */
		loadedModelVersion	}

	/**
	 * Enumération des champs de SmallTalk.
	 */
	public enum SmallTalkFields implements DtFieldName<io.vertigo.chatbot.commons.domain.SmallTalk> {
		/** Propriété 'ID'. */
		smtId,
		/** Propriété 'Title'. */
		title,
		/** Propriété 'Description'. */
		description,
		/** Propriété 'Enabled'. */
		isEnabled,
		/** Propriété 'Chatbot'. */
		botId	}

	/**
	 * Enumération des champs de SmallTalkExport.
	 */
	public enum SmallTalkExportFields implements DtFieldName<io.vertigo.chatbot.commons.domain.SmallTalkExport> {
		/** Propriété 'SmallTalk'. */
		smallTalk,
		/** Propriété 'nluTrainingSentences'. */
		nluTrainingSentences,
		/** Propriété 'utterTexts'. */
		utterTexts	}

	/**
	 * Enumération des champs de StatCriteria.
	 */
	public enum StatCriteriaFields implements DtFieldName<io.vertigo.chatbot.designer.domain.StatCriteria> {
		/** Propriété 'Chatbot selection'. */
		botId,
		/** Propriété 'Time option'. */
		timeOption	}

	/**
	 * Enumération des champs de TrainerInfo.
	 */
	public enum TrainerInfoFields implements DtFieldName<io.vertigo.chatbot.commons.domain.TrainerInfo> {
		/** Propriété 'Name'. */
		name,
		/** Propriété 'Training in progress'. */
		trainingInProgress,
		/** Propriété 'Training state'. */
		trainingState,
		/** Propriété 'Training log'. */
		latestTrainingLog,
		/** Propriété 'Start time'. */
		startTime,
		/** Propriété 'End time'. */
		endTime,
		/** Propriété 'Training percentage'. */
		trainingPercent,
		/** Propriété 'Duration'. */
		duration	}

	/**
	 * Enumération des champs de Training.
	 */
	public enum TrainingFields implements DtFieldName<io.vertigo.chatbot.commons.domain.Training> {
		/** Propriété 'ID'. */
		traId,
		/** Propriété 'Start time'. */
		startTime,
		/** Propriété 'End time'. */
		endTime,
		/** Propriété 'Version'. */
		versionNumber,
		/** Propriété 'Status'. */
		status,
		/** Propriété 'Log'. */
		log,
		/** Propriété 'Informations'. */
		infos,
		/** Propriété 'Warnings'. */
		warnings,
		/** Propriété 'Duration'. */
		duration,
		/** Propriété 'Chatbot'. */
		botId,
		/** Propriété 'Model'. */
		filIdModel	}

	/**
	 * Enumération des champs de UtterText.
	 */
	public enum UtterTextFields implements DtFieldName<io.vertigo.chatbot.commons.domain.UtterText> {
		/** Propriété 'ID'. */
		uttId,
		/** Propriété 'Text'. */
		text,
		/** Propriété 'SmallTalk'. */
		smtId	}

	/** {@inheritDoc} */
	@Override
	public Iterator<Class<?>> iterator() {
		return new Iterator<Class<?>>() {
			private Iterator<Definitions> it = Arrays.asList(Definitions.values()).iterator();

			/** {@inheritDoc} */
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			/** {@inheritDoc} */
			@Override
			public Class<?> next() {
				return it.next().getDtClass();
			}
		};
	}
}
