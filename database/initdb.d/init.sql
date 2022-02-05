-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        PostgreSQL 14.1 (Debian 14.1-1.pgdg110+1) on x86_64-pc-linux-gnu, compiled by gcc (Debian 10.2.1-6) 10.2.1 20210110, 64-bit
-- 서버 OS:                        
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 public.event 구조 내보내기
CREATE TABLE IF NOT EXISTS "event" (
	"event_id" UUID NOT NULL,
	"created_dt" TIMESTAMP NULL DEFAULT NULL,
	"modified_dt" TIMESTAMP NULL DEFAULT NULL,
	"category" VARCHAR(20) NOT NULL,
	"description" VARCHAR(2000) NULL DEFAULT 'NULL::character varying',
	"ended_dt" TIMESTAMP NULL DEFAULT NULL,
	"event_location" VARCHAR(50) NULL DEFAULT 'NULL::character varying',
	"region" VARCHAR NULL DEFAULT NULL,
	"reservation_deadline_dt" TIMESTAMP NULL DEFAULT NULL,
	"started_dt" TIMESTAMP NULL DEFAULT NULL,
	"title" VARCHAR(50) NOT NULL,
	PRIMARY KEY ("event_id")
);

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 public.event_image 구조 내보내기
CREATE TABLE IF NOT EXISTS "event_image" (
	"image_id" UUID NOT NULL,
	"data" BYTEA NULL DEFAULT NULL,
	"event_id" UUID NULL DEFAULT NULL,
	"mimetype" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"original_name" VARCHAR(255) NULL DEFAULT 'NULL::character varying',
	"size" BIGINT NULL DEFAULT NULL,
	PRIMARY KEY ("image_id"),
	CONSTRAINT "fk9oirj7cwmu7k91vr0m13hqh8b" FOREIGN KEY ("event_id") REFERENCES "public"."event" ("event_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 public.event_tag 구조 내보내기
CREATE TABLE IF NOT EXISTS "event_tag" (
	"event_event_id" UUID NOT NULL,
	"tag" VARCHAR(50) NULL DEFAULT 'NULL::character varying',
	CONSTRAINT "fkp088st12mkgls94mxb4twsbye" FOREIGN KEY ("event_event_id") REFERENCES "public"."event" ("event_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- 내보낼 데이터가 선택되어 있지 않습니다.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
