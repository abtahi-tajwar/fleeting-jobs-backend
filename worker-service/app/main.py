from fastapi import FastAPI
from contextlib import asynccontextmanager
from app.common.rabbit.rabbit_config import REQUEST_JOB_DETAILS_QUEUE
from app.common.rabbit.rabbit_service import RabbitService
from app.modules.jobs.jobs_callback import job_details_fetch_callback
from app.scraping.scraping_service import parse_jobs
from app.example_parser_config.example_parser_config import load_config
from threading import Thread

rabbit_service = RabbitService()
def start_consumer():
    rabbit_service.declare_queue(REQUEST_JOB_DETAILS_QUEUE)
    rabbit_service.consume(
        REQUEST_JOB_DETAILS_QUEUE,
        job_details_fetch_callback
    )
    rabbit_service.start()

@asynccontextmanager
async def lifespan(app: FastAPI):
    thread = Thread(target=start_consumer, daemon=True)
    thread.start()
    print("RabbitMQ consumer started")
    yield
    print("Stopping RabbitMQ consumer")
    rabbit_service.stop()

app = FastAPI(lifespan=lifespan)

@app.get("/")
def root():
    return {"message": "Application is running successfully!"}

@app.get("/jobs/search/{company_id}")
def search_jobs(company_id: int):
    config = load_config(f"rbc")
    return parse_jobs("https://jobs.rbc.com/ca/en/c/technology-analytics-research-jobs", config)
    

