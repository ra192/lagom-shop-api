--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.1
-- Dumped by pg_dump version 9.6.1

-- Started on 2017-03-19 20:53:25

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2192 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 185 (class 1259 OID 74709)
-- Name: category; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE category (
    id bigint NOT NULL,
    displayname character varying(255),
    name character varying(255) NOT NULL,
    parent_id bigint
);


ALTER TABLE category OWNER TO myshop;

--
-- TOC entry 191 (class 1259 OID 82899)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: myshop
--

CREATE SEQUENCE category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE category_id_seq OWNER TO myshop;

--
-- TOC entry 186 (class 1259 OID 74715)
-- Name: category_property; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE category_property (
    category_id bigint NOT NULL,
    properties_id bigint NOT NULL
);


ALTER TABLE category_property OWNER TO myshop;

--
-- TOC entry 187 (class 1259 OID 74720)
-- Name: product; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE product (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    description character varying(255),
    displayname character varying(255),
    imageurl character varying(255),
    price double precision,
    rating real,
    category_id bigint
);


ALTER TABLE product OWNER TO myshop;

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE product_id_seq OWNER TO myshop;

--
-- TOC entry 188 (class 1259 OID 74726)
-- Name: product_property_value; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE product_property_value (
    product_id bigint NOT NULL,
    propertyvalues_id bigint NOT NULL
);


ALTER TABLE product_property_value OWNER TO myshop;

--
-- TOC entry 189 (class 1259 OID 74729)
-- Name: property; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE property (
    id bigint NOT NULL,
    displayname character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE property OWNER TO myshop;

--
-- TOC entry 192 (class 1259 OID 82901)
-- Name: property_id_seq; Type: SEQUENCE; Schema: public; Owner: myshop
--

CREATE SEQUENCE property_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE property_id_seq OWNER TO myshop;

--
-- TOC entry 190 (class 1259 OID 74735)
-- Name: property_value; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE property_value (
    id bigint NOT NULL,
    displayname character varying(255),
    name character varying(255) NOT NULL,
    property_id bigint
);


ALTER TABLE property_value OWNER TO myshop;

--
-- TOC entry 193 (class 1259 OID 82903)
-- Name: user; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    username character varying,
    password character varying
);


ALTER TABLE "user" OWNER TO myshop;

--
-- TOC entry 194 (class 1259 OID 82911)
-- Name: user_role; Type: TABLE; Schema: public; Owner: myshop
--

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role character varying NOT NULL
);


ALTER TABLE user_role OWNER TO myshop;

CREATE TABLE public.token
(
  user_id bigint,
  token character varying,
  valid_to timestamp with time zone
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.token
  OWNER TO myshop;

--
-- TOC entry 2038 (class 2606 OID 74751)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 2042 (class 2606 OID 74753)
-- Name: category_property category_property_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category_property
    ADD CONSTRAINT category_property_pkey PRIMARY KEY (category_id, properties_id);


--
-- TOC entry 2044 (class 2606 OID 74755)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 2048 (class 2606 OID 74757)
-- Name: product_property_value product_property_value_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product_property_value
    ADD CONSTRAINT product_property_value_pkey PRIMARY KEY (product_id, propertyvalues_id);


--
-- TOC entry 2050 (class 2606 OID 74759)
-- Name: property property_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY property
    ADD CONSTRAINT property_pkey PRIMARY KEY (id);


--
-- TOC entry 2054 (class 2606 OID 74761)
-- Name: property_value property_value_pkey; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY property_value
    ADD CONSTRAINT property_value_pkey PRIMARY KEY (id);


--
-- TOC entry 2046 (class 2606 OID 74763)
-- Name: product uk_10ganh9tlh637bdu7sammc8dp; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product
    ADD CONSTRAINT uk_10ganh9tlh637bdu7sammc8dp UNIQUE (code);


--
-- TOC entry 2052 (class 2606 OID 74765)
-- Name: property uk_4jaqs0het40jm6jmhi9fa7dmh; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY property
    ADD CONSTRAINT uk_4jaqs0het40jm6jmhi9fa7dmh UNIQUE (name);


--
-- TOC entry 2040 (class 2606 OID 74767)
-- Name: category uk_foei036ov74bv692o5lh5oi66; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category
    ADD CONSTRAINT uk_foei036ov74bv692o5lh5oi66 UNIQUE (name);


--
-- TOC entry 2056 (class 2606 OID 74771)
-- Name: property_value uk_n0l0ufcisjulucwf00f0dwmq8; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY property_value
    ADD CONSTRAINT uk_n0l0ufcisjulucwf00f0dwmq8 UNIQUE (name);


--
-- TOC entry 2058 (class 2606 OID 82910)
-- Name: user user_pk; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


--
-- TOC entry 2060 (class 2606 OID 82923)
-- Name: user_role user_role_pk; Type: CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pk PRIMARY KEY (user_id, role);


--
-- TOC entry 2067 (class 2606 OID 74774)
-- Name: property_value fk_21tglbpy5qhtj27hfnj9r5r0e; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY property_value
    ADD CONSTRAINT fk_21tglbpy5qhtj27hfnj9r5r0e FOREIGN KEY (property_id) REFERENCES property(id);


--
-- TOC entry 2064 (class 2606 OID 74784)
-- Name: product fk_b7afq93qsn7aoydaftixggf14; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product
    ADD CONSTRAINT fk_b7afq93qsn7aoydaftixggf14 FOREIGN KEY (category_id) REFERENCES category(id);


--
-- TOC entry 2062 (class 2606 OID 74789)
-- Name: category_property fk_g8w1nffq59rad5xuppqt5dk0y; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category_property
    ADD CONSTRAINT fk_g8w1nffq59rad5xuppqt5dk0y FOREIGN KEY (category_id) REFERENCES category(id);


--
-- TOC entry 2065 (class 2606 OID 74794)
-- Name: product_property_value fk_maqwk3gqjmmj8kqoq6d5o1yfc; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product_property_value
    ADD CONSTRAINT fk_maqwk3gqjmmj8kqoq6d5o1yfc FOREIGN KEY (propertyvalues_id) REFERENCES property_value(id);


--
-- TOC entry 2061 (class 2606 OID 74799)
-- Name: category fk_p6elut499cl32in8b8j8sy2n4; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category
    ADD CONSTRAINT fk_p6elut499cl32in8b8j8sy2n4 FOREIGN KEY (parent_id) REFERENCES category(id);


--
-- TOC entry 2066 (class 2606 OID 74804)
-- Name: product_property_value fk_s4r657t8hjwicmafctnfoe4js; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY product_property_value
    ADD CONSTRAINT fk_s4r657t8hjwicmafctnfoe4js FOREIGN KEY (product_id) REFERENCES product(id);


--
-- TOC entry 2063 (class 2606 OID 74809)
-- Name: category_property fk_so37fdysm1mymuynyftclcf3d; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY category_property
    ADD CONSTRAINT fk_so37fdysm1mymuynyftclcf3d FOREIGN KEY (properties_id) REFERENCES property(id);


--
-- TOC entry 2068 (class 2606 OID 82917)
-- Name: user_role user_role_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: myshop
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);

-- Completed on 2017-03-19 20:53:25

--
-- PostgreSQL database dump complete
--

