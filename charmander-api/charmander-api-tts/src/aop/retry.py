import time
from functools import wraps

from logging import getLogger

logger = getLogger(__name__)


def retry_on_exception(exception, message, retries=3, delay=1):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            last_error = None
            for attempt in range(retries):
                try:
                    return func(*args, **kwargs)
                except exception as e:
                    last_error = e
                    if str(e).find(message) != -1:
                        logger.warning(
                            f"function {func.__name__} failed with {e}, retrying... ({attempt + 1}/{retries}) with delay {delay}")
                        time.sleep(delay)
                    else:
                        raise
            raise last_error
        return wrapper
    return decorator
