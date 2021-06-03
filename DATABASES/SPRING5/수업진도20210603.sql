--DESC:Description ���̺��� ����
DESC dept;
--selec: ���̺� ���� ��ȸ, where ��ȸ���� as(Alias) ��Ī���� �ʵ���� �涧 
--concat����Ŭ�����Լ��� ����Ʈ �ۼ��� 
SELECT 
concat(deptno,' ��') as "�μ���ȣ"
, dname as "�μ���"
, concat (loc, ' ��') as "��ġ"
FROM dept 
WHERE loc = 'NEW YORK';
--DUAL  �������̺��̸�.���̺��� ���� ������ select�Ҷ� 
SELECT 3+5 as "3���ϱ�8��" from dual;
--���ڵ�(row) : �÷�(�ʵ�field)��� ����
SELECT concat (count(*),'��') as "������2000�� ����" FROM  emp WHERE sal > 2000;
--ū����ǥ(�ʵ����), ��������ǥ(����ó��,��,����)
SELECT  * FROM emp WHERE ename  != 'KING';
SELECT * FROM emp WHERE hiredate >= '1982/01/01';
--or +(������), and X(������)
SELECT * FROM emp
WHERE deptno = 10 OR job = 'MANAGER';   
SELECT * FROM emp WHERE sal 
NOT BETWEEN 2000 AND 3000;
SELECT * FROM emp WHERE hiredate
BETWEEN '1980/01/01' AND '1980.12.31';
SELECT * FROM emp WHERE comm 
NOT IN (300,500,1400);
--like ��ȸ , ���ϵ�ī�� ��ȸ
--Ű���忡 ��ȣ�� ������ �Լ�upper(),concat(),count()
SELECT * FROM emp WHERE ename LIKE upper('k%');
SELECT * FROM emp WHERE ename LIKE '%N';
--null�� �߿��� ����: null���� ������ �˻�x
--null���� �ʵ忡 ������, �˻���?
SELECT * FROM emp WHERE comm IS NULL;
--NVL(null value)�� ���� ��ġ�ϴ� �Լ�
--����߿� Ŀ�̼��� 0�� ���� �����? null����� ���Ϸ���
SELECT nvl2(comm,100,0), E.* FROM emp E WHERE NVL(comm,0) = 0;
--����Ŭ�� ǥ������x ANSI ���� ǥ���Դϴ�.
SELECT DECODE(comm,null,0,100),NVL2(comm,100,0), E.* FROM emp E WHERE NVL(comm,0) = 0;
--���� sort,���� order by �ʵ�� ��������[�ʱⰪ]\��������
--��������?(select������ �ߺ��Ǿ��ִ�)�Դϴ�.
SELECT ROWNUM,E.* FROM (-- ���̺��
SELECT * FROM emp ORDER BY sal DESC 
) E WHERE ROWNUM = 1;
--�� ������ �ؼ� �Ҷ��� ���ʺ��� �ؼ� 
--�� ���Ŀ��� 1� ����  limit�� mysql(������DB)�� ��ɾ�.����Ŭx
--mysql(������DB)�ִ� AI(autoincrement)�ڵ������� ��� ����Ŭ x
--�ߺ����ڵ�row�� �����ϴ� ��ɾ� distinct
SELECT deptno as "�μ���ȣ"  FROM emp;--�������� �μ���ȣ�� ����
SELECT DISTINCT deptno as "�μ���ȣ" FROM emp;
--���ڿ��� �����Ҷ� concat�Լ��ܿ� ||���̺����2���� ���ļ� ����
SELECT ename ||' is a '|| job AS "���ڿ� ����" FROM emp; 

