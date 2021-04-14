package io.vertigo.chatbot.engine;

import static io.vertigo.ai.bt.BTNodes.condition;
import static io.vertigo.ai.bt.BTNodes.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import io.vertigo.ai.bb.BlackBoard;
import io.vertigo.ai.bb.KeyPattern;
import io.vertigo.ai.bb.KeyTemplate;
import io.vertigo.ai.bt.BTCondition;
import io.vertigo.ai.bt.BTNode;
import io.vertigo.ai.bt.BTStatus;
import io.vertigo.core.lang.Assertion;
import io.vertigo.core.util.StringUtil;

public class BotNodeProvider {
	private final BlackBoard bb;

	public BotNodeProvider(final BlackBoard blackBoard) {
		Assertion.check()
				.isNotNull(blackBoard);
		// ---
		bb = blackBoard;
	}

	public BTNode set(final String keyTemplate, final int value) {
		return () -> {
			bb.putInteger(bb.eval(KeyTemplate.of(keyTemplate)), value);
			return BTStatus.Succeeded;
		};
	}

	public BTNode copy(final String sourceKeyTemplate, final String targetKeyTemplate) {
		return () -> {
			bb.putString(bb.eval(KeyTemplate.of(targetKeyTemplate)), bb.getString(bb.eval(KeyTemplate.of(sourceKeyTemplate))));
			return BTStatus.Succeeded;
		};
	}

	public BTNode set(final String keyTemplate, final String value) {
		return () -> {
			bb.putString(bb.eval(KeyTemplate.of(keyTemplate)), value);
			return BTStatus.Succeeded;
		};
	}

	public BTNode incr(final String keyTemplate) {
		return () -> {
			bb.incr(bb.eval(KeyTemplate.of(keyTemplate)));
			return BTStatus.Succeeded;
		};
	}

	public BTNode incrBy(final String keyTemplate, final int value) {
		return () -> {
			bb.incrBy(bb.eval(KeyTemplate.of(keyTemplate)), value);
			return BTStatus.Succeeded;
		};
	}

	public BTNode decr(final String keyTemplate) {
		return () -> {
			bb.decr(bb.eval(KeyTemplate.of(keyTemplate)));
			return BTStatus.Succeeded;
		};
	}

	public BTNode append(final String keyTemplate, final String something) {
		return () -> {
			bb.append(bb.eval(KeyTemplate.of(keyTemplate)), something);
			return BTStatus.Succeeded;
		};
	}

	public BTNode remove(final String keyPattern) {
		return () -> {
			bb.delete(KeyPattern.of(keyPattern));
			return BTStatus.Succeeded;
		};
	}

	public BTNode removeAll() {
		return () -> {
			bb.delete(KeyPattern.of("*"));
			return BTStatus.Succeeded;
		};
	}

	public BTNode inputString(final String keyTemplate, final String question, final Predicate<String> validator) {
		return selector(
				fulfilled(keyTemplate),
				queryString(keyTemplate, question, validator));
	}

	public BTNode inputInteger(final String keyTemplate, final String question, final Predicate<String> validator) {
		return selector(
				fulfilledInteger(keyTemplate),
				queryInteger(keyTemplate, question, validator));
	}

	private BTNode queryString(final String keyTemplate, final String question, final Predicate<String> validator) {
		return () -> {
			bb.listPush(BotEngine.BOT_RESPONSE_PATH, bb.format(question));
			bb.putString(BotEngine.BOT_IN_PATH.subKey("/key"), keyTemplate);
			bb.putString(BotEngine.BOT_IN_PATH.subKey("/type"), "string");
			return BTStatus.Running;
		};
	}

	private BTNode queryInteger(final String keyTemplate, final String question, final Predicate<String> validator) {
		return () -> {
			bb.listPush(BotEngine.BOT_RESPONSE_PATH, bb.format(question));
			bb.putString(BotEngine.BOT_IN_PATH.subKey("/key"), keyTemplate);
			bb.putString(BotEngine.BOT_IN_PATH.subKey("/type"), "integer");
			return BTStatus.Running;
		};
	}

