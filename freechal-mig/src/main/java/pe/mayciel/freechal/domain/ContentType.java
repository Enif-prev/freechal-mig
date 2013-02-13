package pe.mayciel.freechal.domain;

public enum ContentType {
	/** 게시판 */
	BBS("BBS/CsBBSList.asp", "BBS/CsBBSContent.asp"),
	/** 자료실 */
	PDS("PDS/CsPDSList.asp", ""),
	/** 앨범 */
	ALB("Album/CsPhotoList.asp", "");

	private String listUrl;
	private String artclUrl;

	ContentType(String listUrl, String artclUrl) {
		this.listUrl = listUrl;
		this.artclUrl = artclUrl;
	}

	public String getListUrl() {
		return listUrl;
	}

	public String getArtclUrl() {
		return artclUrl;
	}
}