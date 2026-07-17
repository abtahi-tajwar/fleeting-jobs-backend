import json

from app.scraping.scraping_service import extract_job_details

def job_details_fetch_callback(ch, method, properties, body):
    message = json.loads(body)
    print(message)
    print(message["id"])
    print(message["url"])

    ch.basic_ack(delivery_tag=method.delivery_tag)
    extract_job_details(message["url"])