	public BTCondition fulfilled(final String keyTemplate) {
		return condition(() -> bb.getString(bb.eval(KeyTemplate.of(keyTemplate))) != null);
	}

	public BTCondition fulfilledInteger(final String keyTemplate) {
		return condition(() -> bb.getInteger(bb.eval(KeyTemplate.of(keyTemplate))) != null);
	}

	//2 args
	public BTNode inputString(final String keyTemplate, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return inputString(keyTemplate, question, validator);
	}

	public BTNode inputInteger(final String keyTemplate, final String question) {
		final Predicate<String> validator = t -> !StringUtil.isBlank(t);
		return inputInteger(keyTemplate, question, validator);
	}

	private static Predicate<String> buildChoices(final String choice, final String... otherChoices) {
		final List<String> choices = new ArrayList<>();
		choices.add(choice);
		choices.addAll(List.of(otherChoices));
		return t -> choices.contains(t);
	}

	//3+ args
	public BTNode inputString(final String keyTemplate, final String question, final String choice, final String... otherChoices) {
		return inputString(keyTemplate, question, buildChoices(choice, otherChoices));
	}

	public BTNode inputInteger(final String keyTemplate, final String question, final String choice, final String... otherChoices) {
		return inputInteger(keyTemplate, question, buildChoices(choice, otherChoices));
	}

	public BTNode display(final String msg) {
		return () -> {
			System.out.println(bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	public BTNode say(final String msg) {
		return () -> {
			bb.listPush(BotEngine.BOT_RESPONSE_PATH, bb.format(msg));
			return BTStatus.Succeeded;
		};
	}

	// Integer
	public BTCondition eq(final String keyTemplate, final Integer compare) {
		return condition(() -> bb.eq(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BTCondition eqIntegerByValue(final String keyTemplate, final String otherKeyTemplate) {
		return condition(() -> bb.eq(bb.eval(KeyTemplate.of(keyTemplate)), bb.getInteger(bb.eval(KeyTemplate.of(otherKeyTemplate)))));
	}

	public BTCondition gt(final String keyTemplate, final Integer compare) {
		return condition(() -> bb.gt(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BTCondition lt(final String keyTemplate, final Integer compare) {
		return condition(() -> bb.lt(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BTCondition gtByValue(final String keyTemplate, final String compare) {
		return condition(() -> bb.gt(bb.eval(KeyTemplate.of(keyTemplate)), bb.getInteger(bb.eval(KeyTemplate.of(compare)))));
	}

	public BTCondition ltByValue(final String keyTemplate, final String compare) {
		return condition(() -> bb.lt(bb.eval(KeyTemplate.of(keyTemplate)), bb.getInteger(bb.eval(KeyTemplate.of(compare)))));
	}

	//String

	public BTCondition eqByValue(final String keyTemplate, final String otherKeyTemplate) {
		return condition(() -> bb.eq(bb.eval(KeyTemplate.of(keyTemplate)), bb.getString(bb.eval(KeyTemplate.of(otherKeyTemplate)))));
	}

	public BTCondition eq(final String keyTemplate, final String compare) {
		return condition(() -> bb.eq(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BTCondition eqCaseInsensitive(final String keyTemplate, final String compare) {
		return condition(() -> bb.eqCaseInsensitive(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BTCondition startsWith(final String keyTemplate, final String compare) {
		return condition(() -> bb.startsWith(bb.eval(KeyTemplate.of(keyTemplate)), compare));
	}

	public BotSwitch doSwitch(final String keyTemplate) {
		return new BotSwitch(this, keyTemplate);
	}

	/*	public BTNode confirm(final String keyTemplate, final String confirmMsg) {
			return () -> {
				final String response = answer(confirmMsg);
				if (response.isBlank()) {
					return BTStatus.Succeeded;
				}
				bb.put(bb.format(keyTemplate), response);
				return BTStatus.Succeeded;
			};
		}*/

}
