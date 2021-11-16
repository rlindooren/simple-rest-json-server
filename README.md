# Simple REST JSON server

Run a HTTP REST server that serves JSON content from files.

Uses the [Ktor frame work](https://ktor.io/)

## Usage

Provide REST path and file mapping 'routes' as arguments, e.g.:

`--route "/api/products/=/data/allProducts.json" --route "/api/customers/=/data/allCustomers.json"`

### Run with Gradle

```shell
./gradlew run --args="--route /api/products/=example-data/allProducts.json --route /api/customers/=example-data/allCustomers.json"
```

### Docker

#### Build

```shell
./gradlew installDist

docker build -t simple-rest-json-server .
```

#### Run

```shell
# Update this to match your own set-up
DATA_DIR="$(pwd)/example-data/"
PORT=8080

docker run \
  --volume "${DATA_DIR}:/data/" \
  -p "${PORT}:${PORT}" \
  --env "PORT=${PORT}" \
  simple-rest-json-server \
  --route "/api/products/=/data/allProducts.json" \
  --route "/api/customers/=/data/allCustomers.json"
```

Then goto [http://localhost:8080/](http://localhost:8080/).
