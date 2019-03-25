CREATE SEQUENCE public."studentSeq";

ALTER TABLE public."studentSeq"
  OWNER TO postgres;
COMMENT ON SEQUENCE public."studentSeq"
  IS 'sequence for student id';

CREATE OR REPLACE FUNCTION StudentFunctionForId()
 RETURNS "trigger" AS
 $BODY$
 BEGIN
   New.id:=nextval('studentSeq'); 
   Return NEW;
 END;
 $BODY$
 LANGUAGE 'plpgsql' VOLATILE;

 CREATE TRIGGER BIStudentTrigger
 BEFORE INSERT
 ON student
 FOR EACH ROW
 EXECUTE PROCEDURE StudentFunctionForId();

 INSERT INTO student (name, lastname) VALUES ('lemon', 'tea');

-----------------------------


CREATE SEQUENCE studentSequence;

CREATE TABLE student (
  id integer NOT NULL DEFAULT nextval('studentSequence') PRIMARY KEY
 ,name VARCHAR(255), lastname VARCHAR(255));

-----------------------------
	
ALTER TABLE student ALTER COLUMN id SET DEFAULT nextval('studentSequence');
