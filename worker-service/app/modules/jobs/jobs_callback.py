import json
from app.modules.jobs.job_parser import job_parser

def job_details_fetch_callback(ch, method, properties, body):
    message = json.loads(body)
    print(message)
    print(message["id"])
    print(message["url"])

    ch.basic_ack(delivery_tag=method.delivery_tag)
    job_parser.extract_job_details(message["url"])