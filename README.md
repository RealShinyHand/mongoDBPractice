# mongoDBPractice
몽고 db 를 연습할 수 있게 테스트케이스를 만들면 다른 사람들이 사용하지 않을까?

<h2>테스트 코드 설정</h2>
<h4>@DataJpaTest</h4>
<div>
- JPA 관련된 설정만 로드한다. (WebMVC와 관련된 Bean이나 기능은 로드되지 않는다)
<br/>
<br/>
</div>
<div>
- JPA를 사용해서 생성/조회/수정/삭제 기능의 테스트가 가능하다.
<br/>
<br/>
</div>
<div>
- @Transactional을 기본적으로 내장하고 있으므로, 매 테스트 코드가 종료되면 자동으로 DB가 롤백된다.
기본적으로 내장 DB를 사용하는데, 설정을 통해 실제 DB로 테스트도 가능하다. (권장하지 않는다)
<br/>
<br/>
</div>
<div>
- @Entity가 선언된 클래스를 스캔하여 저장소를 구성한다.
<br/>
<br/>
</div>
<i>출처 : https://jiminidaddy.github.io/dev/2021/05/20/dev-spring-%EB%8B%A8%EC%9C%84%ED%85%8C%EC%8A%A4%ED%8A%B8-Repository/ </i>