from fastapi import FastAPI
from app.scraping.scraping_service import parse_jobs
from app.example_parser_config.example_parser_config import load_config

app = FastAPI()

@app.get("/")
def root():
    return {"message": "Application is running successfully!"}

@app.get("/jobs/search/{company_id}")
def search_jobs(company_id: int):
    config = load_config(f"rbc")
    return parse_jobs("https://jobs.rbc.com/ca/en/c/technology-analytics-research-jobs", config)
    

