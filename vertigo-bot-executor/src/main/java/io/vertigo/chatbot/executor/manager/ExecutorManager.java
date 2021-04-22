/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2020, Vertigo.io, team@vertigo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.chatbot.executor.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertigo.ai.command.BtCommandManager;
import io.vertigo.chatbot.commons.domain.BotExport;
import io.vertigo.chatbot.commons.domain.ExecutorConfiguration;
import io.vertigo.chatbot.commons.domain.TopicExport;
import io.vertigo.chatbot.engine.BotManager;
import io.vertigo.chatbot.engine.model.BotInput;
import io.vertigo.chatbot.engine.model.BotResponse;
import io.vertigo.chatbot.engine.model.TopicDefinition;
import io.vertigo.chatbot.executor.model.ExecutorGlobalConfig;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.node.component.Activeable;
import io.vertigo.core.node.component.Manager;

public class ExecutorManager implements Manager, Activeable {

	private static final Logger LOGGER = LogManager.getLogger(ExecutorManager.class);

	private final ExecutorConfigManager executorConfigManager;
	private final BotManager botManager;
	private final BtCommandManager btCommandManager;

	@Inject
	public ExecutorManager(
			final ExecutorConfigManager executorConfigManager,
			final BotManager botManager,
			final BtCommandManager btCommandManager) {

		Assertion.check()
				.isNotNull(executorConfigManager)
				.isNotNull(botManager)
				.isNotNull(btCommandManager);
		//--
		this.executorConfigManager = executorConfigManager;
		this.botManager = botManager;
		this.btCommandManager = btCommandManager;
	}

	@Override
	public void start() {
		doLoadModel(executorConfigManager.getConfig().getBot());
	}

	@Override
	public void stop() {
		// nothing
	}

	public void loadModel(final BotExport bot, final ExecutorConfiguration executorConfig) {
		final var globalConfig = new ExecutorGlobalConfig();
		globalConfig.setBot(bot);
		globalConfig.setExecutorConfiguration(executorConfig);

		executorConfigManager.saveConfig(globalConfig);

		doLoadModel(bot);
	}

	private void doLoadModel(final BotExport botExport) {
		if (botExport == null) {
			// nothing to load
			LOGGER.info("New runner, load a bot to start using it.");
			return;
		}

		final List<TopicDefinition> topics = new ArrayList<>();

		// TODO : start / fallback
		for (final TopicExport topic : botExport.getTopics()) {
			topics.add(TopicDefinition.of(topic.getName(), btCommandManager.parse(topic.getTopicBT()), topic.getNluTrainingSentences(), 0.6));
		}

		botManager.updateConfig(topics);
	}

	public BotResponse startNewConversation(final BotInput input) {
		Assertion.check()
				.isNull(input.getMessage(), "No message expected");
		//--
		final var newUUID = UUID.randomUUID();

		final var botEngine = botManager.createBotEngine(newUUID);
		final var botResponse = botEngine.runTick(input);

		botResponse.getMetadatas().put("sessionId", newUUID);
		return botResponse;
	}

	public BotResponse handleUserMessage(final UUID sessionId, final BotInput input) {
		Assertion.check()
				.isNotNull(sessionId, "Please provide sessionId")
				.isNotNull(input.getMessage(), "Please provide message");
		//--
		final var botEngine = botManager.createBotEngine(sessionId);

		return botEngine.runTick(input);
	}
}
