CREATE TABLE IF NOT EXISTS "event" (
    "event_id" UUID NOT NULL,
    "created_dt" TIMESTAMP NULL DEFAULT NULL,
    "modified_dt" TIMESTAMP NULL DEFAULT NULL,
    "category" VARCHAR(20) NOT NULL,
    "description" VARCHAR(2000) NULL DEFAULT NULL,
    "ended_dt" TIMESTAMP NULL DEFAULT NULL,
    "event_location" VARCHAR(50) NULL DEFAULT NULL,
    "region" INTEGER NULL DEFAULT NULL,
    "reservation_deadline_dt" TIMESTAMP NULL DEFAULT NULL,
    "started_dt" TIMESTAMP NULL DEFAULT NULL,
    "title" VARCHAR(50) NOT NULL,
    PRIMARY KEY ("event_id")
);

-- 테이블 public.event_image 구조 내보내기
CREATE TABLE IF NOT EXISTS "event_image" (
    "image_id" UUID NOT NULL,
    "data" BYTEA NULL DEFAULT NULL,
    "event_id" UUID NULL DEFAULT NULL,
    "mimetype" VARCHAR(255) NULL DEFAULT NULL,
    "original_name" VARCHAR(255) NULL DEFAULT NULL,
    "size" BIGINT NULL DEFAULT NULL,
    PRIMARY KEY ("image_id"),
    CONSTRAINT "fk9oirj7cwmu7k91vr0m13hqh8b" FOREIGN KEY ("event_id") REFERENCES "public"."event" ("event_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- 테이블 public.event_tag 구조 내보내기
CREATE TABLE IF NOT EXISTS "event_tag" (
    "event_event_id" UUID NOT NULL,
    "tag" VARCHAR(50) NULL DEFAULT NULL,
    CONSTRAINT "fkp088st12mkgls94mxb4twsbye" FOREIGN KEY ("event_event_id") REFERENCES "public"."event" ("event_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);