package io.vertigo.chatbot.designer.domain;

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.stereotype.Field;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Generated;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class TopIntent implements DtObject {
	private static final long serialVersionUID = 1L;

	private Long smtId;
	private String intentRasa;
	private Long count;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Small Talk ID'.
	 * @return Long smtId
	 */
	@Field(domain = "DoId", label = "Small Talk ID")
	public Long getSmtId() {
		return smtId;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Small Talk ID'.
	 * @param smtId Long
	 */
	public void setSmtId(final Long smtId) {
		this.smtId = smtId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Rasa intent'.
	 * @return String intentRasa
	 */
	@Field(domain = "DoLabel", label = "Rasa intent")
	public String getIntentRasa() {
		return intentRasa;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Rasa intent'.
	 * @param intentRasa String
	 */
	public void setIntentRasa(final String intentRasa) {
		this.intentRasa = intentRasa;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Count'.
	 * @return Long count
	 */
	@Field(domain = "DoNumber", label = "Count")
	public Long getCount() {
		return count;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Count'.
	 * @param count Long
	 */
	public void setCount(final Long count) {
		this.count = count;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}