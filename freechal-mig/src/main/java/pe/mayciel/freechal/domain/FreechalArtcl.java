package pe.mayciel.freechal.domain;

import java.util.Date;

public class FreechalArtcl {
	private int docId;
	private String title;
	private Date date;
	private String writerNm;
	private int readCnt;
	private String bdyCont;

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWriterNm() {
		return writerNm;
	}

	public void setWriterNm(String writerNm) {
		this.writerNm = writerNm;
	}

	public int getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}

	public String getBdyCont() {
		return bdyCont;
	}

	public void setBdyCont(String bdyCont) {
		this.bdyCont = bdyCont;
	}
}