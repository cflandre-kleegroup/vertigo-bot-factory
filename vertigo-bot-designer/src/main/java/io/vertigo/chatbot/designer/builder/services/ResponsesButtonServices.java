package io.vertigo.chatbot.designer.builder.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.vertigo.account.authorization.annotations.Secured;
import io.vertigo.chatbot.commons.dao.ResponseButtonDAO;
import io.vertigo.chatbot.commons.domain.Chatbot;
import io.vertigo.chatbot.commons.domain.ResponseButton;
import io.vertigo.chatbot.commons.domain.SmallTalk;
import io.vertigo.chatbot.designer.builder.BuilderPAO;
import io.vertigo.chatbot.domain.DtDefinitions.ResponseButtonFields;
import io.vertigo.commons.transaction.Transactional;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.component.Component;
import io.vertigo.datamodel.criteria.Criterions;
import io.vertigo.datamodel.structure.model.DtList;
import io.vertigo.datamodel.structure.model.DtListState;
import io.vertigo.datamodel.structure.util.VCollectors;

@Transactional
@Secured("BotUser")
public class ResponsesButtonServices implements Component {

	@Inject
	private ResponseButtonDAO responseButtonDAO;

	@Inject
	private BuilderPAO builderPAO;

	public DtList<ResponseButton> getResponsesButtonList(final SmallTalk smallTalk) {
		Assertion.check()
				.isNotNull(smallTalk)
				.isNotNull(smallTalk.getSmtId());
		return responseButtonDAO.findAll(
				Criterions.isEqualTo(ResponseButtonFields.smtId, smallTalk.getSmtId()),
				DtListState.of(1000, 0, ResponseButtonFields.btnId.name(), false));
	}

	public DtList<ResponseButton> getWelcomeButtonsByBot(final Chatbot bot) {
		Assertion.check()
				.isNotNull(bot)
				.isNotNull(bot.getBotId());
		// ---

		return responseButtonDAO.findAll(
				Criterions.isEqualTo(ResponseButtonFields.botIdWelcome, bot.getBotId()),
				DtListState.of(1000, 0, ResponseButtonFields.btnId.name(), false));
	}

	public DtList<ResponseButton> getDefaultButtonsByBot(final Chatbot bot) {
		Assertion.check()
				.isNotNull(bot)
				.isNotNull(bot.getBotId());
		// ---

		return responseButtonDAO.findAll(
				Criterions.isEqualTo(ResponseButtonFields.botIdDefault, bot.getBotId()),
				DtListState.of(1000, 0, ResponseButtonFields.btnId.name(), false));
	}

	public DtList<ResponseButton> getButtonsBySmalltalk(final SmallTalk smallTalk) {
		Assertion.check()
				.isNotNull(smallTalk)
				.isNotNull(smallTalk.getSmtId());
		// ---

		return responseButtonDAO.findAll(
				Criterions.isEqualTo(ResponseButtonFields.smtId, smallTalk.getSmtId()),
				DtListState.of(1000, 0, ResponseButtonFields.btnId.name(), false));
	}

	public void removeAllButtonsBySmtId(final SmallTalk smt) {
		// clear old buttons
		builderPAO.removeAllButtonsBySmtId(smt.getSmtId());

	}

	public void removeAllButtonsByBot(final Chatbot bot) {
		// clear old buttons
		builderPAO.removeAllButtonsByBotId(bot.getBotId());

	}

	public void saveAllButtonsBySmtId(final SmallTalk savedST, final DtList<ResponseButton> buttonList) {
		// save new buttons
		for (final ResponseButton btn : buttonList) {
			btn.setBtnId(null); // force creation
			btn.setSmtId(savedST.getSmtId());
			responseButtonDAO.save(btn);
		}

	}

	public void deleteResponsesButtonsBySmallTalk(final SmallTalk smallTalk) {

		for (final ResponseButton button : getResponsesButtonList(smallTalk)) {
			responseButtonDAO.delete(button.getUID());
		}
	}

	public Map<Long, DtList<ResponseButton>> exportSmallTalkRelativeButtons(final List<Long> smallTalkIds) {
		return responseButtonDAO.exportSmallTalkRelativeButtons(smallTalkIds)
				.stream()
				.collect(Collectors.groupingBy(ResponseButton::getSmtId,
						VCollectors.toDtList(ResponseButton.class)));
	}

	public void saveAllDefaultButtonsByBot(final Chatbot bot, final DtList<ResponseButton> buttonList) {
		// save new buttons
		final Long botId = bot.getBotId();
		for (final ResponseButton btn : buttonList) {
			btn.setBtnId(null); // force creation
			btn.setBotIdDefault(botId);
			responseButtonDAO.save(btn);
		}

	}

	public void saveAllWelcomeButtonsByBot(final Chatbot bot, final DtList<ResponseButton> buttonList) {
		// save new buttons
		final Long botId = bot.getBotId();
		for (final ResponseButton btn : buttonList) {
			btn.setBtnId(null); // force creation
			btn.setBotIdWelcome(botId);
			responseButtonDAO.save(btn);
		}

	}

}
