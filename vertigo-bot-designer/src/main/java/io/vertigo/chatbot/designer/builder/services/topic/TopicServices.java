package io.vertigo.chatbot.designer.builder.services.topic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import io.vertigo.account.authorization.annotations.SecuredOperation;
import io.vertigo.chatbot.commons.dao.topic.NluTrainingSentenceDAO;
import io.vertigo.chatbot.commons.dao.topic.TopicDAO;
import io.vertigo.chatbot.commons.domain.Chatbot;
import io.vertigo.chatbot.commons.domain.topic.KindTopic;
import io.vertigo.chatbot.commons.domain.topic.KindTopicEnum;
import io.vertigo.chatbot.commons.domain.topic.NluTrainingSentence;
import io.vertigo.chatbot.commons.domain.topic.Topic;
import io.vertigo.chatbot.commons.domain.topic.TopicCategory;
import io.vertigo.chatbot.commons.domain.topic.TopicIhm;
import io.vertigo.chatbot.commons.domain.topic.TypeTopicEnum;
import io.vertigo.chatbot.commons.domain.topic.UtterText;
import io.vertigo.chatbot.commons.multilingual.topics.TopicsMultilingualResources;
import io.vertigo.chatbot.designer.builder.services.NodeServices;
import io.vertigo.chatbot.designer.builder.topic.TopicPAO;
import io.vertigo.chatbot.designer.domain.commons.BotPredefinedTopic;
import io.vertigo.chatbot.designer.utils.HashUtils;
import io.vertigo.chatbot.domain.DtDefinitions.NluTrainingSentenceFields;
import io.vertigo.chatbot.domain.DtDefinitions.TopicFields;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.VUserException;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.component.Component;
import io.vertigo.core.util.StringUtil;
import io.vertigo.datamodel.criteria.Criteria;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtListState;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.util.VCollectors;

@Transactional
public class TopicServices implements Component, Activeable {

	@Inject
	private TopicDAO topicDAO;

	@Inject
	private TopicPAO topicPAO;

	@Inject
	private NluTrainingSentenceDAO nluTrainingSentenceDAO;

	@Inject
	private KindTopicServices kindTopicServices;

	private final Set<TopicInterfaceServices<? extends Entity>> topicInterfaceServices = new HashSet<>();

	@Inject
	private SmallTalkServices smallTalkServices;

	@Inject
	private ScriptIntentionServices scriptIntentionServices;

	@Inject
	private NodeServices nodeServices;

	public Topic findTopicById(@SecuredOperation("botVisitor") final Long id) {
		return topicDAO.get(id);
	}

	public TopicIhm findTopicIhmById(@SecuredOperation("botVisitor") final Long id) {
		return topicPAO.getTopicIhmById(id);
	}

	private Topic doSave(final Topic topic, final Chatbot bot) {
		//create code for export
		hasUniqueCode(topic);
		if (topic.getTopId() != null) {
			final Topic oldTopic = findTopicById(topic.getTopId());
			if (!oldTopic.getCode().equals(topic.getCode())) {
				nodeServices.updateNodes(bot);
			}
		}
		return topicDAO.save(topic);
	}

	public Topic save(@SecuredOperation("botContributor") final Topic topic, final Chatbot bot, final Boolean isEnabled, final DtList<NluTrainingSentence> nluTrainingSentences,
			final DtList<NluTrainingSentence> nluTrainingSentencesToDelete) {

		//check if code matches the pattern
		if (KindTopicEnum.NORMAL.name().equals(topic.getKtoCd())) {
			Assertion.check().isNotNull(nluTrainingSentences)
					.isNotNull(nluTrainingSentencesToDelete);
		}
		//create code for export
		hasUniqueCode(topic);
		// save and remove NTS
		final DtList<NluTrainingSentence> oldNluSentences = topic.getTopId() != null ? getNluTrainingSentenceByTopic(bot, topic) : new DtList<>(NluTrainingSentence.class);
		final DtList<NluTrainingSentence> ntsToSave = saveAllNotBlankNTS(topic, nluTrainingSentences);
		removeNTS(nluTrainingSentencesToDelete);
		topic.setIsEnabled(!ntsToSave.isEmpty() && isEnabled);

		final Topic oldTopic = topic.getTopId() != null ? findTopicById(topic.getTopId()) : null;
		if ((oldTopic != null && !oldTopic.getCode().equals(topic.getCode())) || !nluTrainingSentencesToDelete.isEmpty()
				|| !HashUtils.generateHashCodeForNluTrainingSentences(oldNluSentences).equals(HashUtils.generateHashCodeForNluTrainingSentences(nluTrainingSentences))) {
			nodeServices.updateNodes(bot);
		}

		return topicDAO.save(topic);
	}

