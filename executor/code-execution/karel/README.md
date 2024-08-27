## Build  (once)
```bash
docker build -t karel-code-runner .
```

## Clean up (Before run)
```bash
rm -r example/result
rm example/src/*.class
```

## Run
```bash
export UID=$(id -u)
export GID=$(id -g)
docker-compose up
```