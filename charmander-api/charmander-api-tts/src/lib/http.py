import requests
from pydantic import BaseModel


class HTTPClient:
    def get(self, url: str, params: dict[str, any] | list[tuple[str, any]]) -> requests.Response:
        return requests.get(url, params=params)

    def post(self, url: str, json: str = None) -> requests.Response:
        if isinstance(json, BaseModel):
            json = json.model_dump_json(indent=0)
        return requests.post(url, json=json)
