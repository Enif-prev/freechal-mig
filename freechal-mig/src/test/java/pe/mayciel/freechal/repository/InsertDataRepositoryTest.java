package pe.mayciel.freechal.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nhncorp.fos.junit.AbstractTestCaseRunWithSpring;

public class InsertDataRepositoryTest extends AbstractTestCaseRunWithSpring {
	@Autowired
	private InsertDataRepository repository;

	@Test
	public void test() {
		System.out.println(repository.getCnt());
	}
}