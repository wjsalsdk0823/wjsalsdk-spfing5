####20210611
정방향개발:매퍼쿼리>DAO>Service[JUnit]컨트롤러클래스>jsp
역방향개발:(클래스간이동빨라서): 정방향으로 개발한 것 검증요만 사용
관리자단 회원목록 처리 마무리(1페이징밋ㅊ 검색기능구형)


####20210610목
GTM(글로벌타임)이라서 한국시간과 맞지 않을때, 서버시간을 바꿀권한이 없을때, 아래처럼
입력시 등록일시에 +9시간을 더해서 한국시간으로 맞춥니다.
오라클일때 확인 :
SELECT TO_CHAR(systimestamp + numtodsinterval( 9, 'HOUR' ), 'YYYY-MM-DD HH24:MI.SS.FF4')  from dual;
Mysql(마리아dB)확인 :
SELECT DATE_ADD(NOW(3), INTERVAL 9 HOUR);


####20210608화
페이징에 사용되는 변수(쿼리변수+VO변수)아래
queryStartNO,queryPerPageNum,page,perPageNum,startPage,endPage
검색에 사용되는 변수(쿼리변수만): 검색어(search_keyword),검색조건(search_type)

```
--SQL쿼리 페이징을 구현해서 변수로 삼을것을 정의
--PageVO의 멤버변수로 사용예정
SELECT TableB.*FROM
(
    SELECT ROWNUM  AS RNUM, TableA.* FROM 
    (
        SELECT * FROM tbl_member
        WHERE user_id LIKE '%admin%'
        OR user_name LIKE '%사용자8%'
        ORDER BY reg_date DESC 
    )TableA WHERE ROWNUM <= (1*5) + 5 -- (page*b)+5
)TableB WHERE TableB.RNUM > 1*5       -- (page*b)
--페이징쿼리에서 필요한 변수는 2개 
--현재페이지수의 변수  page*b == queryStartNo
--1페이지당 보여줄 갯수의 변수 b == queryPerPageNum
--PageVO에서 필요한 추가변수 :page
--UI하단의 페이지 선택번호 출력할때 사용하는 변수(아래)
--parPageNum 변수 받아서 startPage, endPage 를 구해서 
--하단의 페이지 선택 번호를 출력 
```



####20210607월

오라클 DB관리 로그이 정보:admin/ampsetup -> 변경:Apmsetup1234%
오라클 기초이론 마무리:시퀸스(스프링에서사용-AI자동증가기능)
JUNit로 CRUD 실습 
마이바티스 이용해서 쿼리를 관리
//M-V-C 사이에 데이터를  입출력하는 임시 저장하는 공간 (VO클래스-멤버변수_Get/set메서드)생성
//보통 ValueObject 클래스는 DB테이블과1:1로 매칭
//1.MEmberVO.java VO클래스를 생성(필수)
//2.DB(마이바티스쿼리)쿼리를 만듭니다.(VO사용)
####20210604
오라클일떄:localhost:1521/xe 접속jsp
junit테스트시 SQL에러를 디버그하는 방법은 jdbc드라이버->log4jdbc드라이버
바꾸면, sql에러가 나오게됩니다
junit에서는 sql에러가 보입니다.콘솔창에는 보이지 않습니다. 콘솔창에서sql로그상황이 나오게 하는 드라이버를 추가pom.xml에 추가합니다

```

SELECT * FROM all_users;
DELETE FROM all_susers WHERE username='XE2';
--시노님=테이블명이 사용가ㅣ 힘들정도로 길거나
--오라클은 스프링과 같은 방식 패키지명안에 함수
--프로시저(오라클프로그램)를 만들수 있습니다
--패키지명이 길어서 사용시 개발자에게 부담이 됨.
--위 상황을 해결하도록 만든것이 시노님(동의어)
DROP USER XE2 CASCADE;--xe2사용자를 지울때
--xe사용자 생성한 테이블까지 모두 지우는 명령
--CSS(cascade style sheet)  
```

