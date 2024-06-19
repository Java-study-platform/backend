CREATE TABLE achievements(
  id UUID PRIMARY KEY ,
  name VARCHAR(255) NOT NULL ,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE achievement_progresses(
    id UUID PRIMARY KEY ,
    user_id UUID NOT NULL ,
    achievement_id UUID NOT NULL,
    CONSTRAINT fk_achievement_progress_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_achievement_progress_achievement_id FOREIGN KEY (achievement_id) REFERENCES achievements (id)
);

INSERT INTO achievements (id, name, description)
VALUES ('adc04c2c-e264-4bb2-bdaa-96256cb66ee0', 'Нуб', 'Вау, ты так крут, решил 10 задач'),
       ('46b3b476-a07b-49cd-b9f0-280ada3a9c5d', 'Гуру', 'Поздравляю, ты встал на путь истинного мастера и уже решил 100 задач'),
       ('3e77e40b-b5bf-4655-9964-a5566d431388', 'Истинный мастер', 'Ну, теперь тебя не перебить, 1000 задач - это уже сумасшествие');
