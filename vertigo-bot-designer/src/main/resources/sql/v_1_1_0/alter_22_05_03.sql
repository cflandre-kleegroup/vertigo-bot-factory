ALTER TABLE CHATBOT_CUSTOM_CONFIG ADD COMMENT bool DEFAULT false;

ALTER TABLE CHATBOT_CUSTOM_CONFIG ADD COMMENT_MESSAGE VARCHAR(100);

comment on column CHATBOT_CUSTOM_CONFIG.COMMENT is
'Comment';

comment on column CHATBOT_CUSTOM_CONFIG.COMMENT_MESSAGE is
'Comment message';