package pe.mayciel.freechal.service;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.nhncorp.fos.utils.DateFormatUtil;
import com.nhncorp.fos.utils.HttpConnectionUtil;

import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.FreechalArtcl;

@Service
public class ContentCollectionService {
	/**
	 * 해당 페이지의 답글을 제외한 게시글의 docId 목록을 반환한다.
	 * 
	 * @param info
	 * @param page
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public List<Integer> getDocIdListByPage(ContentInfo info, int page)
			throws SocketTimeoutException, Exception {
		List<Integer> result = new ArrayList<Integer>();
		String html = HttpConnectionUtil.getText(getListUrl(info, page));

		// BBS type
		int start = html.indexOf("<div id=\"BoardTdList\">");
		start = html.indexOf("<table>", start);
		String tableCont = html.substring(start,
				html.indexOf("</table>", start));
		String idPattern = "<td class=\"td_title\"><a ([^>]*)DocId=([^\"]+)\"([^>]*)>";
		Pattern pt = Pattern.compile(idPattern, Pattern.CASE_INSENSITIVE);
		Matcher mc = pt.matcher(tableCont);
		while (mc.find()) {
			result.add(Integer.parseInt(mc.group(2)));
		}
		return result;
	}

	public FreechalArtcl getArtclData(ContentInfo info) throws Exception {
		FreechalArtcl artcl = new FreechalArtcl();
		artcl.setDocId(info.getDocId());
		String html = HttpConnectionUtil.getText(getArtclUrl(info));
		int start = 0;

		start = html.indexOf("<td class=\"td_title\">", start);
		start = html.indexOf(">", start) + 1;
		artcl.setTitle(html.substring(start, html.indexOf("</td>", start))
				.trim());

		start = html.indexOf("<td class=\"td_date\">", start);
		start = html.indexOf(">", start) + 1;
		String dateStr = html.substring(start, html.indexOf("</td>", start));
		artcl.setDate(DateFormatUtil
				.strToDate(dateStr, "yyyy-MM-dd a hh:mm:ss"));

		start = html.indexOf("<span", start);
		start = html.indexOf(">", start) + 1;
		artcl.setWriterNm(html.substring(start, html.indexOf("</span>", start)));

		start = html.indexOf("<div id=\"td_hit\">", start);
		start = html.indexOf("<span", start);
		start = html.indexOf(">", start) + 1;
		artcl.setReadCnt(Integer.parseInt(html.substring(start,
				html.indexOf("</span>", start))));

		start = html.indexOf("<div id=\"content\"", start);
		start = html.indexOf(">", start) + 1;
		artcl.setBdyCont(html.substring(start, html.indexOf("</div>", start)));
		return artcl;
	}

	/**
	 * 게시판 목록 url 을 반환한다.
	 * 
	 * @param info
	 * @param page
	 * @return
	 */
	private String getListUrl(ContentInfo info, int page) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://community.freechal.com/ComService/Activity/")
				.append(info.getType().getListUrl()).append("?GrpId=")
				.append(info.getGrpId()).append("&ObjSeq=")
				.append(info.getObjSeq()).append("&PageNo=").append(page);
		return sb.toString();
	}

	/**
	 * 게시글 url 을 반환한다.
	 * 
	 * @param info
	 * @param page
	 * @return
	 */
	private String getArtclUrl(ContentInfo info) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://community.freechal.com/ComService/Activity/")
				.append(info.getType().getArtclUrl()).append("?GrpId=")
				.append(info.getGrpId()).append("&ObjSeq=")
				.append(info.getObjSeq()).append("&DocId=")
				.append(info.getDocId());
		return sb.toString();
	}
}