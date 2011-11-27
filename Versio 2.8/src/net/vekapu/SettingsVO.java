///////////////////////////////////////////////////////////////////////////////
//
// Filename: SettingsVO.java
//
// Author:   Janne Ilonen
// Project:  Vekapu
//
// Purpose:  Transfer/Value Object for Vekapu settings.
//
// (c) Copyright J.Ilonen, 2006 =>
//
// $Id$
//
///////////////////////////////////////////////////////////////////////////////
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details at gnu.org.
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Transfer/Value Object for Vekapu settings.
 */
package net.vekapu;

import net.vekapu.util.Constant;

/**
 * Transfer/Value Object for Vekapu settings.
 * 
 * @author janne
 *
 */
public class SettingsVO {

	private Boolean console = Boolean.FALSE;
	private Boolean cronJob  = Boolean.FALSE;
	private Boolean email  = Boolean.FALSE;
	private Boolean manual  = Boolean.FALSE;
	private Boolean newPage  = Boolean.FALSE;
	private Boolean sms  = Boolean.FALSE;
	private Boolean test  = Boolean.FALSE;
	private Boolean mailAuth  = Boolean.FALSE;
	
	private String admin = "";
	private String from = "";
	private String replyAddress = "";
	private String mailServer = "";
	private String mailPassWord = "";
	private String mailPort = "";
	private Integer groupCount = new Integer(0);
	private String[] groupName = null;
	
	// Checked week, if "" then latest
	// Tarkastettava kierros => week number
	private String week = "?";
	
	private String groupInfo = "";
	private Boolean proxySet = Boolean.FALSE;
	private String proxyHost = "";
	private String proxyPort = "";
	private Boolean server = Boolean.FALSE;
	private String groupDir = "";
	
	private String correct = "";
	private String checkedRound = "";
	

	/**
	 * 
	 */
	public SettingsVO(String correct) {
		this.correct = correct;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public Boolean isConsole() {
		return console;
	}

	public void setConsole(Boolean console) {
		this.console = console;
	}

	public Boolean isCronJob() {
		return cronJob;
	}

	public void setCronJob(Boolean cronJob) {
		this.cronJob = cronJob;
	}

	public Boolean isEmail() {
		return email;
	}

	public void setEmail(Boolean email) {
		this.email = email;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}
	
	public String getGroupName(int i) {
		return groupName[i];
	}

	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public String getGroupDir() {
		return groupDir;
	}

	public void setGroupDir(String groupDir) {
		this.groupDir = groupDir;
	}

	public String getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
	}

	public String getMailServer() {
		return mailServer;
	}

	public void setMailServer(String aMailServer) {
		this.mailServer = aMailServer;
	}

	public String getMailPassWord() {
		return mailPassWord;
	}

	public void setMailPassWord(String aPassWord) {
		this.mailPassWord = aPassWord;
	}
	
	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String aPort) {
		this.mailPort = aPort;
	}
	
	public Boolean isMailAuth() {
		return mailAuth;
	}

	public void setMailAuth(Boolean aMailAuth) {
		this.mailAuth = aMailAuth;
	}
	
	public Boolean isManual() {
		return manual;
	}

	public void setManual(Boolean manual) {
		this.manual = manual;
	}

	public Boolean isNewPage() {
		return newPage;
	}

	public void setNewPage(Boolean newPage) {
		this.newPage = newPage;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public Boolean isProxySet() {
		return proxySet;
	}

	public void setProxySet(Boolean proxySet) {
		this.proxySet = proxySet;
	}

	public String getReplyAddress() {
		return replyAddress;
	}

	public void setReplyAddress(String aReplyAddress) {
		this.replyAddress = aReplyAddress;
	}

	public Boolean isServer() {
		return server;
	}

	public void setServer(Boolean server) {
		this.server = server;
	}

	public Boolean isSms() {
		return sms;
	}

	public void setSms(Boolean sms) {
		this.sms = sms;
	}

	public Boolean isTest() {
		return test;
	}

	public void setTest(Boolean test) {
		this.test = test;
	}

	/**
	 * Possible values: "" (empty string) / auto => checking latest results or 
	 * giving checked year and round in format YYYY-ROUND (integer-integer).
	 * 
	 * @return Returns the correct.
	 */
	public String getCorrect() {
		return correct.trim();
	}
	
	
	/**
	 * @return the week number WW
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}
	
	
	/**
	 * @param checkedRound the checkedRound to set
	 */
	public void setCheckedRound(String checkedRound) {
		this.checkedRound = checkedRound;
	}

	/**
	 * 
	 * @return Year-Week YYYY-WW
	 */
	public String getCheckedRound() {		
		return checkedRound ;
	}

	@Override
	public String toString() {
		String NEW_LINE = Constant.getLineSeparator();
		String ret = "SettingsVO()" + NEW_LINE +
		"isConsole()       = '" + isConsole() + "'" + NEW_LINE +
		"isCronJob()       = '" + isCronJob() + "'" + NEW_LINE +
		"isEmail()         = '" + isEmail() + "'" + NEW_LINE +
		"isManual()        = '" + isManual() + "'" + NEW_LINE +
		"isNewPage()       = '" + isNewPage() + "'" + NEW_LINE +
		"isSms()           = '" + isSms() + "'" + NEW_LINE +
		"isTest()          = '" + isTest() + "'" + NEW_LINE +
		"isMailAuth()      = '" + isMailAuth() + "'" + NEW_LINE +
		"getAdmin()        = '" + getAdmin() + "'" + NEW_LINE +
		"getFrom()         = '" + getFrom() + "'" + NEW_LINE +
		"getReplyAddress() = '" + getReplyAddress() + "'" + NEW_LINE +
		"getMailServer()   = '" + getMailServer() + "'" + NEW_LINE +
		"getPassWord()     = '" + (getMailPassWord() == "" ? "EMPTY" : "******") + "'" + NEW_LINE +
		"getPort()         = '" + getMailPort() + "'" + NEW_LINE +
		"getGroupCount()   = '" + getGroupCount() + "'" + NEW_LINE;

		for (int i = 1; i <= getGroupCount().intValue(); i++) {
			ret += "getGroupName('" + i + "') = '" + getGroupName(i) + "'" + NEW_LINE;
		}
		ret +=
		"getGroupInfo()    = '" + getGroupInfo() + "'" + NEW_LINE +
		"isProxySet()      = '" + isProxySet() + "'" + NEW_LINE +
		"getProxyHost()    = '" + getProxyHost() + "'" + NEW_LINE +
		"getProxyPort()    = '" + getProxyPort() + "'" + NEW_LINE +
		"isServer()        = '" + isServer() + "'" + NEW_LINE +
		"getGroupDir()     = '" + getGroupDir() + "'" + NEW_LINE +
		"getKierros()      = '" + getWeek() + "'" + NEW_LINE +
		"getCorrect()      = '" + getCorrect() + "'" + NEW_LINE +
		"getCheckedRound() = '" + getCheckedRound() + "'" + NEW_LINE;
		
		return ret;
	}

}
