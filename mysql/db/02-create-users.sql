use `commenter`;

# auths
insert into auths (id, email, password) values ("01D90WXJT2CP60TCX74HEK5JEJ", "hoge@example.com", "$2a$10$sBFP36l1Le67XjxZrdlOSeemkbclJu4kiNWtWUK9TEPH.x5o79Jpi"); # みやしー, password
insert into auths (id, email, password) values ("01D92B2WXJV41YP3G3RVHJSFSV", "fuga@example.com", "$2a$10$nD1pmJ4.pZgomoMrlpTlJurbuWma.KCCOtzWChxl66ECZOiiHnqhG"); # ふにゃ, password
insert into auths (id, email, password) values ("01D92B4Y5F89PJPPV8Y8BDYY3Y", "piyo@example.com", "$2a$10$IfCQzhOvhMr7jG9phZ0e9.7KORfFDR1MYc3RjAziCFTuncCqZ4Cda"); # ひよこ, password

# users
insert into users (id, auth_id, name) values ("01D90WXJXCCB10KM24TAPNKHEQ", "01D90WXJT2CP60TCX74HEK5JEJ", "みやしー");
insert into users (id, auth_id, name) values ("01D92B2WZT4C2PKV70V65W9GJ0", "01D92B2WXJV41YP3G3RVHJSFSV", "ふにゃ");
insert into users (id, auth_id, name) values ("01D92B4Y7QHGQCK5N7849P6QC4", "01D92B4Y5F89PJPPV8Y8BDYY3Y", "ひよこ");

# tokens
insert into tokens (token, auth_id, created_at) values ("Bearer 1E5OY/Jck_CMhRicdzTixdgs/JXJtXo4HfbpJmU-IE6gv25Iq/71~lXsX1t0u3xb", "01D92B2WXJV41YP3G3RVHJSFSV", current_timestamp); # みやしー
insert into tokens (token, auth_id, created_at) values ("Bearer DcCxQARz1Gszsy.OJEuVwtn1MFzwrGS4vuChKOA.RrL+LM6BeKgX4Em5Fy2~30gT", "01D90WXJT2CP60TCX74HEK5JEJ", current_timestamp); # ふにゃ
insert into tokens (token, auth_id, created_at) values ("Bearer R2hlzuy.IEvUve9Yk~0tj/-SLc8w/JX-w9KwYbPkXqJUFYS4wWP5YQdpcFdtn/ZE", "01D92B4Y5F89PJPPV8Y8BDYY3Y", current_timestamp); # ひよこ

# follows
insert into follows (follower, followee) values ("01D90WXJXCCB10KM24TAPNKHEQ", "01D92B2WZT4C2PKV70V65W9GJ0"); # みやしー -> ふにゃ
insert into follows (follower, followee) values ("01D90WXJXCCB10KM24TAPNKHEQ", "01D92B4Y7QHGQCK5N7849P6QC4"); # みやしー -> ひよこ
insert into follows (follower, followee) values ("01D92B2WZT4C2PKV70V65W9GJ0", "01D92B4Y7QHGQCK5N7849P6QC4"); # ふにゃ -> ひよこ
insert into follows (follower, followee) values ("01D92B4Y7QHGQCK5N7849P6QC4", "01D90WXJXCCB10KM24TAPNKHEQ"); # ひよこ -> みやしー