####20210603
작업비중(시간)=관리자단 (70%)-사용자단(50%)
JUnit테스트: 오라클 연동후 회원 관리부분 CRUD  테스트 진행 예정
오라클 4장부터  CRUD실습예정
admin 회원관리(jsp디자인)부터 프로그램작업 
스프링에서 오라클연동 순서2가지
1. jdbc(Java DabaBase Connection) 확장모듈 pom에 추가
2. 오라클 접속 드라이버 확장 모듈을 pom에 추가x 직접 jar 파일을 추가.
root-context.xml 파일에 오라클 커넥션 빈(스프링클래스)추가
####20210602
-views폴더처럼 직접접근이 불가능한 컨텐츠는 controller(라우터)로 접근하게 됨
-views/home/index.jsp 액박처리, 분해는 관리자단 실습 후 작업
-개발순서: ERD제작-> html제작 -> jsp제작 (현재 :관리자단 10기능 작업 후 사용자단5기능)
-admin 폴더 만든이후 분해는 junit실습 후 작업
-admin 부터 프로그램 작업 시작 예정
####20210601
-프로젝트의 버전을 올립니다.-> 외부라이브러리 부분의 버전을 올림-메이븐 컴파일러->
pom.xm1
-Controller클래스에서 생성한 변수 사용(자바)model객체전송(스프링)return home-> home.jsp출력
-출력하는 방식: EL방식출력-${변수}
-많이 사용하는 방ㅅ익JSTL방식(*표준)사용 출력,반복,조건 등 다양한 방식
-언어를 배우는 순서:주석>디버그하는방법>변수>메서드(함수)>클래스>MVC모델
-디버그하는방법:자바(system.out이용해서 콜솔창에 출력
-스프링에서는 logger를 사용해서 디버그 내용을 출력
-스프링이 관리하는 클래스(빈)의 종류3가지 (사용법은 개발자가 만드는 클래스 상단에 입력)
1.@controller클래스빈: 라우터 역할 메서드가 위치
2.@Service클래스빈 :비지니스로직 메서드가 위치
3.@Repository클래스빈: model처리 메서드를 만듭니다.
-4장 패키지와 예외처리 실습
-vs code 작업한 html를 이클립스에 배치를 합니다.
-ERD기준으로 게시판UI마무리합니다
-resources 폴더에 static컨텐츠(html,css,js,font)배치
-프론트개발자가 작업한 결과 백엔드 개발자가 확인
-
####20210531
-오라클: 테이블스페이스(TableSpace)=(Scheme:Mysql)=데이터베이스(DB-
MS-SQL)
-EntityRelationDiagram(ERD-객체관계):Entity=테이블
-데이터 모델:Model Object를 형상화 시킨것을 모델이라고함
-MVC 스프링프로젝트에서 Mdl MOdel이고, 스프링 프로젝트 구성중에 DB를 가리킴
-참고로 V는View고 jsp를 말함
-참고로 C는Controller고 클래스를 말함
-EX라는 테이블스페이스를 EX사용자료
-스프링프로젝트 ERD 제작 후 , 게시판UI디자인 적용



####20210528(금) 
-extends관계클래스에서 this현재클래스,super부모클래스
-자바앱에서는 객체를 생성 후 사용이 끝나면, 메모리에서 삭제하는 명령이 필수
-객체를 메모리에서 삭제:object=null;
-스프링에서는 내장된 가비지컬렉터(쓰레기수집가)가 자동실행 사용하지 않는 객체를 자동으로 지워줌.
-위와 같이 개발자가 하던 메모리관리를 스프링의 대신=ioc(inversion of control)제어의
역전.스프링특징3가지(ioc,aop, di)

####20210527(목)
- 3장 객체와 클래스부터 시작
- 스프링에서는 클래스 extends(상속)보다는 인터페이스 (implements)를 더 많이 사용합니다
- abstract클래스(추상클래스): 구현내용이없이, 메서드명만 있는 클래스,
- 추상클래스는 메서드명만 있기(구현내용이없기)때문에 , 객체로 만들수 없는 클래스 입니다.
- 부모로서 자식에게 상속만 (메서드 이름만)해서 재 사용 가능하게 함.
- 객체로 만들수없다? 실행가능한 클래스로 사용못하지만 프로그램을 구조화 시킬때 사용
- Step1 aaa = new Step1();//이렇게 생성자로 객체를 만들지 못함
- final클래스: 부모가 될수없는 클래스 (상속해서 재사용이 불가능한 클래스)
- 접근제어자: public(패키지위치에 상관없이 접근-제일 많이 사용)
- public 클래스안에서 멤버변수는 private을 제일 많이 사용.(private보안성 갖춤)
- public 클래스 안에서 멤버메서드는 public을 많이 사용(외부 다은 클래스의 메서드를 사용)
    대신 변수가 private이기  때문에 직접 수정x 실행만 가능
- 객체=오브젝트(자바)=인스턴스(클라우드주로샤용)=실행가능한 클래스
- 클래스 자료형(int, long,String처럼)는 멤버변수, 멤버메서드,서브클래스,등


####20210526(수)
- 붕어빵:붕어빵틀(클래스)-> 붕어빵 구워져서 나오면 오브젝트=객체
- OOP: 자바를 객체지향 (클래스를 실행가능하게 하는 )프로그램밍
- 객체=오브젝트=인스턴스=실행가능한 클래스
- 객체(object)와 클래스? 클래스형 자료를 실행 가능하게 만든 것을 객체라고함
- 추상화 (Abstract) : 오프라인 업무-> 대표업무만 뽑아낸 클래스 =추상화클래스
- 클래스는 멤버변수 +멤버메서드 + 서브클래스
- 변하지 않는 변수=상수=변수명을 대문자(원주율)PT=3.141569...
- 보통 상수변수는 클래스의 제일 상단 위치합니다.
- 기본형(정수자료형-소문자시작): byte<short<int<long 
- 기본형(실수자료형-소주점,소문자시작):float<double,12.34f(실수형숫자)
- 기본형(문자형-''단 따움표 값을 일력해야 에러가 나지 않음):char, 1개문자만 입력
- 문자형에서 유니코드 u숫자입니다
- 기본형(분리형-참,거짓):boolean, (0|1)
- 참조형(대문자로 시작):참조형 변수의 대표적인 변수 클래스 입니다
- 클래스 변수(저장된상태)-> 실행가능하게되었을때, 인스턴스 변수(메모리로드된상태)
- 인스턴스라는 말보다는 오브젝트라는 말을 더 많이 사용합니다.
- String(문자열 클래스 변수): 대문자로 시작.
- 상수변수를 명시적으로 만들때: final int max =100;
- for-each 반복문까지 실습
- 
#### 20210525(화)
- 스프링MVC프로젝트: ModelViewcontroller? 약자 MVC 구조 (웹프로그램구조)
- Src/test-java 폴더 만들었습니다: 테스트 작업은 이 폴더에서 하라는 약속
- Sre/main/java 폴더가 진짜 프로그램작업을 하는 폴더.
- javac helloworld.java -encoding UTF-8 (한글 내용도 컴파일됨)
- 위 java컴파일로러 실행한 결과 ->HelloWorld.class실행파일이 생성
- 주), 클래스파일은 실행 패키지의 루트(최상위)에서 실행해야함
- kr.or.test패키지 root폴더src/test/java폴더에 실행을 해야함
- java kr or test helloworld클래스 실행하게 됨
- 이클립스 나오긴 전, 25년전 java 컴파일에서 class프로그램을 만들었습니다.
- 지금은 터미널에서javac 사용하지 않고, 이클립스에서 바로 실행합니다.
- javac 자바앱 컴파일러-> 클래스 실행파일을 만든.class(자바환경JVM실행)
- 메인븐?maven : 웹프로그램 컴파일러 -> 웹프로그램(앱) 실행파일을 만듬.war(톰캣에서 실행)
- 메이븐이 컴파일할때, 자바소스만 컴파일하는 것이 아니고, 외부 라이브러리도 가져와서 바인딩(묶어줌)하게 됨=패키징이라고합니다 .war(와르)파일로 패키지됨
- 메이븐이 관리하는 외부 라이브러이파일(lib) 목록을 저장하는 파일pom.xml입니다.
- pom.xml에서 자바버전을  1.6에서 1.8변경 후 메이븐을 업데이트
- 외부라이브러이 파일을 참조하는  방식 = dependency
- jar파일? javaARchive = jar 자바클래스를  패키징한 파일