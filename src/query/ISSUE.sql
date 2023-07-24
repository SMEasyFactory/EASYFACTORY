DROP TABLE ISSUE;
DROP SEQUENCE ISSUE_SEQ;
----------------------------------------
---- 이슈 게시판 테이블 ----------------
----------------------------------------
CREATE TABLE ISSUE (
      NO NUMBER NOT NULL
      , TITLE VARCHAR2(50) NOT NULL
      , CONTENT VARCHAR2(2000) NOT NULL
      , AUTHOR VARCHAR2(50) NOT NULL
      , REGDATE DATE DEFAULT SYSDATE NOT NULL
      , MODDATE DATE DEFAULT NULL
      , PRIMARY KEY(NO)
);
drop table issue;
----------------------------------------
---- 이슈 게시글 번호 SEQUENCE ---------
----------------------------------------
DROP SEQUENCE ISSUE_SEQ;
CREATE SEQUENCE ISSUE_SEQ
       INCREMENT BY 1
       START WITH 1
       MINVALUE 1
       MAXVALUE 999999
       NOCYCLE
       NOCACHE
       NOORDER;

----------------------------------------
---- 이슈 게시판 테스트 데이터 삽입 ----
----------------------------------------
INSERT INTO ISSUE (
    NO
    , TITLE
    , CONTENT
    , AUTHOR
    , REGDATE
)
VALUES (
    ISSUE_SEQ.NEXTVAL
    ,'테스트 제목입니다.'
    ,'테스트 내용'
    , (SELECT ID FROM MEMBER WHERE UPPER(ID) = UPPER('internalTest') AND PW = 'testpw1')
    , to_char(sysdate,'yyyy-mm-dd')
);

----------------------------------------
---- 이슈 게시판 글 보기(페이징) -------
----------------------------------------
SELECT
    ROWNUM as rnum
    , b.*
FROM (
        SELECT
            NO
            , TITLE
            , CONTENT
            , AUTHOR
            , TO_DATE(TO_CHAR(REGDATE,'yyyy-MM-DD HH24:MI:SS'), 'yyyy-MM-DD HH24:MI:SS') as REGDATE
            , TO_DATE(TO_CHAR(MODDATE,'yyyy-MM-DD HH24:MI:SS'), 'yyyy-MM-DD HH24:MI:SS') as MODDATE
        FROM ISSUE
        ORDER BY NO DESC) b
WHERE
    ROWNUM BETWEEN 1 AND 10;
    
----------------------------------------
---- 작성일 범위로 게시물 COUNT --------
----------------------------------------
SELECT 
    COUNT(*)
FROM ISSUE
WHERE
    NO > 0
and regDate BETWEEN TO_DATE('2023-07-10 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('2023-07-11 23:59:59', 'YYYY-MM-DD HH24:MI:SS');


------------------------------------------------------------------


SELECT
    a.fileno,
    a.no,
    a.originalname,
    a.savename,
    a.filesize,
    b.TITLE,
    b.CONTENT,
    b.AUTHOR,
    b.REGDATE
FROM
    ez_file a
INNER JOIN
    issue b ON a.no = b.no
ORDER BY
    b.no DESC;


COMMIT;

