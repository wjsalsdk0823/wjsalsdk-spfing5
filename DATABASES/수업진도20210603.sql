--DESC:Description 테이블구조 설명
DESC dept;
--selec: 테이블 내용 조회, where 조회조건 as(Alias) 별칭으로 필드명이 길때 
--concat오라클내장함수는 레포트 작성시 
SELECT 
concat(deptno,' 번') as "부서번호"
, dname as "부서명"
, concat (loc, ' 시') as "위치"
FROM dept 
WHERE loc = 'NEW YORK';
--DUAL  가상테이블이름.테이블이 없는 내용을 select할때 
SELECT 3+5 as "3더하기8은" from dual;
--레코드(row) : 컬럼(필드field)들로 구성
SELECT concat (count(*),'명') as "연봉이2000인 직원" FROM  emp WHERE sal > 2000;
--큰따옴표(필드명은), 작음따옴표(문자처리,비교,결합)
SELECT  * FROM emp WHERE ename  != 'KING';
SELECT * FROM emp WHERE hiredate >= '1982/01/01';
--or +(합집합), and X(교집합)
SELECT * FROM emp
WHERE deptno = 10 OR job = 'MANAGER';   
SELECT * FROM emp WHERE sal 
NOT BETWEEN 2000 AND 3000;
SELECT * FROM emp WHERE hiredate
BETWEEN '1980/01/01' AND '1980.12.31';
SELECT * FROM emp WHERE comm 
NOT IN (300,500,1400);
--like 조회 , 와일드카드 조회
--키워드에 괄호가 있으면 함수upper(),concat(),count()
SELECT * FROM emp WHERE ename LIKE upper('k%');
SELECT * FROM emp WHERE ename LIKE '%N';
--null이 중요한 이유: null값이 있으면 검색x
--null값이 필드에 있을때, 검색은?
SELECT * FROM emp WHERE comm IS NULL;
--NVL(null value)널 값을 대치하는 함수
--사원중에 커미션을 0원 받은 사람은? null사람도 구하려면
SELECT nvl2(comm,100,0), E.* FROM emp E WHERE NVL(comm,0) = 0;
--오라클은 표준쿼리x ANSI 쿼리 표준입니다.
SELECT DECODE(comm,null,0,100),NVL2(comm,100,0), E.* FROM emp E WHERE NVL(comm,0) = 0;
--정렬 sort,순서 order by 필드명 오름차순[초기값]\내림차순
--서브쿼리?(select쿼리가 중복되어있는)입니다.
SELECT ROWNUM,E.* FROM (-- 테이블명
SELECT * FROM emp ORDER BY sal DESC 
) E WHERE ROWNUM = 1;
--위 문장을 해석 할때는 안쪽부터 해석 
--위 정렬에서 1등만 구할  limit는 mysql(마리아DB)의 명령어.오라클x
--mysql(마리아DB)있는 AI(autoincrement)자동증가값 명령 오라클 x
--중복레코드row를 제거하는 명령어 distinct
SELECT deptno as "부서번호"  FROM emp;--사원수대로 부서번호가 나옴
SELECT DISTINCT deptno as "부서번호" FROM emp;
--문자열을 연결할때 concat함수외에 ||파이브라인2개를 겹쳐서 구현
SELECT ename ||' is a '|| job AS "문자열 연결" FROM emp; 