	private void hasUniqueCode(final Topic topic) {
		final Optional<Long> topIdOpt = topic.getTopId() != null ? Optional.of(topic.getTopId()) : Optional.empty();
		if (topicPAO.checkUnicityTopicCode(topic.getBotId(), topic.getCode(), topIdOpt)) {
			throw new VUserException(TopicsMultilingualResources.CODE_NON_UNIQUE_ERROR);
		}
		if (TopicsUtils.checkSpecialCharacters(topic.getCode())) {
			throw new VUserException("The code cannot contain the following characters : '[', ']', '|', '¤'. ");
		}
	}

	public Topic createTopic(@SecuredOperation("botContributor") final Topic topic) {
		return topicDAO.create(topic);
	}

	public Topic createTopic(@SecuredOperation("botContributor") final String title, final String ttoCd, final String description, final Boolean isEnabled) {
		final Topic toCreate = new Topic();
		toCreate.setTitle(title);
		toCreate.setTtoCd(ttoCd);
		toCreate.setDescription(description);
		toCreate.setIsEnabled(isEnabled);
		return topicDAO.create(toCreate);
	}

	public Topic getNewTopic(@SecuredOperation("botContributor") final Chatbot bot) {
		final Topic topic = new Topic();
		topic.setBotId(bot.getBotId());
		topic.setIsEnabled(true);
		topic.setKtoCd(KindTopicEnum.NORMAL.name());
		return topic;
	}

	public void deleteTopic(@SecuredOperation("botContributor") final Chatbot bot, final Topic topic) {
		// delete sub elements
		for (final NluTrainingSentence its : getNluTrainingSentenceByTopic(bot, topic)) {
			nluTrainingSentenceDAO.delete(its.getUID());
		}
		topicDAO.delete(topic.getTopId());
		nodeServices.updateNodes(bot);
	}

	public void deleteCompleteTopic(@SecuredOperation("botContributor") final Chatbot bot, final Topic topic) {
		for (final TopicInterfaceServices<? extends Entity> services : topicInterfaceServices) {
			services.deleteIfExists(bot, topic);
		}
		deleteTopic(bot, topic);
	}

	public DtList<Topic> getAllTopicByBot(@SecuredOperation("botVisitor") final Chatbot bot) {
		return topicDAO.findAll(Criterions.isEqualTo(TopicFields.botId, bot.getBotId()), DtListState.of(1000));
	}

	public DtList<Topic> getAllTopicByBotId(final Long botId) {
		return topicDAO.findAll(Criterions.isEqualTo(TopicFields.botId, botId), DtListState.of(1000));
	}

	public DtList<Topic> getAllTopicByBotTtoCd(@SecuredOperation("botVisitor") final Chatbot bot, final String ttoCd) {
		return topicDAO.findAll(Criterions.isEqualTo(TopicFields.botId, bot.getBotId()).and(Criterions.isEqualTo(TopicFields.ttoCd, ttoCd)), DtListState.of(1000));
	}

	public DtList<Topic> getAllNonTechnicalTopicAndActiveByBotTtoCd(@SecuredOperation("botVisitor") final Chatbot bot, final String ttoCd) {
		return topicDAO.findAll(
				Criterions.isEqualTo(TopicFields.botId, bot.getBotId())
						.and(Criterions.isEqualTo(TopicFields.isEnabled, true))
						.and(Criterions.isEqualTo(TopicFields.ttoCd, ttoCd)
								.and(Criterions.isEqualTo(TopicFields.ktoCd, KindTopicEnum.NORMAL.name()))),
				DtListState.of(1000));
	}

	public DtList<Topic> getAllTopicEnableByBot(@SecuredOperation("botVisitor") final Chatbot bot) {
		return topicDAO.findAll(Criterions.isEqualTo(TopicFields.botId, bot.getBotId()).and(Criterions.isEqualTo(TopicFields.isEnabled, true)), DtListState.of(1000));
	}

	public void removeAllTopicsFromBot(@SecuredOperation("botAdm") final Chatbot bot) {
		topicPAO.removeAllTopicsFromBot(bot.getBotId());
	}

	public DtList<Topic> getTopicFromTopicCategory(final TopicCategory category) {
		return topicDAO.getAllTopicFromCategory(category.getTopCatId());
	}

	public DtList<Topic> getAllTopicRelativeSmallTalkByBot(final Chatbot bot) {
		return topicDAO.getAllTopicRelativeSmallTalkByBotId(bot.getBotId());
	}

	public DtList<Topic> getAllTopicRelativeScriptIntentionByBot(final Chatbot bot) {
		return topicDAO.getAllTopicRelativeScriptIntentByBotId(bot.getBotId());
	}

