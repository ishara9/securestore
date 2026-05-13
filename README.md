# Store Application
The Store application keeps track of customers and orders in a database.

# Assumptions
This README assumes you're using a posix environment. It's possible to run this on Windows as well:
* Instead of `./gradlew` use `gradlew.bat`
* The syntax for creating the Docker container is different. You could also install PostgreSQL on bare metal if you prefer


# Prerequisites
This service assumes the presence of a postgresql 16.2 database server running on localhost:5433 (note the non-standard port)
It assumes a username and password `admin:admin` can be used.
It assumes there's already a database called `store`

You can start the PostgreSQL instance like this:
```shell
docker run -d \
  --name postgres \
  --restart always \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=store \
  -v postgres:/var/lib/postgresql/data \
  -p 5433:5432 \
  postgres:16.2 \
  postgres -c wal_level=logical
```

# Running the application
You should be able to run the service using
```shell
./gradlew bootRun
```

The application uses Liquibase to migrate the schema. Some sample data is provided. You can create more data by reading the documentation in utils/README.md

# Data model
An order has an ID, a description, and is associated with the customer which made the order.
A customer has an ID, a name, and 0 or more orders.

# API
Two endpoints are provided:
   * /order
   * /customer

Each of them supports a POST and a GET. The data model is circular - a customer owns a number of orders, and that order necessarily refers back to the customer which owns it.
To avoid loops in the serializer, when writing out a Customer or an Order, they're mapped to CustomerDTO and OrderDTO which contain truncated versions of the dependent object - CustomerOrderDTO and OrderCustomerDTO respectively.

The API is documented in the OpenAPI file OpenAPI.yaml. Note that this spec includes part of one of the tasks below (the new /products endpoint)

# Tasks

1. Extend the order endpoint to find a specific order, by ID
2. Extend the customer endpoint to find customers based on a query string to match a substring of one of the words in their name
3. Users have complained that in production the GET endpoints can get very slow. The database is unfortunately not co-located with the application server, and there's high latency between the two. Identify if there are any optimisations that can improve performance
4. Add a new endpoint /products to model products which appear in an order:
      * A single order contains 1 or more products. 
      * A product has an ID and a description. 
      * Add a POST endpoint to create a product
      * Add a GET endpoint to return all products, and a specific product by ID
        * In both cases, also return a list of the order IDs which contain those products
      * Change the orders endpoint to return a list of products contained in the order

# Bonus points
1. Implement a CI pipeline on the platform of your choice to build the project and deliver it as a Dockerized image

# Notes on the tasks
Assume that the project represents a production application.
Think carefully about the impact on performance when implementing your changes
The specifications of the tasks have been left deliberately vague. You will be required to exercise judgement about what to deliver - in a real world environment, you would clarify these points in refinement, but since this is a project to be completed without interaction, feel free to make assumptions - but be prepared to defend them when asked.
There's no CI pipeline associated with this project, but in reality there would be. Consider the things that you would expect that pipeline to verify before allowing your code to be promoted
Feel free to refactor the codebase if necessary. Bad choices were deliberately made when creating this project.

---

# Recent Updates

## API Enhancements

The following API endpoints have been extended and modernized:

### Customer API (v2)
- `GET /v2/customer` - Retrieve all customers (with pagination)
  - Query parameter: `name` (optional) - Filter customers by name substring
  - Example: `GET /v2/customer?name=john&page=0&size=10`

### Order API (v2)
- `GET /v2/order` - Retrieve all orders (with pagination)
  - Example: `GET /v2/order?page=0&size=10`

### Legacy API (v1)
- `GET /customer` - Retrieve all customers (legacy endpoint)
- `POST /customer` - Create a new customer
- `GET /order` - Retrieve all orders
- `POST /order` - Create a new order
- `GET /order/{id}` - Retrieve a specific order by ID

### Product Endpoints
- `POST /products` - Create a new product
- `GET /products` - Retrieve all products (with pagination)
  - Example: `GET /products?page=0&size=10`
- `GET /products/{id}` - Retrieve a specific product by ID
- Products include a list of order IDs they are contained in

For full API specifications, refer to the OpenAPI.yaml file.

## Running with Docker Compose

A `docker-compose.yml` file is included for easy local development and testing.

### Prerequisites
- Docker and Docker Compose installed on your system

### Quick Start

1. Start all services (PostgreSQL and Redis):
```shell
docker-compose up -d
```

2. Build and run the application:
```shell
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

### Services Included
- **PostgreSQL 16.2** - Database server on port 5433
  - Default credentials: `admin:admin`
  - Database name: `store`
- **Redis 7** - Cache/session store on port 6379

### Useful Docker Compose Commands
```shell
# View logs from all services
docker-compose logs -f

# View logs from a specific service
docker-compose logs -f postgres
docker-compose logs -f redis

# Stop all services
docker-compose down

# Stop services and remove volumes
docker-compose down -v

# Restart services
docker-compose restart
```
