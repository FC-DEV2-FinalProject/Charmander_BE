import uuid

import boto3
import botocore
from botocore.config import Config

from ..aop.retry import retry_on_exception


class S3Storage:
    _s3: boto3.client

    def __init__(self, access_key: str, secret_key: str, bucket_name: str, endpoint_url: str, region: str):
        self.bucket_name = bucket_name
        self._s3 = boto3.client(
            's3',
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key,
            endpoint_url=endpoint_url,
            config=Config(
                region_name=region,
                signature_version='v4'
            )
        )

    def create_bucket(self, bucket_name: str):
        self._s3.create_bucket(Bucket=bucket_name)

    def is_bucket_exists(self, bucket_name: str) -> bool:
        try:
            self._s3.head_bucket(Bucket=bucket_name)
            return True
        except boto3.client('s3').exceptions.ClientError as e:
            return False

    def get_obj(self, file_name: str) -> bytes:
        obj = self._s3.Object(self.bucket_name, file_name)
        return obj.get()['Body'].read()

    def head_obj(self, key: str, bucket_name: str = None) -> dict:
        bucket_name = self.bucket_name if bucket_name is None else bucket_name
        return self._s3.head_object(Bucket=bucket_name, Key=key)

    @retry_on_exception(botocore.exceptions.ClientError, message="PreconditionFailed", retries=3, delay=0.1)
    def put_obj(self, file_name: str, data: bytes, bucket_name: str = None) -> str:
        """
            Put object to the bucket

            :param file_name: Object key
            :param data: Object data
            :param bucket_name: Bucket name

            :return: Object key

            :raises Exception: If bucket not found
        """

        bucket_name = self.bucket_name if bucket_name is None else bucket_name

        params = {
            "Bucket": bucket_name,
            "Key": f"1",
            "Body": data,
            "IfNoneMatch": "*",  # Do not overwrite existing object
            "Metadata": {
                "filename": file_name
            }
        }
        self._s3.put_object(**params)
        return params["Key"]

    def get_presigned_url(self, key: str, expiration: int = 3600) -> str:
        """
            Generate presigned url for the object

            :param key: Object key
            :param expiration: Expiration time in seconds

            :return: Presigned url

            :raises Exception: If object not found
        """

        meta = self.head_obj(key)
        if meta is None:
            raise Exception(f"Object not found: {key}")

        params = {
            "Params": {
                "Bucket": self.bucket_name,
                "Key": key,
                "ResponseContentDisposition": f"attachment; filename={meta['Metadata']['filename']}",
            },
            "ExpiresIn": expiration,
        }
        return self._s3.generate_presigned_url('get_object', **params)
