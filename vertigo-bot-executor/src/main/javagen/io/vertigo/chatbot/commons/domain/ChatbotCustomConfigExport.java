package io.vertigo.chatbot.commons.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.DtObject;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class ChatbotCustomConfigExport implements DtObject {
	private static final long serialVersionUID = 1L;

	private String botEmailAddress;
	private Boolean reinitializationButton;
	private String backgroundColor;
	private String fontColor;
	private String fontFamily;
	private Boolean displayAvatar;
	private Long totalMaxAttachmentSize;
	private String botFormat;
	
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
	 * Récupère la valeur de la propriété 'Bot format'.
	 * @return String botFormat
	 */
	@Field(smartType = "STyLabel", label = "Bot format")
	public String getBotFormat() {
		return botFormat;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Bot format'.
	 * @param botFormat String
	 */
	public void setBotFormat(final String botFormat) {
		this.botFormat = botFormat;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
