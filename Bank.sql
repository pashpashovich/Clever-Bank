--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.3

-- Started on 2023-09-03 21:07:27

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 865 (class 1247 OID 16644)
-- Name: bank_names; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.bank_names AS ENUM (
    'CLEVERBANK',
    'BELINVESTBANK',
    'BSBBANK',
    'BELARUSBANK',
    'STATUSBANK'
);


ALTER TYPE public.bank_names OWNER TO postgres;

--
-- TOC entry 868 (class 1247 OID 16656)
-- Name: banks; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.banks AS ENUM (
    'CLEVERBANK',
    'BELINVESTBANK',
    'BSBBANK',
    'BELARUSBANK',
    'STATUSBANK'
);


ALTER TYPE public.banks OWNER TO postgres;

--
-- TOC entry 871 (class 1247 OID 16693)
-- Name: currency_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.currency_type AS ENUM (
    'BYN',
    'USD',
    'RUB',
    'XEU',
    'CNY'
);


ALTER TYPE public.currency_type OWNER TO postgres;

--
-- TOC entry 856 (class 1247 OID 16543)
-- Name: transaction_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.transaction_type AS ENUM (
    'DEPOSIT',
    'WITHDRAWAL',
    'PAYMENT',
    'ACCOUNT_TRANSFER',
    'SALARY_CREDIT'
);


ALTER TYPE public.transaction_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 16530)
-- Name: Accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Accounts" (
    accountnumber bigint NOT NULL,
    balance double precision NOT NULL,
    account_id bigint NOT NULL,
    bank_id bigint NOT NULL,
    date_of_open date,
    currency_t public.currency_type
);


ALTER TABLE public."Accounts" OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16492)
-- Name: Admins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Admins" (
    admin_id bigint NOT NULL
);


ALTER TABLE public."Admins" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16580)
-- Name: Banks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Banks" (
    names public.banks,
    bank_id bigint NOT NULL
);


ALTER TABLE public."Banks" OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16671)
-- Name: Banks_bank_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Banks" ALTER COLUMN bank_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Banks_bank_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 217 (class 1259 OID 16495)
-- Name: Customers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Customers" (
    customer_id bigint NOT NULL,
    fio text NOT NULL,
    has_access boolean NOT NULL
);


ALTER TABLE public."Customers" OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16553)
-- Name: Transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Transactions" (
    transaction_id bigint NOT NULL,
    type_tr public.transaction_type NOT NULL,
    fromaccount_id bigint NOT NULL,
    toaccount_id bigint NOT NULL,
    amount double precision NOT NULL,
    date date NOT NULL,
    "time" time without time zone NOT NULL
);


ALTER TABLE public."Transactions" OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16485)
-- Name: Users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Users" (
    user_id bigint NOT NULL,
    login text NOT NULL,
    password text NOT NULL,
    salt text NOT NULL
);


