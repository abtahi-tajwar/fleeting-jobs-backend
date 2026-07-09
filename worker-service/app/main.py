from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def root():
    return {"message": "Application is running successfully!"}

@app.get("/jobs/search/{company_id}")
def search_jobs(company_id: int):
    return {"jobs": [f"Example jobs for company {company_id}"]}