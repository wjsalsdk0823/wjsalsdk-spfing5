-- 생성자 Oracle SQL Developer Data Modeler 19.2.0.182.1216
--   위치:        2021-05-31 15:04:02 KST
--   사이트:      Oracle Database 11g
--   유형:      Oracle Database 11g




CREATE TABLE tbl_attach (
    save_file_name   VARCHAR2(255) NOT NULL,
    real_file_name   VARCHAR2(255),
    reg_date         DATE,
    bno              NUMBER(11)
)
LOGGING;

COMMENT ON TABLE tbl_attach IS
    '첨부파일관리테이블';

COMMENT ON COLUMN tbl_attach.save_file_name IS
    '물리적으로저장되는파일명(해시데이터)';

COMMENT ON COLUMN tbl_attach.real_file_name IS
    '한글파일명으로UI에서보이게';

COMMENT ON COLUMN tbl_attach.reg_date IS
    '등록일자';

ALTER TABLE tbl_attach ADD CONSTRAINT tbl_attach_pk PRIMARY KEY ( save_file_name );
CREATE TABLE tbl_board (
    bno           NUMBER(11) NOT NULL,
    title         VARCHAR2(255),
    content       CLOB,
    writer        VARCHAR2(50),
    view_count    NUMBER(11),
    reply_count   NUMBER(11),
    reg_date_1    TIMESTAMP,
    update_date   TIMESTAMP,
    board_type    VARCHAR2(20)
)
LOGGING;

COMMENT ON TABLE tbl_board IS
    '게시물관리테이블';

COMMENT ON COLUMN tbl_board.bno IS
    '게시판고유번호(자동증가처리)';

COMMENT ON COLUMN tbl_board.title IS
    '게시물제목';

COMMENT ON COLUMN tbl_board.content IS
    '글내용';

COMMENT ON COLUMN tbl_board.writer IS
    '글작성자ID';

COMMENT ON COLUMN tbl_board.view_count IS
    '글조회수카운터';

COMMENT ON COLUMN tbl_board.reply_count IS
    '댓글카운트';

COMMENT ON COLUMN tbl_board.reg_date_1 IS
    '등록일시';

COMMENT ON COLUMN tbl_board.update_date IS
    '수정일시';

ALTER TABLE tbl_board ADD CONSTRAINT tbl_board_pk PRIMARY KEY ( bno );
CREATE TABLE tbl_board_type (
    board_type   VARCHAR2(20) NOT NULL,
    board_name   VARCHAR2(50),
    board_sun    NUMBER(11)
)
LOGGING;

COMMENT ON TABLE tbl_board_type IS
    '게시판생성관리(NOTICE,GALLERY등등)';

COMMENT ON COLUMN tbl_board_type.board_type IS
    '게시판타입(NOTICE,GALLERY)';

COMMENT ON COLUMN tbl_board_type.board_name IS
    '한글게시판이름';

COMMENT ON COLUMN tbl_board_type.board_sun IS
    '게시판출력순서';

ALTER TABLE tbl_board_type ADD CONSTRAINT tbl_board_type_pk PRIMARY KEY ( board_type );
CREATE TABLE tbl_member (
    user_id       VARCHAR2(50) NOT NULL,
    user_pw       VARCHAR2(55),
    user_name     VARCHAR2(255),
    email         VARCHAR2(255),
    point         NUMBER(11),
    enabled       NUMBER(1),
    levels        VARCHAR2(50),
    reg_date      TIMESTAMP,
    update_date   TIMESTAMP
)
LOGGING;

COMMENT ON TABLE tbl_member IS
    '회원관리테이블';

COMMENT ON COLUMN tbl_member.user_id IS
    '회원고유ID';

COMMENT ON COLUMN tbl_member.user_pw IS
    '회원로그인암호(스프링시큐리티)';

COMMENT ON COLUMN tbl_member.user_name IS
    '회원이름';

COMMENT ON COLUMN tbl_member.email IS
    '회원이메일';

COMMENT ON COLUMN tbl_member.point IS
    '회원포인트';

COMMENT ON COLUMN tbl_member.enabled IS
    '로그인승인여부';

COMMENT ON COLUMN tbl_member.levels IS
    '회원권한(ROLE_ADMIN_ROLE_USER)';

COMMENT ON COLUMN tbl_member.reg_date IS
    '등록일시';

COMMENT ON COLUMN tbl_member.update_date IS
    '수정일시';

ALTER TABLE tbl_member ADD CONSTRAINT tbl_member_pk PRIMARY KEY ( user_id );
CREATE TABLE tbl_reply (
    rno           NUMBER(11) NOT NULL,
    reply_text    VARCHAR2(255),
    replyer       VARCHAR2(50),
    reg_date      DATE,
    update_date   DATE,
    bno           NUMBER(11)
)
LOGGING;

COMMENT ON TABLE tbl_reply IS
    '댓글관리테이블';

COMMENT ON COLUMN tbl_reply.rno IS
    '댓글고유번호(자동증가AI)';

COMMENT ON COLUMN tbl_reply.reply_text IS
    '댓글내용';

COMMENT ON COLUMN tbl_reply.replyer IS
    '댓글작정자ID';

COMMENT ON COLUMN tbl_reply.reg_date IS
    '등록일';

COMMENT ON COLUMN tbl_reply.update_date IS
    '수정일';

COMMENT ON COLUMN tbl_reply.bno IS
    '게시물관리고유값외래키';

ALTER TABLE tbl_reply ADD CONSTRAINT tbl_reply_pk PRIMARY KEY ( rno );
ALTER TABLE tbl_attach
    ADD CONSTRAINT tbl_attach_tbl_board_fk FOREIGN KEY ( bno )
        REFERENCES tbl_board ( bno )
    NOT DEFERRABLE;
ALTER TABLE tbl_board
    ADD CONSTRAINT tbl_board_tbl_board_type_fk FOREIGN KEY ( board_type )
        REFERENCES tbl_board_type ( board_type )
    NOT DEFERRABLE;
ALTER TABLE tbl_reply
    ADD CONSTRAINT tbl_reply_tbl_board_fk FOREIGN KEY ( bno )
        REFERENCES tbl_board ( bno )
    NOT DEFERRABLE;

-- Oracle SQL Developer Data Modeler 요약 보고서: 
-- 
-- CREATE TABLE                             5
-- CREATE INDEX                             0
-- CREATE VIEW                              0
-- ALTER TABLE                              8
-- ALTER INDEX                              0
-- ALTER VIEW                               0
-- DROP TABLE                               0
-- DROP INDEX                               0
-- DROP VIEW                                0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- DROP PACKAGE                             0
-- DROP PACKAGE BODY                        0
-- DROP PROCEDURE                           0
-- DROP FUNCTION                            0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- DROP TRIGGER                             0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- DROP TYPE                                0
-- CREATE SEQUENCE                          0
-- ALTER SEQUENCE                           0
-- DROP SEQUENCE                            0
-- CREATE MATERIALIZED VIEW                 0
-- DROP MATERIALIZED VIEW                   0
-- CREATE SYNONYM                           0
-- DROP SYNONYM                             0
-- CREATE DIMENSION                         0
-- DROP DIMENSION                           0
-- CREATE CONTEXT                           0
-- DROP CONTEXT                             0
-- CREATE DIRECTORY                         0
-- DROP DIRECTORY                           0

-- 
-- ERRORS                                   0
-- WARNINGS                                 0