ALTER TABLE public."Users" OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16484)
-- Name: User_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Users" ALTER COLUMN user_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."User_user_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3281 (class 0 OID 16530)
-- Dependencies: 218
-- Data for Name: Accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Accounts" (accountnumber, balance, account_id, bank_id, date_of_open, currency_t) FROM stdin;
6789876	200	2	1	2019-02-24	BYN
765431234	954.7	19	2	2023-09-03	BYN
76543456	432.7	19	3	2023-09-03	XEU
654876543	546.7	19	5	2023-09-03	BYN
6545678	20.6	2	1	2021-09-12	BYN
65749467	345.7	28	4	2023-09-03	RUB
76545892	325.7	28	5	2023-09-03	BYN
76530874	328.9	18	5	2023-09-03	BYN
87659035	342.7	24	1	2023-09-03	BYN
56804379	437.9	24	1	2023-09-03	BYN
65842876	432.8	24	1	2023-09-03	BYN
45678333	567.4	24	1	2023-09-03	BYN
87654369	548.8	25	1	2023-09-03	BYN
67543480	235.6	25	1	2023-09-03	BYN
23565	345.6	25	1	2023-09-03	BYN
34676534	543.5	25	1	2023-09-03	BYN
45697644	234.5	25	1	2023-09-03	BYN
34788721	2346.7	21	1	2023-09-03	BYN
65234582	5436.7	21	1	2023-09-03	BYN
45677621	654.7	21	1	2023-09-03	BYN
65435686	345.6	21	1	2023-09-03	BYN
34568543	546.7	21	1	2023-09-03	BYN
76546784	765.8	21	1	2023-09-03	BYN
76547654	567.8	21	1	2023-09-03	BYN
67894323	876.8	27	3	2023-09-03	BYN
8765478	547.8	27	3	2023-09-03	USD
76537986	3457.8	27	2	2023-09-03	XEU
86543789	543.8	22	5	2023-09-03	XEU
56789321	657.8	22	2	2023-09-03	USD
78890432	6578.4	22	1	2023-09-03	BYN
67894436	5678	22	3	2023-09-03	RUB
45678942	6548.7	22	1	2023-09-03	BYN
56784357	567.8	20	3	2023-09-03	BYN
13489055	6547.2	20	2	2023-09-03	BYN
4567894	4568.3	20	2	2023-09-03	RUB
1237653	23.2	15	1	2023-08-28	BYN
87653486	658.9	20	2	2023-09-03	USD
87654232	456.8	23	1	2023-09-03	BYN
68987652	567.8	23	1	2023-09-03	BYN
67894245	5678.3	23	1	2023-09-03	BYN
654338	110	2	1	2022-05-16	BYN
\.


--
-- TOC entry 3279 (class 0 OID 16492)
-- Dependencies: 216
-- Data for Name: Admins; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Admins" (admin_id) FROM stdin;
3
\.


--
-- TOC entry 3283 (class 0 OID 16580)
-- Dependencies: 220
-- Data for Name: Banks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Banks" (names, bank_id) FROM stdin;
CLEVERBANK	1
BELINVESTBANK	2
BSBBANK	3
BELARUSBANK	4
STATUSBANK	5
\.


--
-- TOC entry 3280 (class 0 OID 16495)
-- Dependencies: 217
-- Data for Name: Customers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Customers" (customer_id, fio, has_access) FROM stdin;
2	Косович Павел Владимирович	t
10	Косович Анжелика Леонидовна	t
9	Химич Елена Сергеевна	t
11	Прокопенко Алина Дмитривена	t
12	Киринович Елена Александровна	t
13	Бажаткова Элина Эдуардовна	t
15	Васильков Алексей Иванович	t
26	Косолапкин Тимур Владимирович	f
16	Иванов Иван Иванович	t
17	Анисимов Иван Михайлович	t
19	Бобрович Майкл Майклович	t
28	Кучерявая Алла Михайловна	t
18	Иванчик Жанна Денисовна	t
24	Чапанова Людмила Дмитриевна	t
25	Ковалёва Наталья Павловна	t
21	Логинов Сергей Николаевич	t
27	Куприянова Алина Ивановна	t
22	Косарева Алина Ивановна	t
20	Безымянный Джон Владимирович	t
23	Зуев Александр Александрович	t
\.


--
-- TOC entry 3282 (class 0 OID 16553)
-- Dependencies: 219
-- Data for Name: Transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Transactions" (transaction_id, type_tr, fromaccount_id, toaccount_id, amount, date, "time") FROM stdin;
34588765	WITHDRAWAL	654338	6545678	100	2023-08-08	14:36:32
12	DEPOSIT	654338	654338	12	2023-08-27	16:27:59.766
444808727	DEPOSIT	6789876	6789876	203	2023-08-27	16:39:24.78
444809007	ACCOUNT_TRANSFER	6789876	6545678	104	2023-08-27	19:19:45.101
444809009	ACCOUNT_TRANSFER	6789876	6545678	100.5	2023-08-27	19:21:13.014
444809041	ACCOUNT_TRANSFER	654338	6545678	21.6	2023-08-27	19:53:54.232
444809043	ACCOUNT_TRANSFER	654338	6789876	10.8	2023-08-27	19:55:40.979
444809047	ACCOUNT_TRANSFER	654338	6545678	2.4	2023-08-27	19:59:42.974
444809091	ACCOUNT_TRANSFER	6545678	6789876	12.8	2023-08-27	20:03:33.807
444809095	WITHDRAWAL	6545678	6545678	123.7	2023-08-27	20:07:47.319
445568405	ACCOUNT_TRANSFER	6545678	6789876	54.8	2023-09-03	13:17:04.714
445568797	DEPOSIT	6545678	6545678	64.7	2023-09-03	17:09:45.025
445568800	WITHDRAWAL	6545678	6545678	-10	2023-09-03	17:12:07.451
445568808	DEPOSIT	6789876	6789876	100.6	2023-09-03	17:20:45.611
445568809	WITHDRAWAL	6789876	6789876	194	2023-09-03	17:21:23.022
445568811	DEPOSIT	6545678	6545678	37.1	2023-09-03	17:23:18.802
445568814	DEPOSIT	6789876	6789876	6	2023-09-03	17:26:00.791
445568818	DEPOSIT	6545678	6545678	8.1	2023-09-03	17:30:05.139
445568821	DEPOSIT	6545678	6545678	2.9	2023-09-03	17:33:59.498
445568822	DEPOSIT	654338	654338	2.2	2023-09-03	17:34:24.792
445569182	WITHDRAWAL	6545678	6545678	45.2	2023-09-03	17:37:24.822
\.


