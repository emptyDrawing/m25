
package com.pub;

public class M25_Notice {

	private int indexNum;
	
	private String noticeTilte;
	private String noticeContent;

	private String noticeOwner;
	private String noticePW;
	
	private String noticeTime;
	
	
	
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}
	
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	
	public void setNoticeOwner(String noticeOwner) {
		this.noticeOwner = noticeOwner;
	}
	
	public void setNoticePW(String noticePW) {
		this.noticePW = noticePW;
	}
	
	public void setNoticeTilte(String noticeTilte) {
		this.noticeTilte = noticeTilte;
	}
	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}
	
	public int getIndexNum() {
		return indexNum;
	}
	
	public String getNoticeContent() {
		return noticeContent;
	}
	
	public String getNoticeOwner() {
		return noticeOwner;
	}
	
	public String getNoticePW() {
		return noticePW;
	}
	
	public String getNoticeTilte() {
		return noticeTilte;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

}
