package org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.filterConditionSpecifics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.csstudio.nams.common.fachwert.Millisekunden;
import org.csstudio.nams.common.material.regelwerk.Operator;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.FilterConditionDTO;

/**
 * Dieses Daten-Transfer-Objekt stellt hält die Konfiguration einer
 * AMS_FilterCond_TimeBasedItems.
 * 
 * Das Create-Statement für die Datenbank hat folgendes Aussehen:
 * 
 * <pre>
 * create table AMS_FilterCond_TimeBased
 * 
 * iFilterConditionRef	INT NOT NULL,
 * cStartKeyValue		VARCHAR(16),
 * sStartOperator		SMALLINT,
 * cStartCompValue		VARCHAR(128),
 * cConfirmKeyValue	VARCHAR(16),
 * sConfirmOperator	SMALLINT,
 * cConfirmCompValue	VARCHAR(128),
 * sTimePeriod			SMALLINT,
 * sTimeBehavior		SMALLINT
 * ;
 * </pre>
 */
@Entity
@Table(name = "AMS_FilterCond_TimeBased")
@PrimaryKeyJoinColumn(name = "iFilterConditionRef")
public class TimeBasedFilterConditionDTO extends FilterConditionDTO {

	public String getCStartKeyValue() {
		return cStartKeyValue;
	}
	public void setCStartKeyValue(String startKeyValue) {
		cStartKeyValue = startKeyValue;
	}
	public String getCStartCompValue() {
		return cStartCompValue;
	}
	public void setCStartCompValue(String startCompValue) {
		cStartCompValue = startCompValue;
	}
	public String getCConfirmKeyValue() {
		return cConfirmKeyValue;
	}
	public void setCConfirmKeyValue(String confirmKeyValue) {
		cConfirmKeyValue = confirmKeyValue;
	}
	public String getCConfirmCompValue() {
		return cConfirmCompValue;
	}
	public void setCConfirmCompValue(String confirmCompValue) {
		cConfirmCompValue = confirmCompValue;
	}
	
	public short getSTimeBehavior() {
		return sTimeBehavior;
	}
	public void setSTimeBehavior(short timeBehavior) {
		sTimeBehavior = timeBehavior;
	}
	public Millisekunden getTimePeriod() {
		return Millisekunden.valueOf(sTimePeriod * 1000);
	}
	public void setTimePeriod(Millisekunden millisekunden) {
		sTimePeriod = (short) (millisekunden.alsLongVonMillisekunden() / 1000);
	}
	public Operator getTBStartOperator () {
		return Operator.findOperatorOfDBId(sStartOperator);
	}
	public void setTBStartOperator (Operator operator) {
		sStartOperator = operator.asDatabaseId();
	}
	public Operator getTBConfirmOperator () {
		return Operator.findOperatorOfDBId(sConfirmOperator);
	}
	public void setTBConfirmOperator (Operator operator) {
		sConfirmOperator = operator.asDatabaseId();
	}
	
	@SuppressWarnings("unused")
	private int getIFilterConditionRef() {
		return iFilterConditionRef;
	}
	@SuppressWarnings("unused")
	private void setIFilterConditionRef(int filterConditionRef) {
		iFilterConditionRef = filterConditionRef;
	}
	@SuppressWarnings("unused")
	private short getSStartOperator() {
		return sStartOperator;
	}
	@SuppressWarnings("unused")
	private void setSStartOperator(short startOperator) {
		sStartOperator = startOperator;
	}
	@SuppressWarnings("unused")
	private short getSConfirmOperator() {
		return sConfirmOperator;
	}
	@SuppressWarnings("unused")
	private void setSConfirmOperator(short confirmOperator) {
		sConfirmOperator = confirmOperator;
	}
	@SuppressWarnings("unused")
	private short getSTimePeriod() {
		return sTimePeriod;
	}
	@SuppressWarnings("unused")
	private void setSTimePeriod(short timePeriod) {
		sTimePeriod = timePeriod;
	}
	
	@Column(name = "iFilterConditionRef", nullable = false, updatable = false, insertable = false)
	private int iFilterConditionRef;
	@Column(name = "cStartKeyValue")
	private String cStartKeyValue;
	@Column(name = "sStartOperator")
	private short sStartOperator;
	@Column(name = "cStartCompValue")
	private String cStartCompValue;
	
	@Column(name = "cConfirmKeyValue")
	private String cConfirmKeyValue;
	@Column(name = "sConfirmOperator")
	private short sConfirmOperator;
	@Column(name = "cConfirmCompValue")
	private String cConfirmCompValue;
	
	@Column(name="sTimePeriod")
	private short sTimePeriod;
	@Column(name="sTimeBehavior")
	private short sTimeBehavior;
}