--
-- TOC entry 3278 (class 0 OID 16485)
-- Dependencies: 215
-- Data for Name: Users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Users" (user_id, login, password, salt) FROM stdin;
2	pashpashovich	faoqx4Py+qq2mcQoGnbJLBPXyPo5vEuKnaR0a6zQwJI=	MGT6EDMMMw0+uMoGGQwWiw==
9	pasha17	JQZuPEdAFqS+Cogzk0kZUx8m0dErrBnfa9UWEiNeGjY=	YEBrW5s4UK0pooxbxp5tUw==
3	pashka	kUpT9IoOmtsCXqpHsJjKij8ofwbfKbVLwemENOwbv/Q=	AgGkqTllqbtItGZwydBVUA==
10	mam	QsbJTkhs1qzQFP43sO7sFvhHh8+Wts3qtWWjGNT5NJk=	6idM5x+d6sZOGSEz5PbJig==
11	dfgh	+Y03JhIVROLCsx13e2RCfF0koTd+Pyl0PVv4yKvIb4k=	wv9ks8E8jZlKJtBnjq0X6w==
12	23wert	9+kNjZsDR0rdGauBDGV0s4xa08T+hM/rvtGDJ1adPAA=	wuSU3x8rAlejuY6vJF2WAw==
13	pasha22	sB2YMw7TeniLXOGQbUaCx0hHl2mR/ZOg9h7KDpohzbQ=	WMYqs2xbJPHPCONrMl4kbQ==
15	pashka17	GYhvBVSE/O6gYX86cr5MxXf7BMKb3I6B+RmCJ+RtgyU=	OzYWOTvOw/JyGU+jHAEgvw==
16	pashka22	bQyA713zjFNEukBIh2PciUoGPQ/XLWWZX2nIFBtdffk=	Oe6zOBdmQeQt6KskzY0Nbg==
17	ssopfa65	t0/Q/hIwAENGNpYagRsHYBq1Lgxh1jv5uON+FcM9T/E=	ZIYuw7HSqibNfhkrohXL3Q==
18	vladick65	NIlWtK4DAhaT2fNWmZB4aOLJQghizghRdJv4Lh9Owy8=	dwTQ1UDDdO4hxjsAN25MJw==
19	nazar67g	IXplR0hpoGQ0SkmNAX4QlM+W33je7iBVPYIg8EOF+Is=	0wlHSXu7Ax2eXyzcRm4Z2A==
20	frosya57f	o1S8//IgX2WfoCMY9PGE9PbQe+UdIEq95WdTOKeMs0Y=	u5izS9/2pWSUND4DJZzFYQ==
21	cloun6fj	WDHfaapEZLUyPKKy+clUf9sbNvJJ7OqichBXtp/24AU=	TnZ5P4agcrXg7oCM4t1Xdg==
23	pasha65	i+5MttqzebnYuv9C+qiJQMAhGHuaOkmFlAvU/w2Ftio=	I5oQr4cYEyovMvbsWN5SMQ==
24	daska87ytf	18Ib0iShUDqIiOjwgEPLQwi8/5ev8knWla0GgMxtenc=	hsNKdWFX3JMb3JKiagd0iA==
26	guy65	J3Al38+OFjEzCyxvN24p4PhdCUSXtpZ5qkwnHaNjisU=	zGDvjYL0GeWVm9Zypn3+1Q==
22	alinchicks	pf3ppDMtdg0Zyf6Y97cCX3cYwQFz/+JpJL7MaYsw9yo=	nsNp+kMtT9HxITP27HbDCQ==
25	kolka54	pjxFQV8FOGjQLja7Zo4BjW8EGSg6QlVmBSxKBZWzYII=	ve7mvFx3y4CnLCSD7ZzAbw==
27	alesyalal	d+7otS3CAy5vlcJNlJDP2oK5zAvnhys3ZdbeYHL7Auc=	MZV+Wpv5njFhyBZNUVkLeg==
28	kucheryashka43	X1UV1/mjfNKdNxEWnl17vxCnIYUUYqwfWuD8WWgXEUM=	2mBlV7k4ARTiEuLo/z2lbA==
\.


