ALTER TABLE category ADD code VARCHAR(36) NOT NULL AFTER id;
UPDATE category SET code = uuid();
ALTER TABLE category ADD CONSTRAINT uk_code UNIQUE(code);