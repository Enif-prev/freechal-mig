package pe.mayciel.freechal.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.mayciel.freechal.domain.ContentInfo;
import pe.mayciel.freechal.domain.ContentType;
import pe.mayciel.freechal.domain.FreechalArtcl;

import com.nhncorp.fos.junit.AbstractTestCaseRunWithSpring;

public class ContentCollectionServiceTest extends AbstractTestCaseRunWithSpring {
	@Autowired
	private ContentCollectionService service;

	private ContentInfo info = new ContentInfo();

	@Before
	public void before() {
		info.setGrpId(406762);
		info.setType(ContentType.BBS);
		info.setObjSeq(1);
	}

	@Test
	public void getDocIdListByPage() throws Exception {
		List<Integer> list = service.getDocIdListByPage(info, 2);
		assertTrue(list.size() > 0);
	}

	@Test
	public void getArtclData() throws Exception {
		info.setDocId(128972969);
		FreechalArtcl artcl = service.getArtclData(info);
		System.out.println("ttl : " + artcl.getTitle());
		System.out.println("date : " + artcl.getDate());
		System.out.println("writer : " + artcl.getWriterNm());
		System.out.println("readCnt : " + artcl.getReadCnt());
		System.out.println("bdyCont : " + artcl.getBdyCont());
	}
}