package com.skj.mongopractice;

import com.mongodb.client.MongoClient;
import com.skj.mongopractice.domain.Simple;
import com.skj.mongopractice.service.SimpleServiceImpl;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 0.0.1
 * @implSpec Service, Repository Layer 통합 테스팅
 * @author RealShinyHand
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
	@Order(1)
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
	@Order(2)
	void initalDBrecord(){
		simpleService.deleteAll();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		for(int i = 1 ; i <= RECORD_COUNT ; i++ ){
			simpleService.insertOne(Simple.builder().title(String.valueOf(i)).body(i+"body").date(LocalDate.parse("2000-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusMonths(i-1)).build());
		}

	}





	@Test
	@DisplayName("1.[SELECT-기초] 존재하는 모든 Simple 조회 : simples collection 하위 모든 도큐먼트 조회")
	@Order(3)
	void givenEmptyWhenFindAllThenSuccess(){
		log.info("[SELECT-기초] 존재하는 모든 Simple 조회 : simples collection 하위 모든 도큐먼트 조회");
		//given
		//when
		List<Simple> simpleList = simpleService.findAll();
		//then
		Assertions.assertEquals(RECORD_COUNT,simpleList.size());
	}

	@Test
	@Order(4)
	@DisplayName("2-1.[SELECT-기초] title이 일치하는 Simple 한건 조회 : 성공 시나리오")
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
	@Order(5)
	@DisplayName("2-2.[SELECT-기초] title이 일치하는 Simple 한건 조회 : 실패 시나리오")
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
	@Order(6)
	@DisplayName("3.[SELECT-기초] 주어진 title 포함된  도큐머트 반환")
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
	@Order(7)
	@DisplayName("4.[SELECT-기초] 주어진 date 보다 큰 도큐먼트만 반환 & date 내림차순 정렬")
	void givenWhenFindWhereDateGtOrderByDateDescThenSuccess(){
		//given
		LocalDate date = LocalDate.of(2000,1,1);
		//when
		List<Simple> simpleList = simpleService.findByDateMoreThan(date);

		//then
		Assertions.assertEquals(99,simpleList.size());
		for(int i = 0 ; i < simpleList.size() -1;i++){
			boolean result = simpleList.get(i).getDate().isAfter(simpleList.get(i+1).getDate());
			Assertions.assertEquals(true,result);
		}
	}


	@Test
	@DisplayName("5.전체 갯수 조회")
	@Order(8)
	void givenWhenGetCountThenSuccess(){
		int count = simpleService.getToalCount();

		Assertions.assertEquals(RECORD_COUNT,count);
	}

	@Test
	@DisplayName("6. 전체 타이틀 조회 (단, title,_id을 조회하시오..)")
	@Order(9)
	void givenWhenGetTitlesThenReturnTitles(){
		List<Simple> simpleList = simpleService.getTitles();

		for(Simple e : simpleList){
			Assertions.assertNull(e.getDate());
			Assertions.assertNull(e.getId());
			Assertions.assertNull(e.getBody());
			Assertions.assertNotNull(e.getTitle());
		}

	}

	@Test
	@DisplayName("7. date가 가장 큰 top 3개만 조회")
	@Order(10)
	void givenWhenfindLimitTop3ThenReturn3Record(){
		List<Simple> simpleList = simpleService.findTop3();
		Assertions.assertEquals(3,simpleList.size());

		Assertions.assertEquals(simpleList.get(0).getDate(),LocalDate.of(2008,4,1));
		Assertions.assertEquals(simpleList.get(1).getDate(),LocalDate.of(2008,3,1));
		Assertions.assertEquals(simpleList.get(2).getDate(),LocalDate.of(2008,2,1));
	}
	
	@Test
	@Order(11)
	@DisplayName("8.페이징 처리, 페이지당 아이템 갯수 = 10개로 하여, 1~n 까지 페이징 처리하라,정렬은 날짜 내림차순 ")
	void givenPagingIndex_whenFindWithPaging_thenReturnList(){

		List<Simple> simpleList;
		Assertions.assertThrows(IllegalArgumentException.class,() -> simpleService.findWithPaging(-1));
		for(int i = 0 ; i < 10;i++){
			simpleList = simpleService.findWithPaging(i);
			Assertions.assertEquals(10,simpleList.size());
		}
		simpleList = simpleService.findWithPaging(0);
		Assertions.assertEquals(true,simpleList.get(0).getDate().isAfter(simpleList.get(1).getDate()));
		Assertions.assertEquals(true,
				simpleList.get(simpleList.size()-1)
						.getDate().isAfter(simpleService.findWithPaging(1).get(0).getDate()));
		Assertions.assertEquals(simpleService.findWithPaging(10).size(),0);
	}


}