--
-- TOC entry 3290 (class 0 OID 0)
-- Dependencies: 221
-- Name: Banks_bank_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Banks_bank_id_seq"', 5, true);


--
-- TOC entry 3291 (class 0 OID 0)
-- Dependencies: 214
-- Name: User_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."User_user_id_seq"', 28, true);


--
-- TOC entry 3124 (class 2606 OID 16534)
-- Name: Accounts Account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Accounts"
    ADD CONSTRAINT "Account_pkey" PRIMARY KEY (accountnumber);


--
-- TOC entry 3120 (class 2606 OID 16541)
-- Name: Admins Admins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Admins"
    ADD CONSTRAINT "Admins_pkey" PRIMARY KEY (admin_id);


--
-- TOC entry 3128 (class 2606 OID 16676)
-- Name: Banks Banks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Banks"
    ADD CONSTRAINT "Banks_pkey" PRIMARY KEY (bank_id);


--
-- TOC entry 3122 (class 2606 OID 16529)
-- Name: Customers Customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Customers"
    ADD CONSTRAINT "Customers_pkey" PRIMARY KEY (customer_id);


--
-- TOC entry 3126 (class 2606 OID 16569)
-- Name: Transactions Transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Transactions"
    ADD CONSTRAINT "Transactions_pkey" PRIMARY KEY (transaction_id);


--
-- TOC entry 3118 (class 2606 OID 16489)
-- Name: Users User_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Users"
    ADD CONSTRAINT "User_pkey" PRIMARY KEY (user_id);


--
-- TOC entry 3131 (class 2606 OID 16708)
-- Name: Accounts account_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Accounts"
    ADD CONSTRAINT account_id FOREIGN KEY (account_id) REFERENCES public."Customers"(customer_id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 3129 (class 2606 OID 16498)
-- Name: Admins admin_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Admins"
    ADD CONSTRAINT admin_id FOREIGN KEY (admin_id) REFERENCES public."Users"(user_id) NOT VALID;


--
-- TOC entry 3132 (class 2606 OID 16677)
-- Name: Accounts bank_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Accounts"
    ADD CONSTRAINT bank_id FOREIGN KEY (bank_id) REFERENCES public."Banks"(bank_id) NOT VALID;


--
-- TOC entry 3130 (class 2606 OID 16703)
-- Name: Customers customer_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Customers"
    ADD CONSTRAINT customer_id FOREIGN KEY (customer_id) REFERENCES public."Users"(user_id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 3133 (class 2606 OID 16713)
-- Name: Transactions from; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Transactions"
    ADD CONSTRAINT "from" FOREIGN KEY (fromaccount_id) REFERENCES public."Accounts"(accountnumber) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 3134 (class 2606 OID 16718)
-- Name: Transactions to; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Transactions"
    ADD CONSTRAINT "to" FOREIGN KEY (toaccount_id) REFERENCES public."Accounts"(accountnumber) ON DELETE CASCADE NOT VALID;


-- Completed on 2023-09-03 21:07:28

--
-- PostgreSQL database dump complete
--

