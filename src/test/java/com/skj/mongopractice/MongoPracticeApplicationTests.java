package com.skj.mongopractice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.skj.mongopractice.domain.Simple;
import com.skj.mongopractice.dto.ProfessorDto;
import com.skj.mongopractice.service.ProfessorService;
import com.skj.mongopractice.service.SimpleServiceImpl;
import com.skj.mongopractice.service.StudentService;
import com.skj.mongopractice.service.SubjectService;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 0.0.1
 * @implSpec Service, Repository Layer 통합 테스팅
 * @author RealShinyHand
 */
@SpringBootTest
class MongoPracticeApplicationTests {

	Logger log = LoggerFactory.getLogger(MongoPracticeApplicationTests.class);

	private final int RECORD_COUNT = 100;
	@Autowired
	SimpleServiceImpl simpleService;

	@Autowired
	MongoClient mongoClient;

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(mongoClient);
		Assertions.assertNotNull(mongoTemplate);

		log.info("mongoDB 확인");
		mongoClient.listDatabaseNames().forEach((item)->log.info("{}",item));
		log.info("mongoDB 확인 - 끝 -");


		log.info("현재 DB = {}",mongoTemplate.getDb().getName());
		log.info("에플리케이션 정상 동작 - 테스트 시작");
	}

	@Test
	void initalDBrecord(){
		simpleService.deleteAll();
		for(int i = 1 ; i <= RECORD_COUNT ; i++ ){
			simpleService.insertOne(Simple.builder().title(String.valueOf(i)).body(i+"body").date(LocalDate.parse("2000-01-01").plusMonths(i-1)).build());
		}

	}




	@Test
	@DisplayName("1.[SELECT-기초] 존재하는 모든 Simple 조회 : simples collection 하위 모든 도큐먼트 조회")
	void givenEmptyWhenFindAllThenSuccess(){
		log.info("[SELECT-기초] 존재하는 모든 Simple 조회 : simples collection 하위 모든 도큐먼트 조회");
		//given
		//when
		List<Simple> simpleList = simpleService.findAll();
		//then
		Assertions.assertEquals(RECORD_COUNT,simpleList.size());
	}

	@Test
	@DisplayName("2.[SELECT-기초] title이 일치하는 Simple 한건 조회 : 성공 시나리오")
	void givenTitleWhenFindByTitleThenSuccess(){
		log.info("[SELECT-기초] title이 일치하는 Simple 한건 조회");
		//given
		String title = "1";
		//when

		Simple simple = simpleService.findByTitle(title);
		//then
		Assertions.assertNotNull(simple);
		Assertions.assertEquals(title,simple.getTitle());
	}

	@Test
	@DisplayName("3.[SELECT-기초] title이 일치하는 Simple 한건 조회 : 실패 시나리오")
	void givenNotExistTitleWhenFindByTitleThenReturnNull(){
		log.info("[SELECT-기초] title이 일치하는 Simple 한건 조회 : 실패 시나리오");
		//given
		String title = "abcdedf";
		//when

		Simple simple = simpleService.findByTitle(title);
		//then
		Assertions.assertNull(simple);
	}

	@Test
	@DisplayName("4.[SELECT-기초] 주어진 title 포함된  도큐머트 반환")
	void givenTitleWhenFindByLikeTitleThenSuccess(){
		log.info("[SELECT-기초] 주어진 title 포함된  도큐머트 반환");
		//given
		String title = "1";
		//when
		List<Simple> simpleList = simpleService.findByLikeTitle(title);
		//then
		Assertions.assertEquals(20,simpleList.size());

		Pattern pattern = Pattern.compile("^.*1.*$");
		for(Simple e : simpleList){
			Matcher matcher = pattern.matcher(e.getTitle());
			Assertions.assertEquals(true,matcher.find());
		}
	}



	@Test
	@DisplayName("5.[SELECT-기초] 주어진 date 보다 큰 도큐먼트만 반환 & date 내림차순 정렬")
	void givenWhenFindWhereDateGtOrderByDateDescThenSuccess(){


	}





}
