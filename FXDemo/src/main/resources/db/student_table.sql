/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  marcin
 * Created: Mar 25, 2019
 */

CREATE TABLE public.student
(
  id bigint NOT NULL,
  lastname character varying(255),
  name character varying(255),
  date date,
  CONSTRAINT person_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.student
  OWNER TO postgres;