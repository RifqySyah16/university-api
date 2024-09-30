INSERT INTO public.teacher (name, email, address) VALUES
    ('John Doe', 'john.doe@example.com', '123 Main St, Springfield'),
    ('Jane Smith', 'jane.smith@example.com', '456 Oak St, Riverdale'),
    ('Michael Johnson', 'michael.johnson@example.com', '789 Pine St, Sunnyvale'),
    ('Emily Davis', 'emily.davis@example.com', '321 Maple St, Brookfield'),
    ('Robert Brown', 'robert.brown@example.com', '654 Cedar St, Hillcrest'),
    ('Linda Wilson', 'linda.wilson@example.com', '987 Birch St, Greenville'),
    ('William Taylor', 'william.taylor@example.com', '246 Elm St, Lakeside'),
    ('Elizabeth Anderson', 'elizabeth.anderson@example.com', '135 Walnut St, Kingsport'),
    ('James Thomas', 'james.thomas@example.com', '369 Chestnut St, Belmont'),
    ('Patricia White', 'patricia.white@example.com', '753 Aspen St, Highland');

INSERT INTO public.student (name, email, address) VALUES
    ('John Doe', 'johndoe@example.com', '123 Main St, City A'),
    ('Jane Smith', 'janesmith@example.com', '456 Maple Ave, City B'),
    ('Alice Johnson', 'alicej@example.com', '789 Oak Dr, City C'),
    ('Bob Brown', 'bobbrown@example.com', '101 Pine St, City D'),
    ('Charlie Green', 'charliegreen@example.com', '202 Elm St, City E'),
    ('David White', 'davidwhite@example.com', '303 Cedar St, City F'),
    ('Eve Black', 'eveblack@example.com', '404 Birch Rd, City G'),
    ('Frank Wright', 'frankwright@example.com', '505 Walnut Ln, City H'),
    ('Grace Hall', 'gracehall@example.com', '606 Poplar St, City I'),
    ('Hank Miller', 'hankmiller@example.com', '707 Sycamore Ave, City J');

INSERT INTO public.course (course_name, description) VALUES 
('Mathematics', 'Basic Mathematics course covering algebra and geometry.'),
('Physics', 'Introduction to Physics with fundamental concepts.'),
('Chemistry', 'Basic Chemistry course including experiments.'),
('Biology', 'Study of living organisms and their environments.'),
('Computer Science', 'Introduction to programming and algorithms.'),
('History', 'World history from ancient times to modern era.'),
('Geography', 'Study of physical features of Earth.'),
('English Literature', 'Introduction to classic and modern literature.'),
('Economics', 'Basic principles of economics including micro and macroeconomics.'),
('Philosophy', 'Introduction to Western philosophy and ethics.');

INSERT INTO public.application_user
(name, email, username, password )
VALUES('Rifqy', 'rifqy.syah@rocketmail.com', 'rifqy16', '$2a$12$qesJBOVbo/C.2NL.gfH7ueF9APKKbFWv1PTM9DMp9G.HNIkyBo8vi');