CREATE TABLE cart(
                     id INT AUTO_INCREMENT NOT NULL,
                     product_codes VARCHAR(500) NOT NULL,
                     PRIMARY KEY (id)
);

CREATE TABLE purchase(
                         id INT AUTO_INCREMENT NOT NULL,
                         cart_id INT NOT NULL REFERENCES cart(id),
                         product_codes VARCHAR(500) NOT NULL,
                         PRIMARY KEY (id)
);



