from fastapi import FastAPI
from app.scraping.scraping_service import navigate_to

app = FastAPI()

@app.get("/")
def root():
    return {"message": "Application is running successfully!"}

@app.get("/jobs/search/{company_id}")
def search_jobs(company_id: int):
    return navigate_to("https://jobs.rbc.com/ca/en/c/technology-analytics-research-jobs")
    

