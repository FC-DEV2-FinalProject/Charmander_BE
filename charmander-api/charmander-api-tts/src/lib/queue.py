from abc import ABC, abstractmethod
from concurrent.futures import ThreadPoolExecutor as Pool


class TaskQueue(ABC):
    @abstractmethod
    def submit(self, func, *args, **kwargs):
        pass


class LocalTaskQueue(TaskQueue):
    def __init__(self):
        self._pool = Pool()

    def submit(self, func, *args, **kwargs):
        return self._pool.submit(func, *args, **kwargs)
