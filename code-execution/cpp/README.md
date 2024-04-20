## Build  (once)
```bash
docker build -t cpp-code-runner .
```

## Clean up (Before run)
```bash
rm -r example/result
rm example/src/main
```

## Run
```bash
export UID=$(id -u)
export GID=$(id -g)
docker-compose up 
```