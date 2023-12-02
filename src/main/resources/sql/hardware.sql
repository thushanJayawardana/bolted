DROP DATABASE IF EXISTS hardware;
     CREATE DATABASE IF NOT EXISTS hardware;
        USE hardware;
            CREATE TABLE register(
                user_name VARCHAR(20) PRIMARY KEY,
                password VARCHAR(20)
            );

            CREATE TABLE product(
                p_Id VARCHAR(20) PRIMARY KEY,
                name VARCHAR(30) NOT NULL,
                type VARCHAR(30) NOT NULL,
                qty DOUBLE,
                price DOUBLE NOT NULL
            );

            CREATE TABLE employee(
                e_Id VARCHAR(20) PRIMARY KEY ,
                name VARCHAR(30) NOT NULL,
                email VARCHAR(30) NOT NULL ,
                mobile INT NOT NULL ,
                position VARCHAR(30)
            );

            CREATE TABLE customer(
                cust_Id VARCHAR(20) PRIMARY KEY ,
                name VARCHAR(30) NOT NULL ,
                address VARCHAR(30) NOT NULL ,
                mobile INT
            );

            CREATE TABLE supplier(
                sup_Id VARCHAR(20) PRIMARY KEY ,
                name VARCHAR(30) NOT NULL ,
                products VARCHAR(30) NOT NULL ,
                mobile INT NOT NULL
            );

            CREATE TABLE orders(
                order_Id VARCHAR(20) PRIMARY KEY ,
                description VARCHAR(30) NOT NULL ,
                order_date DATE NOT NULL ,
                cust_id VARCHAR(20),
                FOREIGN KEY (cust_Id) REFERENCES customer(cust_Id) ON UPDATE CASCADE ON DELETE CASCADE
            );

            CREATE TABLE product_details(
                pdts_Id VARCHAR(20) PRIMARY KEY ,
                p_Id VARCHAR(20),
                FOREIGN KEY (p_Id) REFERENCES product(p_Id) ON UPDATE CASCADE ON DELETE CASCADE
            );

            CREATE TABLE order_details(
                OD_Id VARCHAR(20) PRIMARY KEY ,
                order_Id VARCHAR(20),
                p_Id VARCHAR(20),
                FOREIGN KEY (order_Id) REFERENCES orders(order_Id) ON UPDATE CASCADE ON DELETE CASCADE ,
                FOREIGN KEY (p_Id) REFERENCES product(p_Id) ON UPDATE CASCADE ON DELETE CASCADE
            );

            CREATE TABLE supllier_details(
                SD_Id VARCHAR(20) PRIMARY KEY ,
                sup_Id VARCHAR(20),
                FOREIGN KEY (sup_Id) REFERENCES supplier(sup_Id)ON UPDATE CASCADE ON DELETE CASCADE
            );

            CREATE TABLE items(
                item_Id VARCHAR(20) PRIMARY KEY ,
                description VARCHAR(30),
                qty DOUBLE ,
                price DOUBLE
            );

DROP TABLE item;
CREATE TABLE item(
                      item_Id VARCHAR(20) PRIMARY KEY ,
                      sup_Id VARCHAR(20),
                      sup_name VARCHAR(30),
                      description VARCHAR(30),
                      qty DOUBLE ,
                      price DOUBLE
);
CREATE TABLE item(
                      item_Id VARCHAR(20) PRIMARY KEY ,
                      description VARCHAR(30),
                      qty DOUBLE ,
                      price DOUBLE
);

CREATE TABLE supplier_details (
    sup_Id varchar(20),
    item_Id varchar(20),
    FOREIGN KEY (sup_Id) REFERENCES supplier(sup_Id)ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (item_Id) REFERENCES item(item_Id)ON UPDATE CASCADE ON DELETE CASCADE

);

