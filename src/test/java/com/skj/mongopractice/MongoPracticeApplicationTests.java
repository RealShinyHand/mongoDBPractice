package com.skj.mongopractice;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.skj.mongopractice.dto.ProfessorDto;
import com.skj.mongopractice.service.ProfessorService;
import com.skj.mongopractice.service.StudentService;
import com.skj.mongopractice.service.SubjectService;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * @version 0.0.1
 * @implSpec Service, Repository Layer 통합 테스팅
 * @author RealShinyHand
 */
@SpringBootTest
class MongoPracticeApplicationTests {

	Logger log = LoggerFactory.getLogger(MongoPracticeApplicationTests.class);

	@Autowired
	ProfessorService professorService;

	@Autowired
	StudentService studentService;

	@Autowired
	SubjectService subjectService;

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
		long count = mongoTemplate.getCollectionNames().stream().count();
		if(count == 0){
			log.info("practice DB가 초기화 되지 않았거나 Collection 이 없음으로 생성합니다.");
			mongoTemplate.createCollection("professors");
			mongoTemplate.createCollection("students");
			mongoTemplate.createCollection("subjects");
		}else{
			log.info("practiceDB, Collection 확인 끝");
		}

		log.info("현재 DB = {}",mongoTemplate.getDb().getName());
		log.info("에플리케이션 정상 동작 - 테스트 시작");
	}




	@Test
	@DisplayName("[SELECT-기초]존재하는 모든 professor를 조회")
	void givenEmptyWhenSelectProfessorThenSuccess(){
		//given
		//when
		List<ProfessorDto> professorDtoList =  professorService.findAll();
		//then
	}

}
