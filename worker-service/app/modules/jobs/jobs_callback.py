import json
import aio_pika

from app.example_parser_config.example_parser_config import load_config
from app.modules.jobs.job_parser import job_parser


async def job_details_fetch_callback(message: aio_pika.IncomingMessage):
    payload = json.loads(message.body)

    print(f"Parsing description for {payload['id']}")

    config = load_config("rbc")

    await job_parser.extract_job_details(
        payload["id"],
        payload["url"],
        config,
    )