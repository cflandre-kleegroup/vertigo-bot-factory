package io.vertigo.chatbot.commons.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ChatbotCustomConfig implements Entity {
	private static final long serialVersionUID = 1L;

	private Long cccId;
	private String botEmailAddress;
	private Boolean reinitializationButton;
	private String backgroundColor;
	private String fontColor;
	private String fontFamily;
	private Boolean displayAvatar;
	private Long totalMaxAttachmentSize;
	private Boolean disableNlu;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AChatbotCustomConfigChatbot",
			fkFieldName = "botId",
			primaryDtDefinitionName = "DtChatbot",
			primaryIsNavigable = true,
			primaryRole = "Chatbot",
			primaryLabel = "Chatbot",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtChatbotCustomConfig",
			foreignIsNavigable = false,
			foreignRole = "ChatbotCustomConfig",
			foreignLabel = "ChatbotCustomConfig",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<io.vertigo.chatbot.commons.domain.Chatbot> botIdAccessor = new StoreVAccessor<>(io.vertigo.chatbot.commons.domain.Chatbot.class, "Chatbot");

	/** {@inheritDoc} */
	@Override
	public UID<ChatbotCustomConfig> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Context value id'.
	 * @return Long cccId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Context value id")
	public Long getCccId() {
		return cccId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Context value id'.
	 * @param cccId Long <b>Obligatoire</b>
	 */
	public void setCccId(final Long cccId) {
		this.cccId = cccId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Bot email address'.
	 * @return String botEmailAddress
	 */
	@Field(smartType = "STyLabel", label = "Bot email address")
	public String getBotEmailAddress() {
		return botEmailAddress;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Bot email address'.
	 * @param botEmailAddress String
	 */
	public void setBotEmailAddress(final String botEmailAddress) {
		this.botEmailAddress = botEmailAddress;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Reinitialization button'.
	 * @return Boolean reinitializationButton
	 */
	@Field(smartType = "STyYesNo", label = "Reinitialization button")
	public Boolean getReinitializationButton() {
		return reinitializationButton;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Reinitialization button'.
	 * @param reinitializationButton Boolean
	 */
	public void setReinitializationButton(final Boolean reinitializationButton) {
		this.reinitializationButton = reinitializationButton;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Bot background color'.
	 * @return String backgroundColor
	 */
	@Field(smartType = "STyLabel", label = "Bot background color")
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Bot background color'.
	 * @param backgroundColor String
	 */
	public void setBackgroundColor(final String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Bot font color'.
	 * @return String fontColor
	 */
	@Field(smartType = "STyLabel", label = "Bot font color")
	public String getFontColor() {
		return fontColor;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Bot font color'.
	 * @param fontColor String
	 */
	public void setFontColor(final String fontColor) {
		this.fontColor = fontColor;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Bot font family'.
	 * @return String fontFamily
	 */
	@Field(smartType = "STyLabel", label = "Bot font family")
	public String getFontFamily() {
		return fontFamily;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Bot font family'.
	 * @param fontFamily String
	 */
	public void setFontFamily(final String fontFamily) {
		this.fontFamily = fontFamily;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Display avatar'.
	 * @return Boolean displayAvatar
	 */
	@Field(smartType = "STyYesNo", label = "Display avatar")
	public Boolean getDisplayAvatar() {
		return displayAvatar;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Display avatar'.
	 * @param displayAvatar Boolean
	 */
	public void setDisplayAvatar(final Boolean displayAvatar) {
		this.displayAvatar = displayAvatar;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Total maximum attachment size'.
	 * @return Long totalMaxAttachmentSize
	 */
	@Field(smartType = "STyNumber", label = "Total maximum attachment size")
	public Long getTotalMaxAttachmentSize() {
		return totalMaxAttachmentSize;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Total maximum attachment size'.
	 * @param totalMaxAttachmentSize Long
	 */
	public void setTotalMaxAttachmentSize(final Long totalMaxAttachmentSize) {
		this.totalMaxAttachmentSize = totalMaxAttachmentSize;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Disable NlU'.
	 * @return Boolean disableNlu
	 */
	@Field(smartType = "STyYesNo", label = "Disable NlU")
	public Boolean getDisableNlu() {
		return disableNlu;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Disable NlU'.
	 * @param disableNlu Boolean
	 */
	public void setDisableNlu(final Boolean disableNlu) {
		this.disableNlu = disableNlu;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Chatbot'.
	 * @return Long botId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyId", label = "Chatbot", fkDefinition = "DtChatbot", cardinality = io.vertigo.core.lang.Cardinality.ONE )
	public Long getBotId() {
		return (Long) botIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Chatbot'.
	 * @param botId Long <b>Obligatoire</b>
	 */
	public void setBotId(final Long botId) {
		botIdAccessor.setId(botId);
	}

 	/**
	 * Association : Chatbot.
	 * @return l'accesseur vers la propriété 'Chatbot'
	 */
	public StoreVAccessor<io.vertigo.chatbot.commons.domain.Chatbot> chatbot() {
		return botIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