	public Topic getBasicTopicByBotIdKtoCd(final Long botId, final String ktoCd) {
		return topicDAO.getBasicTopicByBotIdKtoCd(botId, ktoCd);
	}

	public DtList<TopicIhm> getAllNonTechnicalTopicIhmByBot(@SecuredOperation("botVisitor") final Chatbot bot, final String locale) {
		return topicPAO.getAllTopicsIhmFromBot(bot.getBotId(), Optional.of(KindTopicEnum.NORMAL.name()), locale);
	}

	public DtList<Topic> getTopicReferencingTopId(final Long topId) {
		return topicDAO.getTopicReferencingTopId(topId);
	}

	public Topic initNewBasicTopic(final String ktoCd) {
		final Topic topic = new Topic();
		final KindTopic kto = kindTopicServices.findKindTopicByCd(ktoCd);
		topic.setIsEnabled(true);
		topic.setTitle(kto.getLabel());
		topic.setTtoCd(TypeTopicEnum.SMALLTALK.name());
		topic.setKtoCd(ktoCd);
		topic.setDescription(kto.getDescription());
		topic.setCode(ktoCd);
		return topic;

	}

	public void saveBotTopic(final Chatbot chatbot, final TopicCategory topicCategory, final String ktoCd, final BotPredefinedTopic botTopic) {
		final Topic topic;
		if (botTopic.getTopId() == null) {
			topic = initNewBasicTopic(ktoCd);
		} else {
			topic = topicDAO.get(botTopic.getTopId());
		}
		topic.setBotId(chatbot.getBotId());
		topic.setTtoCd(botTopic.getTtoCd());
		topic.setTopCatId(topicCategory.getTopCatId());
		//Saving the topic is executed after, because a null response is needed if the topic has no topId yet
		topicDAO.save(topic);

		for (final TopicInterfaceServices<? extends Entity> service : topicInterfaceServices) {
			if (service.handleObject(topic)) {
				service.createOrUpdateFromTopic(chatbot, topic, botTopic.getValue());
			} else {
				service.deleteIfExists(chatbot, topic);
			}
		}

	}

	public Topic saveTtoCd(final Topic topic, final String ttoCd, final Chatbot bot) {
		topic.setTtoCd(ttoCd);
		return doSave(topic, bot);
	}

	public UtterText initUtterTextBasicTopic(final Topic topic) {
		return new UtterText();
	}

	//********* NTS part ********/

	public DtList<NluTrainingSentence> getNluTrainingSentenceByTopic(@SecuredOperation("botVisitor") final Chatbot bot, final Topic topic) {
		Assertion.check()
				.isNotNull(topic.getTopId());
		// ---

		return nluTrainingSentenceDAO.findAll(
				Criterions.isEqualTo(NluTrainingSentenceFields.topId, topic.getTopId()),
				DtListState.of(1000, 0, NluTrainingSentenceFields.ntsId.name(), false));
	}

	public void removeNTS(final DtList<NluTrainingSentence> nluTrainingSentencesToDelete) {
		nluTrainingSentencesToDelete.stream()
				.filter(itt -> itt.getNtsId() != null)
				.forEach(itt -> nluTrainingSentenceDAO.delete(itt.getNtsId()));
	}

	protected DtList<NluTrainingSentence> saveAllNotBlankNTS(final Topic topic, final DtList<NluTrainingSentence> nluTrainingSentences) {
		// save nlu textes
		final DtList<NluTrainingSentence> ntsToSave = nluTrainingSentences.stream()
				.filter(nts -> !StringUtil.isBlank(nts.getText()))
				.collect(VCollectors.toDtList(NluTrainingSentence.class));

		for (final NluTrainingSentence nts : ntsToSave) {
			if (TopicsUtils.checkSpecialCharacters(nts.getText())) {
				throw new VUserException("The responses cannot contain the following characters : '[', ']', '|', '¤'. ");
			}
			nts.setTopId(topic.getTopId());
			nluTrainingSentenceDAO.save(nts);

		}

		return ntsToSave;
	}

	public void removeAllNTSFromBot(final Chatbot bot) {
		topicPAO.removeAllNluTrainingSentenceByBotId(bot.getBotId());
	}

	@Override
	public void start() {
		topicInterfaceServices.add(scriptIntentionServices);
		topicInterfaceServices.add(smallTalkServices);
	}

	@Override
	public void stop() {
		//nothing
	}

	public Optional<Topic> getTopicByCode(final String code, final Long botId) {
		final Criteria<Topic> criteria = Criterions.isEqualTo(TopicFields.code, code).and(Criterions.isEqualTo(TopicFields.botId, botId));
		return topicDAO.findOptional(criteria);
	}
}
