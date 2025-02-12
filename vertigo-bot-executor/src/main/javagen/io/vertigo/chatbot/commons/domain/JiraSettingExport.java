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
public final class JiraSettingExport implements DtObject {
	private static final long serialVersionUID = 1L;

	private String url;
	private String login;
	private String password;
	private String project;
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Jira URL'.
	 * @return String url <b>Obligatoire</b>
	 */
	@Field(smartType = "STyUrl", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Jira URL")
	public String getUrl() {
		return url;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Jira URL'.
	 * @param url String <b>Obligatoire</b>
	 */
	public void setUrl(final String url) {
		this.url = url;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Login'.
	 * @return String login <b>Obligatoire</b>
	 */
	@Field(smartType = "STyLabel", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Login")
	public String getLogin() {
		return login;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Login'.
	 * @param login String <b>Obligatoire</b>
	 */
	public void setLogin(final String login) {
		this.login = login;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Password'.
	 * @return String password <b>Obligatoire</b>
	 */
	@Field(smartType = "STyPassword", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Password")
	public String getPassword() {
		return password;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Password'.
	 * @param password String <b>Obligatoire</b>
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Project'.
	 * @return String project <b>Obligatoire</b>
	 */
	@Field(smartType = "STyLabel", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Project")
	public String getProject() {
		return project;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Project'.
	 * @param project String <b>Obligatoire</b>
	 */
	public void setProject(final String project) {
		this.project = project;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
