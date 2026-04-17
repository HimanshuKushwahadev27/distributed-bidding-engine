CREATE SEQUENCE auction_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE bid_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE auctions(
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('auction_id_seq'),   
    product_id BIGINT NOT NULL,
    starting_price DOUBLE PRECISION NOT NULL,
    current_highest_bid DOUBLE PRECISION,
    highest_bidder_id BIGINT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE bids(
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('bid_id_seq'),
    auction_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL
);