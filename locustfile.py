from locust import task, between
from locust.contrib.fasthttp import FastHttpUser
from random import randrange

# Usage locust --headless -t 15m -u 800 -r 200 -H https://localhost:8080

class SortingUser(FastHttpUser):
    wait_time = between(0.1, 0.3)

    @task
    def sorting(self):
        size = randrange(1, 300)
        self.client.post("/v1/delegated/sorting", json={"arraySize": size})

