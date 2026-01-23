create table app_user(
                         id uuid primary key,
                         created_at timestamptz not null,
                         updated_at timestamptz,
                         created_by_id uuid not null,
                         updated_by_id uuid,
                         active boolean not null,
                         full_name varchar(150) not null,
                         email varchar(200) not null,
                         password_hash varchar(100) not null,
                         user_role varchar(30) not null,
                         CONSTRAINT fk_app_user_created_by_id FOREIGN KEY(created_by_id) REFERENCES app_user(id) ON DELETE restrict DEFERRABLE INITIALLY DEFERRED,
                         CONSTRAINT fk_app_user_updated_by_id FOREIGN KEY(updated_by_id) REFERENCES app_user(id) ON DELETE restrict
);

create table address(
                       id uuid primary key,
                       created_at timestamptz not null,
                       updated_at timestamptz,
                       created_by_id uuid not null,
                       updated_by_id uuid,
                       active boolean not null,
                       street varchar(150) not null,
                       street_number varchar(20) not null,
                       neighborhood varchar(150) not null,
                       state_code varchar(2) not null,
                       city varchar(100) not null,


                       CONSTRAINT fk_adress_created_by_id FOREIGN KEY(created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                       CONSTRAINT fk_adress_updated_by_id FOREIGN KEY(updated_by_id) REFERENCES app_user(id) ON DELETE restrict
);

create table customer(
                         id uuid primary key,
                         created_at timestamptz not null,
                         updated_at timestamptz,
                         created_by_id uuid not null,
                         updated_by_id uuid,
                         active boolean not null,
                         email varchar(200) not null,
                         phone varchar(25) not null,
                         address_id uuid not null,

                         CONSTRAINT fk_customer_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                         CONSTRAINT fk_customer_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                         CONSTRAINT fk_customer_address_id FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE restrict
);

create table corporate_customer(
                                   id uuid primary key,
                                   legal_name varchar(150) not null,
                                   trade_name varchar(70),
                                   state_registration varchar(15),
                                   cnpj varchar(14) not null,

                                   CONSTRAINT fk_corporate_customer_id FOREIGN KEY (id) REFERENCES customer(id) ON DELETE restrict
);

create table individual_customer(
                                    id uuid primary key,
                                    full_name varchar(150) not null,
                                    cpf varchar(11) not null,

                                    CONSTRAINT fk_individual_customer_id FOREIGN KEY (id) REFERENCES customer(id) ON DELETE restrict
);

create table product(
                        id uuid primary key,
                        created_at timestamptz not null,
                        updated_at timestamptz,
                        created_by_id uuid not null,
                        updated_by_id uuid,
                        active boolean not null,
                        sku varchar(85) not null,
                        name varchar(150) not null,
                        description varchar(300),
                        price decimal(15,2) not null,

                        CONSTRAINT fk_product_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                        CONSTRAINT fk_product_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict
);

create table stock(
                      id uuid primary key,
                      created_at timestamptz not null,
                      updated_at timestamptz,
                      created_by_id uuid not null,
                      updated_by_id uuid,
                      active boolean not null,
                      product_id uuid not null,
                      quantity int not null,

                      CONSTRAINT fk_stock_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                      CONSTRAINT fk_stock_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                      CONSTRAINT fk_stock_product_id FOREIGN KEY (PRODUCT_ID) REFERENCES product (id) ON DELETE restrict
);

create table seller(
                       id uuid primary key,
                       created_at timestamptz not null,
                       updated_at timestamptz,
                       created_by_id uuid not null,
                       updated_by_id uuid,
                       active boolean not null,
                       full_name varchar(150) not null,
                       cpf varchar(11) not null,
                       email varchar(200) not null,
                       phone varchar(25) not null,

                       CONSTRAINT fk_seller_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                       CONSTRAINT fk_seller_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict
);

create table sales_order(
                            id uuid primary key,
                            created_at timestamptz not null,
                            updated_at timestamptz,
                            created_by_id uuid not null,
                            updated_by_id uuid,
                            active boolean not null,
                            order_code varchar(6) unique not null,
                            customer_id uuid not null,
                            seller_id uuid not null,
                            subtotal_amount decimal(15,2) not null,
                            order_discount_amount decimal(15,2) not null,
                            total_amount decimal(15,2) not null,

                            CONSTRAINT fk_sales_order_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                            CONSTRAINT fk_sales_order_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                            CONSTRAINT fk_sales_order_customer_id FOREIGN KEY (CUSTOMER_ID) REFERENCES customer(id) ON DELETE restrict,
                            CONSTRAINT fk_sales_order_seller_id FOREIGN KEY (SELLER_ID) REFERENCES seller(id) ON DELETE restrict
);

create table sales_order_item(
                                 id uuid primary key,
                                 created_at timestamptz not null,
                                 updated_at timestamptz,
                                 created_by_id uuid not null,
                                 updated_by_id uuid,
                                 active boolean not null,
                                 sales_order_id uuid not null,
                                 PRODUCT_ID uuid not null,
                                 QUANTITY int not null,
                                 UNIT_PRICE decimal(15,2) not null,
                                 UNIT_DISCOUNT_AMOUNT decimal(15,2) not null,

                                 CONSTRAINT fk_sales_order_item_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                                 CONSTRAINT fk_sales_order_item_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                                 CONSTRAINT fk_sales_order_item_sales_order_id FOREIGN KEY (sales_order_id) REFERENCES sales_order(id) ON DELETE restrict,
                                 CONSTRAINT fk_sales_order_item_product_id FOREIGN KEY (PRODUCT_ID) REFERENCES product(id) ON DELETE restrict
);

create table financial_transaction(
                                      id uuid primary key,
                                      created_at timestamptz not null,
                                      updated_at timestamptz,
                                      created_by_id uuid not null,
                                      updated_by_id uuid,
                                      active boolean not null,
                                      sales_order_id uuid not null,
                                      status varchar(30) not null,
                                      payment_method varchar(30) not null,
                                      payment_term varchar(30) not null,

                                      CONSTRAINT fk_financial_transaction_created_by_id FOREIGN KEY (created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                                      CONSTRAINT fk_financial_transaction_updated_by_id FOREIGN KEY (updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                                      CONSTRAINT fk_financial_transaction_sales_order_id FOREIGN KEY(sales_order_id) REFERENCES sales_order(id) ON DELETE restrict
);

create table installment(
                            id uuid primary key,
                            created_at timestamptz not null,
                            updated_at timestamptz,
                            created_by_id uuid not null,
                            updated_by_id uuid,
                            active boolean not null,
                            financial_transaction_id uuid not null,
                            installment_number int not null,
                            installment_amount decimal(15,2) not null,
                            due_date date not null,
                            paid boolean not null,

                            CONSTRAINT fk_installment_created_by_id FOREIGN KEY(created_by_id) REFERENCES app_user(id) ON DELETE restrict,
                            CONSTRAINT fk_installment_updated_by_id FOREIGN KEY(updated_by_id) REFERENCES app_user(id) ON DELETE restrict,
                            CONSTRAINT fk_installment_financial_transaction_id FOREIGN KEY (FINANCIAL_TRANSACTION_ID) REFERENCES financial_transaction(id) ON DELETE restrict
);