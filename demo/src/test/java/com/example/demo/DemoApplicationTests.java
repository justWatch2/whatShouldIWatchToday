//package com.example.demo;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//class DemoApplicationTests {
//	@Autowired
//	private QuestionRepository questionRepository;
//	@Autowired
//	private AnswerRepository answerRepository;
//
//	@Test
//	void contextLoads() {
////		Question question1 = new Question();
////		question1.setSubject("sbb가 무엇인가요?");
////		question1.setContent("sbb에 대해서 알고 싶습니다.");
////		question1.setCreateDate(LocalDateTime.now());
////		this.questionRepository.save(question1);
////
////		Question question2 = new Question();
////		question2.setSubject("스프링부트 모델 질문입니다.");
////		question2.setContent("id는 자동으로 생성되나요?");
////		question2.setCreateDate(LocalDateTime.now());
////		this.questionRepository.save(question2);
////		List<Question> questions = this.questionRepository.findAll();
////		assertEquals(2, questions.size());
////		Question question = questions.get(0);
////		assertEquals("sbb가 무엇인가요?", question.getSubject());
////		Optional<Question> question = this.questionRepository.findById(1);
////		if (question.isPresent()) {
////			Question question1 = question.get();
////			assertEquals("sbb에 대해서 알고 싶습니다.", question1.getContent());
////		}
////		Optional<Question> question2 = Optional.ofNullable(this.questionRepository.findBySubject("스프링부트 모델 질문입니다."));
////		if (question2.isPresent()) {
////			Question question3 = question2.get();
////			assertEquals("id는 자동으로 생성되나요?", question3.getContent());
////		}
////		Optional<Question> question = questionRepository.findById(1);
////		assertTrue(question.isPresent());
////		Question q = question.get();
////		q.setSubject("수정된 제목");
////		this.questionRepository.save(q);
////		Optional<Question> question = questionRepository.findById(1);
////		assertTrue(question.isPresent());
////		Question q = question.get();
////		this.questionRepository.delete(q);
////		assertEquals(1, this.questionRepository.count());
//		Optional<Question> oq = questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		Optional<Answer> answer = answerRepository.findByQuestionId(q.getId());
//		assertTrue(answer.isPresent());
//		System.out.println(answer.get().getContent());
//	}
//
//}
